package com.unshape.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public class Background {
	private Color bgColour = Colour.yellowA;
	
	//	Main
	Main game;
	
	//	Triggers
	private boolean flash = false;
	private Color flashColour = Colour.fOrange;
	private float flashAlpha = 0f;
	
	public Background(Main game) {
		this.game = game;
	}
	
	public void update() {
		
	}
	
	public void draw() {
		Gdx.gl.glClearColor(bgColour.r, bgColour.g, bgColour.b, bgColour.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		if(flash)
			interpolateAlpha(flashColour);
		else {
			reset();
		}
		
		//	FlashBox
		game.renderer.begin(ShapeType.Filled);
		game.renderer.setColor(flashColour.r, flashColour.g, flashColour.b, flashAlpha);
		game.renderer.rect(-100, 0, game.getScreenSize().x + 200, game.getScreenSize().y);
		game.renderer.end();
	}
	
	public void flash(Color colour) {
		//System.out.println("Flash!");
		flash = true;
		flashColour = colour;
	}
	
	private float timer;
	private float flashSpeed = 8;
	private Color interpolateAlpha(Color colour) {
		flashAlpha += flashSpeed * Gdx.graphics.getDeltaTime();
		timer += Gdx.graphics.getDeltaTime();
		game.camera.position.x = 0.5f * (game.getScreenSize().x / 2) + 5 * MathUtils.sin(timer + 10);
		
		if(flashAlpha > 2f) 
			flashSpeed *= -1;
		
		if(flashAlpha < 0 ) {
			reset();
		}
		
		return colour;
	}
	
	public void reset() {
		flash = false;
		game.camera.setToOrtho(false);
		timer = 0;
		flashSpeed = Math.abs(flashSpeed);
		flashAlpha = 0;
	}
}
