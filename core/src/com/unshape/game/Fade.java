package com.unshape.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

//	@author Caelan Anderson
public class Fade {
	private Color bgColour = Colour.yellowA;
	
	//	Management
	private Main game;
	
	//	Triggers
	private boolean doFade = false;		//	true on call to fade 
	private boolean isComplete = false;	//	true if fadeDelta >= maxTime
	private Screen screenToChange;
	
	private float fadeDelta;       //	Incrementer to check against maxTime
	private int fadeDirection = 1; //	FadeOut = 1, FadeIn = -1
	private float maxTime = 2f;    // 	Time taken to fade (adjusts speed accordingly)
	
	Fade(Main game) {
		this.game = game;
	}
	
	public void render(float delta) {
		if(doFade == true) {
			update(delta);
			draw();
		}
	}
	
	private void update(float delta) {
		//	Increment delta in given direction based on maxTime to scale speed
		if(fadeDirection == 1)
			fadeDelta += Gdx.graphics.getDeltaTime() + 0.08/maxTime; 
		else if(fadeDirection == -1)
			fadeDelta -= Gdx.graphics.getDeltaTime() + 0.08/maxTime;
		
			//System.out.println("Do: " + doFade + ", Complete: " + isComplete + "... fadeDelta: " + fadeDelta);
		//	Check if maxTime has been reached
		if(fadeDirection == 1 && fadeDelta > 1) 
			isComplete = true;
		else if(fadeDirection == -1 && fadeDelta <= 0)
			isComplete = true;
		
		//	Execute scene jump (if required), resetting variables and triggers
		if(isComplete)
			execute();
			
	}
	
	//	To be drawn after all other objects
	private void draw() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		game.renderer.begin(ShapeType.Filled);
		game.renderer.setColor(bgColour.r, bgColour.g, bgColour.b, fadeDelta);
		game.renderer.rect(0, 0, game.getScreenSize().x, game.getScreenSize().y);
		game.renderer.end();
	}
	
	//	Inhibits fade until next call, resetting active variables and triggers
	private void reset() {
		doFade = false;
		isComplete = false;
	}
	
	//	Increase alpha to 1
	public void FadeOut(Screen screen, float maxTime) {
		if(!doFade) {
			doFade = true;
			fadeDelta = 0;
			fadeDirection = 1;
			screenToChange = screen;
			this.maxTime = maxTime;
		}
	}
	
	//	Decrease alpha to 0
	public void FadeIn(float maxTime) {
		doFade = true;
		fadeDelta = 1;
		fadeDirection = -1;
		this.maxTime = maxTime;
	}
	
	//	Called upon fade completion
	private void execute() {
		reset(); 
		
		//	For fade into different scene
		if(fadeDirection == 1) {
			game.setScreen(screenToChange);
			screenToChange.dispose();
		}
	}
}
