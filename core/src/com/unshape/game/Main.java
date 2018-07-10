package com.unshape.game;

import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.unshape.game.Screens.GameOverScreen;
import com.unshape.game.Screens.GameScreen;
import com.unshape.game.Screens.GameScreen.GameState;
import com.unshape.game.Screens.MainMenu;
import com.unshape.game.Screens.ShopScreen;
import com.unshape.game.Screens.SplashScreen;

public class Main extends Game {
	
	//	Global
	public SpriteBatch batch;
	public ShapeRenderer renderer;
	public Vector2 screenDimensions;
	public float delta;
	public OrthographicCamera camera;
	
	//	Screens
	public MainMenu mainMenu;
	public SplashScreen splash;
	public GameScreen gameScreen;
	public GameOverScreen gameOverScreen;
	public ShopScreen shopScreen;
	
	//	Extra
	public Fade fade;
	public GlyphLayout layout;
	public SmartFontGenerator fontGen;
	public Preferences prefs;
	public ScrollingBackground scrollBg;

	@Override
	public void create () {
		screenDimensions = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		prefs = Gdx.app.getPreferences("My Preferences");
		scrollBg = new ScrollingBackground(this);
		
		fontGen = new SmartFontGenerator();
		Assets.generateFonts(fontGen, screenDimensions.x, screenDimensions.y);
		Assets.loadAssets(prefs);
		
		batch 		= new SpriteBatch();
		renderer 	= new ShapeRenderer();
		
		fade 		= new Fade(this);   
		layout 		= new GlyphLayout();
		
		camera 		= new OrthographicCamera();
		camera.setToOrtho(false);
		
		//	Screens
		gameScreen 	= new GameScreen(this);
		mainMenu 	= new MainMenu(this);
		splash 		= new SplashScreen(this);
		gameOverScreen = new GameOverScreen(this);
		shopScreen		= new ShopScreen(this);

		setScreen(mainMenu);
	}	
	
	public void update() {
		delta = Gdx.graphics.getDeltaTime();
		
		camera.update();
		renderer.setProjectionMatrix(camera.combined);
		batch.setProjectionMatrix(camera.combined);
	}
	
	public void draw() {
		fade.render(delta);
	}
	
	//	Getters
	public SpriteBatch getBatch() {
		return batch;
	}
	
	public Vector2 getScreenSize() {
		return screenDimensions;
	}
}


