package com.unshape.game;

import com.badlogic.gdx.Gdx;

public class Animator {
	private static float DURATION = Gdx.graphics.getWidth() / 60;
	private static float SPEED = DURATION * 8;	
	
	//	Management
	private Main game;
	
	private boolean play;
	private boolean isComplete;
	private float timer;
	
	private Shape shape;
	private Point point;
	
	Animator(Main game) {
		this.game = game;

		timer = 0;
		play = false;
		isComplete = false;
	}
	
	public void update() {
		if(play) {
			timer += SPEED * Gdx.graphics.getDeltaTime();
			if(animatePoint()){
				point.isAlive = false;
				shape.removeDrawablePoint(point);
				if(shape.getDrawablePoints().size() < 1) {
					Assets.chime.play();
					game.gameScreen.score.addScore(shape.worth);
					//game.gameScreen.powerUp.advanceTimeProgress(0.1f);
					game.gameScreen.score.addKill(1);
					game.gameScreen.getShapeHandler().getPool().checkIn(shape); 
				}
			}
		}
	}

	
	public void pop(Shape shape, Point point) {
		this.shape = shape;
		this.point = point;
		
		play = true;
		isComplete = false;
	}
	
	public boolean animatePoint() {
		isComplete = false;
		if(timer > DURATION) {
			isComplete = true;
		}
		
		point.setRadius(point.getRadius() + timer);
		return isComplete;
	}
	
	public boolean isPlaying() {
		return play;
	}
}
