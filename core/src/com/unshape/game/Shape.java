package com.unshape.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Shape {
	public static float globalSpeed = 1;
	public static float shapeSize = Gdx.graphics.getWidth() / 9.6f;			// Distance of points from shape center
	public static boolean instaKillActive = false;
	
	private Main game;						
	
	//	Speed
	public float speed = 20f;						//	Down speed
	private float smoothFactor; 			// Slow motion pass at center
	public static float smoothIntensity = 3;
	
	//	Class
	private Vector2 position; 
	private Color colour;
	private List<Point> points;
	private List<Point> drawablePoints;
	private InstaKill instaKill; //	Dot in centre shape, visible when max combo reached
	
	public int worth;
	
	Shape(Main game, Color colour, float x, float y, List<Point> newPoints) {
		this.game 		= game;
		this.colour	  	= colour;
		
		instaKill = new InstaKill();
		
		setPoints(newPoints);
		setPosition(x * game.getScreenSize().x, y * game.getScreenSize().y); // Normalised coordinates * screen dimension
	}

	public void draw() {
		drawPoints();
		drawEdges();
	}
	
	private final int yOffset = 10; //	Compensates register lag
	private void drawPoints() {
		instaKill.render();
		
		//	Draw shape points
		game.batch.begin();
		if(instaKill.active)
			game.batch.setColor(Colour.fAmethyst);
		else
			game.batch.setColor(getColour());
		for(Point p : points) 
			if(p.isAlive) {
				game.batch.draw(Assets.point, 
						p.getPosition().x - ((p.getRadius() - 15 ) / 2),
						p.getPosition().y - ((p.getRadius() - 15 ) / 2) - yOffset,
						p.getRadius() - 15,
						p.getRadius() - 15);
			}
		game.batch.end();
	}
	
	private void drawEdges() {
		game.renderer.begin(ShapeType.Filled);

		if(instaKill.active)
			game.renderer.setColor(Colour.fAmethyst);
		else
			game.renderer.setColor(getColour().r,getColour().g,getColour().b,getColour().a - 0.15f);
		//	Draw line between points
		for(int j = 0; j < drawablePoints.size() - 1; j++) {
			game.renderer.rectLine(drawablePoints.get(j).getPosition().x,
								   drawablePoints.get(j).getPosition().y - yOffset,
								   drawablePoints.get(j+1).getPosition().x,
								   drawablePoints.get(j+1).getPosition().y - yOffset,
							       game.getScreenSize().x / 96);
		}
		//	Draw line between first and last point
		game.renderer.rectLine(drawablePoints.get(drawablePoints.size()-1).getPosition().x,
							   drawablePoints.get(drawablePoints.size()-1).getPosition().y - yOffset,
							   drawablePoints.get(0).getPosition().x,
							   drawablePoints.get(0).getPosition().y - yOffset,
							   game.getScreenSize().x / 96);
		game.renderer.end();
	}

	public void update() {
		smoothFactor = smoothIntensity * Math.abs(((game.getScreenSize().y / 2f) - position.y) / (game.getScreenSize().y / 2f));
		
		setPosition(getPosition().x, getPosition().y - globalSpeed * (smoothFactor + 0.1f * speed * game.gameScreen.difficulty.getDifficulty() * Gdx.graphics.getDeltaTime() * game.getScreenSize().y));
		
		for(int i = 0; i < points.size(); i++) {
			points.get(i).update(position.x, position.y, shapeSize);
		}
	}
	
	//	Setters
	public void setPosition(float x, float y) {
		position = new Vector2(x, y);	//	Set shape position	
	}
	
	public void setColour(Color colour) {
		this.colour = colour;
	}
	
	public void setPoints(List<Point> newPoints) {
		points = newPoints;
		drawablePoints = new ArrayList<Point>(points);
		worth = newPoints.size() - 2;
	}
	
	public void removeDrawablePoint(Point point) {
		drawablePoints.remove(point);
	}
	
	public void reset() {
		position.x = 0;
		position.y = -100;
		setPoints(points);
	}
	
	//	Getters
	public List<Point> getDrawablePoints() {
		return drawablePoints;
	}
	
	public Color getColour() {
		return colour;
	}
	
	public List<Point> getPoints() {
		return points;
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public class InstaKill {
		/* Clickable circle that is rendered in the centre of a shape when max combo is reached. If clicked instantly destroys shape */
		
		public boolean active = false;
		public Circle bounds;
		
		public InstaKill() {
			bounds = new Circle();
			bounds.radius = 5;
		}
		
		private float timer;
		
		public void render() {
			if(instaKillActive)
				activate();
			else
				deactivate();
			
			timer += game.delta;
			
			bounds.x = position.x;
			bounds.y = position.y;
			
			if(active) {
				//draw();
			}
		}
		
		public void activate() {
			active = true;
		}
		
		public void deactivate() {
			active = false;
		}
	}
}


