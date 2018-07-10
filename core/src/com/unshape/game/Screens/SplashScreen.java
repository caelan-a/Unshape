package com.unshape.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.MathUtils;
import com.unshape.game.Assets;
import com.unshape.game.Colour;
import com.unshape.game.Main;

public class SplashScreen implements Screen{
	private Color bgColour = Colour.yellowA;
	private Main game;
	private float timer; // For transition
	private BitmapFont font;
	
	public SplashScreen(Main game) {
		this.game = game;
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		game.update();
		timer += game.delta;
		
		Gdx.gl.glClearColor(bgColour.r, bgColour.g, bgColour.b, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.begin();
		game.layout.setText(Assets.scoreFont, "Created");
		Assets.scoreFont.draw(game.getBatch(), "Created", game.getScreenSize().x * 0.5f - (game.layout.width / 2), game.getScreenSize().y * 0.5f + (game.layout.height / 2 + 75));
		game.layout.setText(Assets.scoreFont, "By");
		Assets.scoreFont.draw(game.getBatch(), "By", game.getScreenSize().x * 0.5f - (game.layout.width / 2), game.getScreenSize().y * 0.5f + (game.layout.height / 2));
		game.layout.setText(Assets.scoreFont, "Caelan");
		Assets.scoreFont.draw(game.getBatch(), "Caelan", game.getScreenSize().x * 0.5f - (game.layout.width / 2), game.getScreenSize().y * 0.5f + (game.layout.height / 2 - 75));
		game.batch.end();
		
		if(timer > 0.5)
			game.fade.FadeOut(game.mainMenu, 3);
		
		game.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
