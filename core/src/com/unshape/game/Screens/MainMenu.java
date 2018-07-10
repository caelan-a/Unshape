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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.unshape.game.Assets;
import com.unshape.game.ButtonSet;
import com.unshape.game.ButtonSet.Button;
import com.unshape.game.Colour;
import com.unshape.game.Main;
import com.unshape.game.ScrollingBackground;

//	@author Caelan Anderson
public class MainMenu implements Screen{
	private Color bgColour = Colour.yellowA;
	
	//	Management
	private Main game;
	private InputProcessor InputHandler;

	private float timer;
	
	private boolean slide = false;
	private int direction = 1;
	private float globalY = 0; //	For sliding
	
	private ButtonSet buttonSet;
	private ButtonSet optionSet;
	private ButtonSet buttonSetThree;
	
	private SubScreen currentSubScreen = SubScreen.main;
	enum SubScreen {
		main,
		options,
	}
	
	public MainMenu(Main game) {
		this.game = game;
			
		buttonSet = new ButtonSet(game, 0, 0.9f * game.getScreenSize().y, game.getScreenSize().x, 0.1f * game.getScreenSize().y, 0, true);
		buttonSet.addButton(buttonSet.new Button(Assets.trophyIcon, game.getScreenSize().y / 20, Colour.greenB));
		buttonSet.addButton(buttonSet.new Button(Assets.shareIcon, game.getScreenSize().y / 20, Colour.greenB));
		buttonSet.addButton(buttonSet.new Button(Assets.starIcon, game.getScreenSize().y / 20, Colour.greenB) { public void execute() {Gdx.net.openURI("https://play.google.com/store/"); }} );
		buttonSet.addButton(buttonSet.new Button(Assets.settingsIcon, game.getScreenSize().y / 20, Colour.greenB) { public void execute() { if(currentSubScreen != SubScreen.options) { currentSubScreen = SubScreen.options; buttonSet.getButtons().get(3).downTab = true; } else {  currentSubScreen = SubScreen.main; buttonSet.getButtons().get(3).downTab = false;}}});
		
		optionSet = new ButtonSet(game, 0.1f * game.getScreenSize().x, 0.5f * game.getScreenSize().y, 0.8f * game.getScreenSize().x, 0.1f * game.getScreenSize().y, 10, false);
		optionSet.addButton(optionSet.new Button("Reset Progress ( Level " + Assets.level + " )", Colour.greenB) { public void execute() { Assets.setLevel(1); optionSet.getButtons().get(0).label = "Reset Progress ( Level " + Assets.level + " )";} });
		optionSet.addButton(optionSet.new Button("Choose Level", Colour.greenB) { public void execute() { } });
		optionSet.addButton(optionSet.new Button("Reset Upgrades", Colour.greenB) { public void execute() { } });
		optionSet.addButton(optionSet.new Button("Music ( " + game.prefs.getBoolean("music", false) + " )", Colour.greenB) { public void execute() { Assets.setMusic(); optionSet.getButtons().get(3).label =  "Music ( " + Assets.getMusic() + " )"; } });
		
		buttonSetThree = new ButtonSet(game, 0, 0.1f * game.getScreenSize().y, game.getScreenSize().x, 0.1f * game.getScreenSize().y, 0, true);
		buttonSetThree.addButton(buttonSetThree.new Button(Assets.playIcon, game.getScreenSize().y / 20, Colour.greenB) {public void execute() { slide = true; }});
		
		InputHandler = new InputHandler();
	}

	public void update() {
		game.update();
		slide();
	}
	
	private float yDelta;
	private float playAlpha;
	public void draw() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		Gdx.gl.glClearColor(bgColour.r, bgColour.g, bgColour.b, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.scrollBg.update();
		buttonSet.render();
		buttonSetThree.render();
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		if(currentSubScreen == SubScreen.options) {
			optionSet.render();
		}
		
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		game.renderer.begin(ShapeType.Filled);

		if(currentSubScreen == SubScreen.main) {
			//	Title band
			game.renderer.setColor(Colour.greenB.r, Colour.greenB.g, Colour.greenB.b, 0.8f);
			game.layout.setText(Assets.titleFont, "Unshape");
			game.renderer.rect(0, game.getScreenSize().y * 0.45f + globalY, game.getScreenSize().x, game.getScreenSize().y / 3.4f);
		} else if(currentSubScreen == SubScreen.options) {
			game.renderer.setColor(Colour.greenB.r, Colour.greenB.g, Colour.greenB.b, 0.8f);
			game.layout.setText(Assets.titleFont, "Options");
			game.renderer.rect(0, game.getScreenSize().y * 0.63f + globalY, game.getScreenSize().x, 0.2f * game.getScreenSize().y);
		}

		game.renderer.end();
		
		game.batch.begin();

		if(currentSubScreen == SubScreen.main) {
			game.layout.setText(Assets.titleFont, "Unshape");
			Assets.titleFont.draw(game.getBatch(), "Unshape", game.getScreenSize().x * 0.5f - (game.layout.width / 2), game.getScreenSize().y * 0.53f + (game.layout.height / 2 + (game.getScreenSize().y / 9.14f)) + globalY);

			game.layout.setText(Assets.subheadingFont, "Level");
			Assets.subheadingFont.draw(game.getBatch(), "Level", game.getScreenSize().x * 0.5f - (game.layout.width / 2), game.getScreenSize().y * 0.55f + (game.layout.height / 2 - (game.getScreenSize().y / 21.3f)) + globalY);

			Assets.highscoreFont.setColor(Colour.fTurqoise);
			game.layout.setText(Assets.highscoreFont, Integer.toString(game.prefs.getInteger("level")));
			Assets.highscoreFont.draw(game.getBatch(), Integer.toString(game.prefs.getInteger("level")),  game.getScreenSize().x * 0.5f - (game.layout.width / 2), (0.4f * game.getScreenSize().y) + globalY);
		} else if(currentSubScreen == SubScreen.options) {
			game.layout.setText(Assets.titleFont, "Options");
			Assets.titleFont.draw(game.getBatch(), "Options", game.getScreenSize().x * 0.5f - (game.layout.width / 2), game.getScreenSize().y * 0.63f + (game.layout.height / 2 + (game.getScreenSize().y / 9.14f)) + globalY);

		}
		game.batch.end();
	}

	private int dir = 1;
	private void lerpAlpha() {
		playAlpha += dir * 3.5f * Gdx.graphics.getDeltaTime();
	
		if(playAlpha > 1f)
			dir *= -1;
		else if(playAlpha < 0)
			dir = 1;
	}
	
	
	//	For sliding
	private void slide() {
		if(!slide)
			globalY = 0;
		if(slide) {
			//System.out.println("GlobalY: " + globalY);
			game.fade.FadeOut(game.gameScreen, 1);
			//globalY += direction * Gdx.graphics.getDeltaTime() * 1000;
			if(globalY <= -800) {
				slide = false;
				game.setScreen(game.gameScreen);
			}  else if (globalY > 800) {
				slide = false;
				game.setScreen(game.gameScreen);
			}
		}
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(InputHandler);
		currentSubScreen = SubScreen.main;
		game.fade.FadeIn(10);
		slide = false;
	}

	@Override
	public void render(float delta) {
		update();
		draw();
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
			buttonSetThree.checkSelection(screenX, downY, false);
			
			if(currentSubScreen == SubScreen.options) {
				optionSet.checkSelection(screenX, downY, false);
			}
			
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			upY = game.getScreenSize().y - screenY;
			  
			buttonSet.resetButtons();
			buttonSetThree.resetButtons();
			buttonSet.checkSelection(screenX, upY, true);
			buttonSetThree.checkSelection(screenX, downY, true);
			
			if(currentSubScreen == SubScreen.options) {
				optionSet.resetButtons();
				optionSet.checkSelection(screenX, upY, true);
			}
			
			direction = -1;
			isDragged = false;
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
