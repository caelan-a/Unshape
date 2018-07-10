package com.unshape.game.Screens;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.unshape.game.Assets;
import com.unshape.game.Background;
import com.unshape.game.HUD;
import com.unshape.game.Level;
import com.unshape.game.Main;
import com.unshape.game.Point;
import com.unshape.game.Shape;
import com.unshape.game.ShapeHandler;

//	@author Caelan Anderson
public class GameScreen implements Screen {
	//	Management
	private Main game;
	private InputProcessor InputHandler;
	
	//	Outer Classes
	private ShapeHandler shapeHandler;
	private Background background;
	public HUD hud;
	
	//	Inner Classes
	public Difficulty difficulty;
	public Health health;
	public Score score;
	//public PowerUp powerUp;
	//public LocalLevel level;
	
	public enum GameState {
		Running,
		Suspended
	}
	public GameState gameState;
	
	
	public GameScreen(Main game) {
		this.game 	 = game;
		
		InputHandler = new InputHandler();
		shapeHandler = new ShapeHandler(game);
		hud 		 = new HUD(game);
		background 	 = new Background(game);
		
		//	Inner Classes
		difficulty 	 = new Difficulty();
		health 		 = new Health();
		score		 = new Score();
		//powerUp		 = new PowerUp();
		//level 		 = new LocalLevel();
	}
	
	private void update() {		
		game.update();
		background.update();
		shapeHandler.update();
		difficulty.update();
		health.update();
		score.update();
		//powerUp.update();
		//level.update();
		gameState = GameState.Running;
	}

	private void draw() {
		background.draw();
		game.scrollBg.update();
		shapeHandler.draw();
		hud.draw();
		game.draw();
	}

	public void restart() {
		background.reset();
		health.reset();
		score.reset();
		difficulty.reset();
	}
	
	public void finishLevel(boolean win) {
		game.gameOverScreen.setLevelAchievement(win, score.gemsCollected);
		game.fade.FadeOut(game.gameOverScreen, 1);
	}
	
	//	Getters
	
	public Background getBackground() {
		return background;
	}
	
	public ShapeHandler getShapeHandler() {
		return shapeHandler;
	}
	
	//	Screen Methods
	
	@Override
	public void render(float delta) {
		update();
		draw();
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(InputHandler);
		getShapeHandler().getPool().resetShapes();
		Assets.ost.setLooping(true);
		if(game.prefs.getBoolean("music", false) == true)
			Assets.ost.play();
		else
			Assets.ost.stop();
		//level.setLevel();
		game.fade.FadeIn(15);
		gameState = GameState.Running;
		health.isAlive = true;
		restart();
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
	
	//	Helper Classes
	
	public class Difficulty {
		public boolean isComplete;
		private float timer;
		
		// Speed difficulty curve
		private final double MAX_VALUE = 700;
		private final double MID_POINT = 0.2;
		private final double STEEPNESS = 0.85;

		public float difficulty; // Speed difficulty
		public double x = 0; // Time
		
		private int lastLevel = 0;
		public int level = 0;
		public boolean changeLevel = false;
		
		Difficulty() {
			difficulty = 0.1f;
		}

		public void update() {
			timer += game.delta;
			//System.out.println("Difficulty" + difficulty);
			if(timer > 2) {
				timer = 0;
				x += 0.005;
				setDifficulty();
			}
			checkLevel();
		}
		
		public void checkLevel() {
			//	Determine level
			if(score.kills < 3) {
				level = 1;
				if(level != lastLevel) {
					changeLevel = true;
					lastLevel = level;
				}
			} else if(score.kills < 5) {
				level = 2;
				if(level != lastLevel) {
					changeLevel = true;
					lastLevel = level;
				}
			} else if(score.kills < 25) {
				level = 3;
				if(level != lastLevel) {
					changeLevel = true;
					lastLevel = level;
				}
			} else if(score.kills < 50) {
				level = 4;
				if(level != lastLevel) {
					changeLevel = true;
					lastLevel = level;
				}
			} else if(score.kills < 100) {
				level = 5;
				if(level != lastLevel) {
					changeLevel = true;
					lastLevel = level;
				}
			} 
			
			
			//System.out.println(level);
			
			//	Determine behaviour
			
			if(changeLevel) {
				switch (level) {
				case 1 :
					hud.levelMarker.showLevel(1);
					shapeHandler.getSpawner().calibrateColumns(1);
					shapeHandler.getSpawner().setFrequency(1.5f);
					shapeHandler.getSpawner().setMaxShapeNum(3);
					break;
				case 2 :
					shapeHandler.getSpawner().setMaxShapeNum(4);
					break;
				case 3 :
					shapeHandler.getSpawner().calibrateColumns(2);
					shapeHandler.getSpawner().setMaxShapeNum(5);
					shapeHandler.getSpawner().setFrequency(1.2f);
					break;
				case 4 :
					shapeHandler.getSpawner().calibrateColumns(3);
					shapeHandler.getSpawner().setFrequency(1.0f);
					
					break;
				case 5 :
					
					break;
				case 6 :
					hud.levelMarker.showComment(MathUtils.random(hud.levelMarker.getCommentsSize() - 1));
					break;
				}
				changeLevel = false;
			}
		}
		
		public void setDifficulty() {
			if(!isComplete)
				difficulty = (float) ((MAX_VALUE / (1 + (Math.pow(999*Math.E, -STEEPNESS * (x - MID_POINT))))) / MAX_VALUE) - 0.1f;
			if(difficulty > 0.95)
				isComplete = true;
			System.out.println(difficulty);
		}
		
		public float getDifficulty() {
			return difficulty;
		}
		
		public void setLevel(int amount) {
			level = amount;
		}
		
		public int getLevel() {
			return level;
		}
		
		public void reset() {
			difficulty = 0.1f;
			x = 0;
			level = 0;
		}
	}
	
	public class LocalLevel {
		private float DURATION = 5;
		
		public boolean isComplete = false;
		public boolean advanceProgress = false;
		
		public float shapeCount = 5;
		public float shapesKilled;
		private float timer = 0;

		private float levelProgress;

		public void update() {
			if(!isComplete) {
				if(advanceProgress) {
					timer += game.delta;
					levelProgress += 0.1f * timer;
					if(levelProgress > shapesKilled / shapeCount) {
						timer = 0;
						levelProgress = shapesKilled / shapeCount;
						advanceProgress = false;
					}
				}
				
				if(levelProgress >= 1) {
					levelProgress = 1;
					isComplete = true;
					hud.levelMarker.showFinalComment(MathUtils.random(8));
					gameState = GameState.Suspended;
				}
			}
		}
		
		public void setLevel() {
			Level.setLevel(game);
			isComplete = false;
			levelProgress = 0;
			shapesKilled = 0;
		}
		
		public float getProgress() {
			return levelProgress;
		}
		
		public void advanceProgress(int amount) {
			advanceProgress = true;
			timer = 0;
			shapesKilled += amount;
		}
	}
	
	public class Health {
		private boolean isAlive = true;
		private final int MAX_HEALTH = 7;
		private int health = MAX_HEALTH;

		public void update() {
			if(health <= 0) {
				finishLevel(false);
			}
		}
		
		public void setHealth(int amount) {
			health = amount;
		}
		
		public int getHealth() {
			return health;
		}
	
		
		public void reset() {
			health = MAX_HEALTH;
		}
	}

	public class Score {
		private int score = 0;
		private int kills = 0;
		private int gemsCollected;
		
		public void update() {
		}
		
		//	Getters and Setters
		public void addKill(int amount) {
			kills += amount;
			//level.advanceProgress(amount);
		}
		
		public void addScore(int amount) {
			score += amount;
		}
		
		public int getScore() {
			return score;
		}
	
		public int getHighscore() {
			return Assets.highscore;
		}
		
		public void setHighscore() {
			if(score > getHighscore()) {
				Assets.highscore = score;
				game.prefs.putInteger("highscore", Assets.highscore);
				game.prefs.flush();
			}
		}
		
		public void reset() {
			setHighscore();
			Assets.lastScore = score;
			score = 0;
			kills = 0;
			gemsCollected = 0;
		}
		
		public void addGem(int amount) {
			gemsCollected += amount;
		}
	}
	
	public class PowerUp {
		private boolean active = false;

		//	Original values before powerup
		private float oShapeSpeed;
		private float oBgSpeed;
		
		//	Speed of slowDown powerup
		private float speed = 1f;
		private boolean speedUp = false;
		private boolean slowDown = false;
		
		//	Progress towards powerup
		public float timeProgress = 0.9f;
		public float targetProgress = 0.9f;
		
		PowerUp() {
			oShapeSpeed = Shape.globalSpeed;
			oBgSpeed = game.scrollBg.getSpeed();
		}
		
		public void update() {
			Shape.globalSpeed = oShapeSpeed * speed;
			game.scrollBg.setSpeed(oBgSpeed * speed);
			
			slowMotion();			
			
			if(targetProgress < 1)
				targetProgress += 0.02f * game.delta;
		}
	
		public void instaKill() { 
				for(Shape s : shapeHandler.getPool().inUse) 
					for(Point p : s.getDrawablePoints()) 
						p.pop(s);
				active = false;
		}
		
		public void slowMotion() {
			if(slowDown) {
				speed -= game.delta;
				if(speed < 0.5f) {
					slowDown = false;
					speed = 0.5f;
				}
			}
			
			if(speedUp) {
				speed += game.delta;
				if(speed > 1) {
					speed = 1;
					speedUp = false;
				}
			}
			
			//	Disable upon completion
			if(timeProgress < 0) {
				active = false;
				speedUp = true;
				timeProgress = 0;
			}
			
			if(active)
				timeProgress -= 0.005;
		}
		
		public void trigger(int type) {
			active = true;
			speed = 1;
			
			oShapeSpeed = Shape.globalSpeed;
			oBgSpeed = game.scrollBg.getSpeed();
			
			switch (type) {
			case 1: 
				slowDown = true;
				break;
			case 2: 
				instaKill();
				targetProgress = 0;
				break;
			}
		}
		
		public void checkCollision(float x, float y) {
			//if(hud.comboBar.timeBounds.contains(x, y) && timeProgress >= 1) {
			//	trigger(1);
			//} else if (hud.comboBar.targetBounds.contains(x, y) && targetProgress >= 1) {
			//	trigger(2);
			//}
		}
		
		//	Getters and Setters
		public void advanceTimeProgress(float amount) {
			if(!active && timeProgress < 1)
				timeProgress += amount;
		}
	
		public float getTimeProgress() {
			return timeProgress;
		}
		
		public float getTargetProgress() {
			return targetProgress;
		}

		public void interuptSlowDown() {
			timeProgress = 0;
			speedUp = true;
		}
		public void reset() {
			targetProgress = 0;
			timeProgress = 0;
		}
		
	}
	
	public class InputHandler implements InputProcessor {
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
			shapeHandler.checkCollision(screenX, game.getScreenSize().y - screenY);
			//powerUp.checkCollision(screenX, game.getScreenSize().y - screenY);
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			// TODO Auto-generated method stub
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
