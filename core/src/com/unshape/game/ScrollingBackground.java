package com.unshape.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class ScrollingBackground {
	private final int MAX_SHAPES = 20;
	
	private Main game;
	
	private List<Shape> foreground;	//	Shapes in foreground
	private List<Shape> background; //	Shapes in background
	
	private float globalSpeed = 2; 		// Factor by which all shapes move
	
	public ScrollingBackground(Main game) {
		this.game = game;
		foreground = new ArrayList<Shape>();
		background = new ArrayList<Shape>();
		
		for(int i = 0; i < MAX_SHAPES / 2; i++) {
			foreground.add(new Shape(MathUtils.random(1.0f) * game.getScreenSize().x, (MathUtils.random(1.0f) * game.getScreenSize().y), 1));
		}
		
		for(int i = 0; i < MAX_SHAPES / 2; i++) {
			background.add(new Shape(MathUtils.random(1.0f) * game.getScreenSize().x, (MathUtils.random(1.0f) * game.getScreenSize().y), 0));
		}
	}
	
	public void update() {
		for(Shape s : background) {
			s.update();
			s.draw();
		}
		for(Shape s : foreground) {
			s.update();
			s.draw();
		}
	}
	
	public void setSpeed(float speed) {
		globalSpeed = speed;
	}
	
	public float getSpeed() {
		return globalSpeed;
	}
	
	private class Shape {
		private final float widthRange = 0.1f;			// Normalised for screen dimensions
		private final float heightRange = 0.2f; 		// Normalised for screen dimensions
		
		private Rectangle bounds;
		private Color colour;
		private float alpha;
		private float speed;
		private int type;	//	background = 0, foreground = 1
		
		Shape(float x, float y, int type) {
			this.type = type;
			colour = Colour.fOrange;
			if(type == 0)
				alpha = 0.55f;
			else
				alpha = 0.95f;
			
			bounds = new Rectangle();
			randomiseProperties();
			bounds.x = x;
			bounds.y = y;
		}
		
		public void update() {
			//	Move
			bounds.y -= 3 * speed * globalSpeed;
			
			if(bounds.y < - bounds.height) {
				randomiseProperties();
			}
		}
		
		public void draw() {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
						
			game.renderer.begin(ShapeType.Filled);
			game.renderer.setColor(colour.r, colour.g, colour.b, alpha);
			game.renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
			game.renderer.end();
		}
		
		public void randomiseProperties() {
			//	Position
			bounds.x = MathUtils.random(1.0f) * game.getScreenSize().x;
			bounds.y = (MathUtils.random(0.2f) * game.getScreenSize().y) + game.getScreenSize().y; 
			
			// Size
			bounds.width = MathUtils.random(0.1f, widthRange) * game.getScreenSize().x;
			bounds.height = MathUtils.random(0.1f, heightRange) * game.getScreenSize().y;
			
			//	Speed
			if(type == 0)
				speed = MathUtils.random(0.1f, 0.3f);
			else
				speed = MathUtils.random(0.4f, 0.6f);
		}
	}
}
