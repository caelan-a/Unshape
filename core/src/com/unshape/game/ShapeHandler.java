package com.unshape.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.unshape.game.Screens.GameScreen.GameState;

//	@author Caelan Anderson
public class ShapeHandler {
	
	//	Management
	private Main game;
	private float screenW;
	private float screenH;
	
	//	Spawning
	private Spawner spawner;
	private Pool pool;
	
	//	Extras
	private boolean shapeMidDeath = false;
	
	public ShapeHandler(Main game) {
		this.game = game;
		screenW	  = game.getScreenSize().x;
		screenH   = game.getScreenSize().y;
	
		pool	  	  = new Pool();
		spawner   	  = new Spawner();
	}
	
	public void draw() {
		for (Shape s : pool.inUse)
			s.draw();
	}
	
	public void update() {
		spawner.update();
		checkOffScreen();
		
		for (int i = 0; i < pool.inUse.size(); i++)
			pool.inUse.get(i).update();
	}
	
	//	Getters
	public List<Shape> getShapes() {
		return pool.inUse;
	}

	public Pool getPool() {
		return pool;
	}
	
	public Spawner getSpawner() {
		return spawner;
	}
	
	//	Shape tracking
	public void checkCollision(float x, float y) {
		for(int i = 0; i < pool.inUse.size(); i++){
			for(int j = 0; j < pool.inUse.get(i).getPoints().size(); j++) {
				if(pool.inUse.get(i).getPoints().get(j).contains(x, y) && pool.inUse.get(i).getPoints().get(j).isAlive && !pool.inUse.get(i).getPoints().get(j).getAnimator().isPlaying()) {
					Assets.ting.play();
					pool.inUse.get(i).getPoints().get(j).pop(pool.inUse.get(i));
					shapeMidDeath = true;
					//game.gameScreen.hud.effectPool.checkOut(x, y);
					//	Determines whether one tap can hit underlying points 
					//break; 
				}
			}
		}
	}
	
	public void checkOffScreen(){
		for(int i = 0; i < pool.inUse.size(); i++){
			if(pool.inUse.get(i).getPosition().y < -100) {
				pool.checkIn(pool.inUse.get(i));
				game.gameScreen.health.setHealth(game.gameScreen.health.getHealth() - 1);
				//game.gameScreen.getBackground().flash(Colour.orangeA);
				game.gameScreen.powerUp.interuptSlowDown();
				Gdx.input.vibrate(200);
			}
		}
	}
	
	//	Creation handling
	public class Pool {
		private final int MAX_ELEMENTS = 10; // Pool size
		public List<Shape> available;		
		public List<Shape> inUse;			 
		private Vector2 initPos = new Vector2(0, -150);
		
		Pool () {
			available = new ArrayList<Shape>();
			inUse = new ArrayList<Shape>();
			
			for(int i = 0; i < MAX_ELEMENTS; i++) // Initialise available list
				available.add(new Shape(game, Colour.shapeColours[0], initPos.x, initPos.y, ShapeForm.createTriangle(35, game)));
		}
		
		//	Admin methods
		private Shape shapeToTransfer;
		public Shape checkOut() {
			if(available.size() < 1) {// If no pool.inUse are available extend pool
				available.add(new Shape(game, Colour.shapeColours[0], initPos.x, initPos.y, ShapeForm.createTriangle(35, game)));
			}
			shapeToTransfer = available.get(available.size()-1); // Get last shape
			inUse.add(shapeToTransfer);
			available.remove(shapeToTransfer);
			return shapeToTransfer;
		}
		
		public void checkIn(Shape shape) {
			if(shapeMidDeath) {
				popShape(shape);
				shapeMidDeath = false;
			}
			
			shape.reset();
			available.add(shape);
			inUse.remove(shape);		
		}
		
		public void popShape(Shape shape) {
			game.gameScreen.hud.addPopUp(shape.getPosition().x - 0.08f * game.getScreenSize().x, shape.getPosition().y + 3.8f * shape.speed, 0);
			game.gameScreen.hud.addPopUp(shape.getPosition().x, shape.getPosition().y + shape.speed / 2, Assets.gemIcon);
		}
		
		private Shape shapeToAdd;
		public void addShape(Color colour, float x, float y, List<Point> points) {
			//	Correct rotation for non-planar shapes
			if(points.size() > 3) {
				for(int i = 0; i < points.size()-2 ; i++) {
					points.get(i+1).setRotationDirection(points.get(i).getRotationDirection());
					points.get(i+1).setRotationSpeed(points.get(i).getRotationMagnitude());
				}
				points.get(points.size()-1).setRotationDirection(points.get(0).getRotationDirection());
				points.get(points.size()-1).setRotationSpeed(points.get(0).getRotationMagnitude());

			}
			shapeToAdd = checkOut();
			shapeToAdd.setColour(colour);
			shapeToAdd.setPosition(x * screenW, y * screenH);
			shapeToAdd.setPoints(points);
			//shapeToAdd.worth = MathUtils.random(((points.size() - 3) * 3 ) + 1, ((points.size() - 3) * 3 ) + points.size());
			
			//System.out.println("Pos: " + shapeToAdd.getPosition().x);
		}
		
		public void resetShapes() {
			for(int i = 0; i < inUse.size(); i++)
				checkIn(inUse.get(i));
		}
	}

	public class Spawner {
		public int shapesToSpawn;
		public int shapesSpawned;
		
		private float timer;
		
		private float spawnFrequency = 1.5f;
		private int maxShapeNum = 3;
		
		//	Holders for shape's new randomised properties
		private float column;
		private float xPosition;
		private float yPosition;
		private int shapeNum;	// Shape form   
		
		private float columnRange = 0.1f;
		private float[] columns;
		
		private float radius = game.getScreenSize().x / 9.6f;
		
		Spawner() {
			timer = spawnFrequency -0.1f; // Allows instant spawn at startup
			calibrateColumns(1);
		}
		
		//	Set column/row size based on fraction of screen dimensions
		public void calibrateColumns(int sections) {
			columns = new float[sections];
		
			for(int i = 0; i < columns.length; i++) {
				columns[i] = (((screenW / sections) / 2) + (i * screenW / sections)) / screenW;
				//System.out.println(columns[i]);
			}
		}
		
		public void setFrequency(float freq) {
			spawnFrequency = freq;
		}
		
		public void setMaxShapeNum(int num) {
			maxShapeNum = num;
		}
		
		public void update() {
			timer += Gdx.graphics.getDeltaTime();

			if(shapesSpawned >= shapesToSpawn) {
				game.gameScreen.gameState = GameState.Suspended;
			}
			
			if(timer > spawnFrequency) {
				if(game.gameScreen.gameState == GameState.Running) {
					shapesSpawned++;
					column = columns[MathUtils.random(columns.length-1)];
					
					//	Incorporate random padding range and compensate for screen edges
					if(column == columns[0] && columns.length > 1)
						xPosition = column + MathUtils.random(0, columnRange);
					else if(column == columns[columns.length-1] && columns.length > 1)
						xPosition = column + MathUtils.random(-columnRange, 0);
					else
						xPosition = column + MathUtils.random(-columnRange, columnRange);
					
					
					yPosition = MathUtils.random(1f) + 1.1f;
					shapeNum = MathUtils.random(3, maxShapeNum);

					switch(shapeNum) {

					case 3:
						pool.addShape(Colour.shapeColours[0], xPosition, yPosition, ShapeForm.createTriangle(radius, game));
						break;

					case 4:
						pool.addShape(Colour.shapeColours[1], xPosition, yPosition, ShapeForm.createSquare(radius, game));
						break;

					case 5:
						pool.addShape(Colour.shapeColours[2], xPosition, yPosition, ShapeForm.createPentagon(radius, game));
						break;

					case 6:
						pool.addShape(Colour.shapeColours[3], xPosition, yPosition, ShapeForm.createHexagon(radius, game));
						break;

					}
				}
				timer = 0; // Reset timer
			}
		}
	}
}

