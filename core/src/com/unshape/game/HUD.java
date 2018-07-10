package com.unshape.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.unshape.game.Screens.GameScreen.GameState;



public class HUD {
	private Main game;
	
	//private GlyphLayout glyphLayout;
	public List<PopUp> popupList;
	
	//	Extras
	public LevelMarker levelMarker;
	public ComboBar comboBar;
	public LevelProgress levelProgress;
	public HealthBar healthBar;
	
	public EffectPool effectPool;
	
	public HUD(Main game) {
		this.game = game;

		levelMarker = new LevelMarker();
		levelProgress = new LevelProgress();
		healthBar = new HealthBar(0.03f * game.getScreenSize().x, 0.97f * game.getScreenSize().y);
		comboBar = new ComboBar();
		popupList = new ArrayList<PopUp>();
		effectPool = new EffectPool();
	}

	public void draw() {
		levelMarker.render();
		levelProgress.draw();
		healthBar.draw();
		
		comboBar.draw();
		
		for(int i = 0; i < popupList.size(); i ++) {
			popupList.get(i).render();
		}
		
		game.getBatch().begin();
		
		//	Score
		Assets.scoreFont.setColor(Colour.greenC);
		game.layout.setText(Assets.scoreFont, Integer.toString(game.gameScreen.score.getScore()));
		//Assets.scoreFont.draw(game.getBatch(), Integer.toString(game.gameScreen.score.getScore()), 20, game.getScreenSize().y - (game.layout.height / 3));
	
		//	Highscore
		Assets.scoreFont.setColor(Colour.greenB);
		game.layout.setText(Assets.scoreFont, Integer.toString(Assets.highscore));
		//Assets.scoreFont.draw(game.getBatch(), Integer.toString(game.gameScreen.score.getHighscore()), game.getScreenSize().x - (game.layout.width + 10), game.getScreenSize().y - (game.layout.height / 3));
		
		game.getBatch().end();

		effectPool.update();
		
		//music.draw();
	}	
	
	public void addPopUp(float x, float y, int score) {
		popupList.add(new PopUp(x, y, score));
	}
	
	public void addPopUp(float x, float y, Texture tex) {
		popupList.add(new PopUp(x, y, tex));
	}
	
	public class PopUp {
		private final float SPEED = 10f; // Ascent speed
		private final int MAX_DISPLACEMENT = 6;
		private float displacement = 0;	//	Movement from spawn along y axis
		private float displacementAlpha;
		
		private boolean active  = false;
		private Vector2 position;		//	Set on shape destruction
		private int score;
		private Texture tex;
		
		PopUp(float x, float y, int score) {
			position = new Vector2();
			show(x, y, score);
			System.out.println("Pop: x: " + x + ", y: " + y + ", worth: " + score);
		}
		
		PopUp(float x, float y, Texture tex) {
			position = new Vector2();
			show(x, y, tex);
		}
		
		public void render() {
			if(active) {
				displacementAlpha = 1.0f - (displacement / MAX_DISPLACEMENT); 
				
				//	Move
				displacement += Gdx.graphics.getDeltaTime() * SPEED;
				position.y += displacement;
				if(displacement > MAX_DISPLACEMENT) {
					reset();
				}

				//	Draw
				if(tex == null) {
				game.batch.begin();
				Assets.scoreFont.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, displacementAlpha);
				System.out.println(1.0f - (displacement / MAX_DISPLACEMENT));
				Assets.scoreFont.draw(game.batch, "+", position.x, position.y);
				game.batch.end();
				} else if(score == -1) {
					game.batch.begin();
					
					if(displacementAlpha < 0) {
						displacementAlpha = 0;
					}
					
					game.batch.setColor(1,1,1, displacementAlpha);
					System.out.println("Displacement Alpha: " + (1.0f - (displacement / MAX_DISPLACEMENT)));
					game.batch.draw(tex, position.x, position.y +12,  64, 64);
					game.batch.end();
				}
			}
		}
		
		public void show(float x, float y, int score) {
			tex = null;
			active = true;
			this.score = score;
			position.x = x;
			position.y = y;
		}
		
		public void show(float x, float y, Texture tex) {
			score = -1;
			active = true;
			this.tex = tex;
			position.x = x;
			position.y = y;
		}
		
		public void reset() {
			popupList.remove(this);
			/*
			active = false;
			position.x = 0;
			position.y = 0;
			displacement = 0;
			*/
		}
	}
	
	public class LevelMarker {
		private Color colour;
		private float alpha;
		private float SLIDE_SPEED = game.getScreenSize().y / 1.2f;
		
		private boolean active;
		private boolean finalBanner;
		
		private float yPosition;
		private float bandHeight = 0.2f * game.getScreenSize().y;
		private float range = game.getScreenSize().y / 3; //	slow center pass range

		private String[] comments = {"Nice", "Fantastic", "Superb", "Impressive", "Cool", "Amazing", "Perfect", "Exquisite", "Excellent" };
		private String markerText = "";
		
		public LevelMarker() {
			yPosition = 1.2f * game.getScreenSize().y;
		}
		
		public void render() {
			if(active && yPosition < -bandHeight) {
				active = false;
			}
			
			if(active) {

				if(markerText == "Level 1")
					SLIDE_SPEED = game.getScreenSize().y / 3f;
				else
					SLIDE_SPEED = game.getScreenSize().y / 2f;
				
				Gdx.gl.glEnable(GL20.GL_BLEND);
				Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
				
				yPosition -= SLIDE_SPEED * game.delta + (10f * Math.abs(((game.getScreenSize().y / 2f) - yPosition) / range));

				//	Band
				game.renderer.begin(ShapeType.Filled);
				game.renderer.setColor(colour.r, colour.g, colour.b, alpha);
				game.renderer.rect(0, yPosition - (bandHeight / 2), game.getScreenSize().x, bandHeight);
				game.renderer.end();

				//	Text
				game.batch.begin();
				Assets.scoreFont.setColor(Color.WHITE);
				game.layout.setText(Assets.scoreFont, markerText);
				Assets.scoreFont.draw(game.getBatch(), markerText, game.getScreenSize().x * 0.5f - (game.layout.width / 2), yPosition + (game.layout.height / 2));
				game.batch.end();
				
				if(finalBanner) {
					if(yPosition < 0) {
						finalBanner = false;
						game.gameScreen.finishLevel(true);
					}
				}
			}
		}
		
		public void showLevel(int level) {
			active = true;
			yPosition = 1.2f * game.getScreenSize().y;
			markerText = "Level " + level;
			colour = Colour.greenB;
			alpha = 0.8f;
		}
		
		public void showComment(int index) {
			active = true;
			yPosition = 1.2f * game.getScreenSize().y;
			markerText = comments[index];
			colour = Colour.greenB;
			alpha = 0.8f;
		}
		
		public void showFinalComment(int index) {
			active = true;
			finalBanner = true;
			yPosition = 1.2f * game.getScreenSize().y;
			markerText = comments[index];
			colour = Colour.greenB;
			alpha = 0.8f;
		}

		public int getCommentsSize() {
			return comments.length;
		}
	}
	
	public class LevelProgress {
		private Color colour = Colour.fAmethyst;
		private Rectangle bounds;
		float progress;
		
		LevelProgress() {
			bounds = new Rectangle(0, 0.95f * game.getScreenSize().y, game.getScreenSize().x, 0.05f * game.getScreenSize().y );
		}
		
		public void draw() {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			
			progress = game.gameScreen.level.getProgress();
			System.out.println(progress);
			game.renderer.begin(ShapeType.Filled);
			game.renderer.setColor(colour.r, colour.g, colour.b, 0.85f);
			game.renderer.rect(bounds.x - 100, bounds.y, progress * bounds.width + 100, bounds.height);
			game.renderer.end();
		}
	}
	
	public class HealthBar {
		private float x;
		private float y;
		
		private Color colour = Colour.fRed;
		private Color bgColour = Colour.fAmethyst;
		private int healthCount;
		
		HealthBar(float x, float y) {
			this.x = x;
			this.y = y;
		}
		
		public void draw() {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			
			healthCount = game.gameScreen.health.getHealth();
			
			game.renderer.begin(ShapeType.Filled);
		//	game.renderer.setColor(bgColour.r, bgColour.g, bgColour.b, 0.8f);
			//game.renderer.circle(x, y, 50);
			game.renderer.end();
			
			game.batch.begin();
			game.batch.setColor(colour.r, colour.g, colour.b, 1f);
			game.batch.draw(Assets.heartIcon, 0.01f * game.getScreenSize().x, 0.01f * game.getScreenSize().y, game.getScreenSize().y / 8.9f, game.getScreenSize().y / 8.9f);
			
			game.layout.setText(Assets.optionFont, Integer.toString(game.gameScreen.health.getHealth()));
			Assets.optionFont.draw(game.batch, Integer.toString(game.gameScreen.health.getHealth()),  0.09f * game.getScreenSize().x - (game.layout.width / 2), 0.12f * game.getScreenSize().y - (game.layout.height / 2));
			game.batch.end();
		}
	}
	
	public class EffectPool {
		private final int MAX_ELEMENTS = 10; // Pool size
		public List<ParticleEffect> available;		
		public List<ParticleEffect> inUse;			 
		
		EffectPool () {
			available = new ArrayList<ParticleEffect>();
			inUse = new ArrayList<ParticleEffect>();
			
			for(int i = 0; i < MAX_ELEMENTS; i++) // Initialise available list
				available.add(createEffect());
		}
		
		public void update() {
			game.batch.begin();
			for(ParticleEffect pE : inUse) {
				pE.draw(game.batch, game.delta);
			}
			game.batch.end();
			
			for(int i = 0; i < inUse.size(); i++) {
				if(inUse.get(i).isComplete()) {
					checkIn(inUse.get(i));
				}
			}
			
		}
		
		//	Admin methods
		private ParticleEffect objToTransfer;
		public ParticleEffect checkOut(float x, float y) {
			if(available.size() < 1) {// If no pool.inUse are available extend pool
				available.add(createEffect());
			}
			objToTransfer = available.get(available.size()-1); // Get last shape
			objToTransfer.setPosition(x, y);
			objToTransfer.start();
			inUse.add(objToTransfer);
			available.remove(objToTransfer);
			return objToTransfer;
		}
		
		public void checkIn(ParticleEffect obj) {
			available.add(obj);
			inUse.remove(obj);		
		}

		private ParticleEffect objToAdd;
		public void addShape(Color colour, float x, float y, List<Point> points) {
			objToAdd = checkOut(x, y);
		}
		
		public void resetShapes() {
			for(int i = 0; i < inUse.size(); i++)
				checkIn(inUse.get(i));
		}
		
		public ParticleEffect createEffect() {
			ParticleEffect effect = new ParticleEffect();
			effect.load(Gdx.files.internal("fx/ShapeExplosion2.gfx"), Gdx.files.internal("fx"));
			effect.allowCompletion();
			effect.scaleEffect(1f);
			return effect;
		}
	}
	
	public class ComboBar {
		public Rectangle bounds;
		
		private float timeProgress;
		private float targetProgress;
		
		public Circle timeBounds;
		public Circle targetBounds;
		
		private float timeAlpha = 0;
		private float targetAlpha = 0;
		
		private int timeAlphaDirection = 1;
		private int targetAlphaDirection = 1;
		
		public ComboBar() {
			timeBounds = new Circle();
			timeBounds.radius = game.getScreenSize().x / 15;
			timeBounds.setPosition(0.90f * game.getScreenSize().x, 0.1f * game.getScreenSize().y);
			
			targetBounds = new Circle();
			targetBounds.radius = game.getScreenSize().x / 15;
			targetBounds.setPosition(0.70f * game.getScreenSize().x, 0.1f * game.getScreenSize().y);
		}
		
		public void draw() {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			
			timeProgress = game.gameScreen.powerUp.timeProgress;
			targetProgress = game.gameScreen.powerUp.targetProgress;
			
			if(timeProgress >= 1) {
				System.out.println(timeAlpha);
				timeAlpha += 5 * timeAlphaDirection * game.delta;
				
				if(timeAlpha < 0)
					timeAlphaDirection = 1;
				else if(timeAlpha > 1)
					timeAlphaDirection = -1;
			} else {
				timeAlpha = 1;
			}
			
			if(targetProgress >= 1) {
				targetAlpha += 5 * targetAlphaDirection * game.delta;
				
				if(targetAlpha < 0)
					targetAlphaDirection = 1;
				else if(targetAlpha > 1)
					targetAlphaDirection = -1;
			} else {
				targetAlpha = 1;
			}
			
			game.renderer.begin(ShapeType.Filled);
			game.renderer.setColor(Colour.fAmethyst.r, Colour.fAmethyst.g, Colour.fAmethyst.b, 1);
			game.renderer.arc(timeBounds.x, timeBounds.y,  1.3f * timeBounds.radius, 90, timeProgress * 360);
			game.renderer.setColor(Colour.fAmethyst.r, Colour.fAmethyst.g, Colour.fAmethyst.b, 1);
			game.renderer.arc(targetBounds.x, targetBounds.y,  1.3f * targetBounds.radius, 90, targetProgress * 360);
			game.renderer.end();
			
			game.batch.begin();
			if(timeProgress < 1)
				game.batch.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, 0.6f);
			else
				game.batch.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, 1f);
			game.batch.draw(Assets.time, timeBounds.x - timeBounds.radius, timeBounds.y - timeBounds.radius, 2 * timeBounds.radius, 2 * timeBounds.radius);
			if(targetProgress < 1)
				game.batch.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, 0.6f);
			else
				game.batch.setColor(Color.WHITE.r, Color.WHITE.g, Color.WHITE.b, 1f);
			game.batch.draw(Assets.killAllIcon, targetBounds.x - targetBounds.radius, targetBounds.y - targetBounds.radius, 2 * targetBounds.radius, 2 * targetBounds.radius);
			game.batch.end();
		}
	}
}
