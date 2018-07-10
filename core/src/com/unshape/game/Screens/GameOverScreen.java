package com.unshape.game.Screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.unshape.game.Assets;
import com.unshape.game.ButtonSet;
import com.unshape.game.Colour;
import com.unshape.game.Main;

//	@author Caelan Anderson
public class GameOverScreen implements Screen{
	private Color bgColour = Colour.yellowA;
	
	//	Management
	private Main game;
	private InputProcessor InputHandler;
	
	private ButtonSet buttonSet;
	
	private float timer;
	private Sprite highscoreBg;
	
	private boolean levelPassed = false;
	private int level;
	private int gemsCollected;
	private int totalGems;
	
	private boolean holdAnimation = false;
	
	public GameOverScreen(final Main game) {
		this.game = game;
		InputHandler = new InputHandler();

		buttonSet = new ButtonSet(game, 0.2f * game.getScreenSize().x, 0.3f * game.getScreenSize().y, 0.6f * game.getScreenSize().x, 0.1f * game.getScreenSize().y, 10, false);
		buttonSet.addButton(buttonSet.new Button("Next Level", Colour.greenB){ public void execute() { game.fade.FadeOut(game.gameScreen, 1);} });
		buttonSet.addButton(buttonSet.new Button("Upgrades", Colour.greenB){ public void execute() { goToUpgrade();} });
		buttonSet.addButton(buttonSet.new Button("Menu", Colour.greenB) { public void execute() { game.fade.FadeOut(game.mainMenu, 1);} });
		
		
		highscoreBg = new Sprite(Assets.highscoreBg);
		highscoreBg.setSize(1.5f * game.getScreenSize().y, 1.5f * game.getScreenSize().y);
		highscoreBg.setOriginCenter();
		highscoreBg.setPosition(-(highscoreBg.getWidth() / 2f) + (game.getScreenSize().x / 2f),-(highscoreBg.getHeight() / 2f) + (game.getScreenSize().y / 2f));
	
		pDelta = game.getScreenSize().x;
		lDelta = game.getScreenSize().x;
		
		LABEL_SPEED = game.getScreenSize().x * 3.5f;
	}

	public void update() {
		game.update();
		highscoreBg.rotate(1);
		
		if(pSlide) {
			pDelta -= LABEL_SPEED * game.delta;
			if(pDelta < 0) {
				pDelta = 0;
				pSlide = false;
			}
		}
		
		if(lSlide) {
			lDelta -= LABEL_SPEED * game.delta;
			if(lDelta < 0) {
				lDelta = 0;
				lSlide = false;
				pSlide = true;
			}
		}
	}
	
	public void goToUpgrade() {
		game.fade.FadeOut(game.shopScreen, 1);
		game.shopScreen.justWon = levelPassed;
		game.shopScreen.previousScreen = this;
		holdAnimation = true;
	}
	
	public void setLevelAchievement(boolean win, int gemsCollected) {
		this.levelPassed = win;
		this.gemsCollected = gemsCollected;
	}
	
	public void reset() {
		pSlide = false;
		lSlide = false;
		pDelta = game.getScreenSize().x;
		lDelta = game.getScreenSize().x;
	}
	
	private final float LABEL_SPEED;
	private boolean pSlide;
	private float pDelta; //	Delta for 'Passed' text
	private boolean lSlide;
	private float lDelta;
	public void draw() {
		Gdx.gl.glClearColor(bgColour.r, bgColour.g, bgColour.b, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.scrollBg.update();
		
		game.renderer.begin(ShapeType.Filled);
		game.renderer.setColor(Colour.greenB.r, Colour.greenB.g, Colour.greenB.b, 0.8f);
		game.renderer.rect(0, 0.85f * game.getScreenSize().y, game.getScreenSize().x, 0.1f * game.getScreenSize().y);
		game.renderer.rect(0, 0.57f * game.getScreenSize().y, game.getScreenSize().x, 0.25f * game.getScreenSize().y);
		game.renderer.rect(0, 0.45f * game.getScreenSize().y, game.getScreenSize().x, 0.1f * game.getScreenSize().y);
		game.renderer.end();
		
		game.batch.begin();
		if(levelPassed) {
			Assets.titleFont.setColor(Color.WHITE);
			game.layout.setText(Assets.titleFont, "Passed");
			Assets.titleFont.draw(game.getBatch(), "Passed", game.getScreenSize().x * 0.5f - (game.layout.width / 2) + pDelta, game.getScreenSize().y * 0.6f + (game.layout.height / 2 + (game.getScreenSize().y / 9.14f)));
		} else if(!levelPassed){
			Assets.titleFont.setColor(Colour.fRed);
			game.layout.setText(Assets.titleFont, "Failed..");
			Assets.titleFont.draw(game.getBatch(), "Failed..", game.getScreenSize().x * 0.5f - (game.layout.width / 2) + pDelta, game.getScreenSize().y * 0.6f + (game.layout.height / 2 + (game.getScreenSize().y / 9.14f)));
			Assets.titleFont.setColor(Color.WHITE);
		}
		Assets.optionFont.setColor(Color.WHITE);
		game.layout.setText(Assets.optionFont, "Level " + level);
		Assets.optionFont.draw(game.batch, "Level " + level, 0.5f * game.getScreenSize().x - (game.layout.width / 2) - lDelta, 0.923f * game.getScreenSize().y);
		
		game.batch.setColor(Color.WHITE);
		game.batch.draw(Assets.gemIcon, 0.5f * game.getScreenSize().x - 32, 0.46f * game.getScreenSize().y, game.getScreenSize().x / 10, game.getScreenSize().x / 10);
		game.batch.end();
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(InputHandler);
		game.fade.FadeIn(10);
		level = game.prefs.getInteger("level");
		reset();
		if(holdAnimation) {
			holdAnimation = false;
			lDelta = 0;
			pDelta = 0;
		} else {
			lSlide = true;
		}
		if(levelPassed)
			buttonSet.getButtons().get(0).label = "Next Level";
		else if(!levelPassed)
			buttonSet.getButtons().get(0).label = "Retry";
	}

	@Override
	public void render(float delta) {
		update();
		draw();
		buttonSet.render();
		game.draw();
	}
	
	//	Interface methods
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
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	//	Class-specific input
	public class InputHandler implements InputProcessor {
		private float downY; //	on touch down
		private float upY;	// on touch up
		private boolean isDragged;
		
		@Override
		public boolean keyDown(int keycode) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			downY = game.getScreenSize().y - screenY;
			buttonSet.checkSelection(screenX, downY, false);
			
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			upY = game.getScreenSize().y - screenY;
			buttonSet.checkSelection(screenX, upY, true);
			buttonSet.resetButtons();
			
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			isDragged = true;
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean scrolled(int amount) {
			// TODO Auto-generated method stub
			return false;
		}
	}
}