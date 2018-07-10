package com.unshape.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Point extends Circle{
	public boolean isAlive = true;
	
	private Vector2 offset;		//	Normalised
	private Animator animator;	//	For pop animation
	
	private int[] directions = {-1, 1};
	private int rotationDirection;
	private float rotationMagnitude = 1f;
	
	private ParticleEffect explosion;
	
	Point(float x,  float y, float radius, Main game) {
		this.radius = radius;
		
		offset = new Vector2(x,y);
		animator = new Animator(game);

		rotationDirection = directions[MathUtils.random(1)];
		rotationMagnitude = MathUtils.random(0.2f, 1.0f);

	}
	
	public void update(float x, float y, float shapeSize) {
		offset.rotate(rotationDirection * rotationMagnitude); //	Rotate each frame
		animator.update();	 
		
		setPosition(x + (offset.x * shapeSize), y + (offset.y * shapeSize)); //	Set points according to shape
	}
	//	Setters
	public void setRotationSpeed(float mag) {
		rotationMagnitude = mag;
	}
	
	public void setRotationDirection(int dir) {
		rotationDirection = dir;
	}
	
	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void pop(Shape shape) {
		animator.pop(shape, this);
	}
	
	//	Getters
	public float getRadius() {
		return radius;
	}
	
	public Vector2 getPosition() {
		return new Vector2(x,y);
	}
	
	public Vector2 getOffset() {
		return offset;
	}
	
	public int getRotationDirection() {
		return rotationDirection;
	}
	
	public float getRotationMagnitude() {
		return rotationMagnitude;
	}
	
	public Animator getAnimator() {
		return animator;
	}
	
	public void explodeEffect() {
		explosion.start();
	}
}
