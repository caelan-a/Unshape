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
import com.unshape.game.ButtonSet.Button;

//	@author Caelan Anderson
public class ShopScreen implements Screen{
	private Color bgColour = Colour.yellowA;

	//	Management
	private Main game;
	private InputProcessor InputHandler;

	private ButtonSet buttonSet;
	private ButtonSet gemBuyButtonSet;
	
	private UpgradeManager upgradeManager;
	
	public boolean justWon = false;
	public Screen previousScreen;

	private float timer;


	public ShopScreen(final Main game) {
		this.game = game;
		InputHandler = new InputHandler();
		
		upgradeManager = new UpgradeManager(0.2f * game.getScreenSize().x, 0.6f * game.getScreenSize().y, 0.6f * game.getScreenSize().x, 0.1f * game.getScreenSize().y, 10);
		upgradeManager.addUpgrade(upgradeManager.new Upgrade("Time Warp", Assets.timeIcon, Colour.greenB, "slowTime"));
		upgradeManager.addUpgrade(upgradeManager.new Upgrade("Shape-agon", Assets.killAllIcon, Colour.greenB, "killAll"));
		upgradeManager.addUpgrade(upgradeManager.new Upgrade("Health", Assets.heartIcon, Colour.greenB, "health"));
		upgradeManager.addUpgrade(upgradeManager.new Upgrade("Luck", Assets.starIcon, Colour.greenB, "luck"));
		upgradeManager.addUpgrade(upgradeManager.new Upgrade("Dampener", Assets.starIcon, Colour.greenB, "damper"));

		
		buttonSet = new ButtonSet(game, 0, 0.82f * game.getScreenSize().y, 0.15f * game.getScreenSize().x, 0.15f * game.getScreenSize().y, 0, false);
		buttonSet.addButton(buttonSet.new Button(Assets.backIcon, game.getScreenSize().x / 20, Colour.greenB){ public void execute() { if(previousScreen == game.gameOverScreen) { game.gameScreen.finishLevel(justWon);} } });

		gemBuyButtonSet = new ButtonSet(game, 0.85f * game.getScreenSize().x, 0.82f * game.getScreenSize().y, 0.15f * game.getScreenSize().x, 0.15f * game.getScreenSize().y, 0, false);
		gemBuyButtonSet.addButton(gemBuyButtonSet.new Button(Assets.buyGemIcon, game.getScreenSize().x / 10, Colour.greenB){ public void execute() {  } });

		
	}

	public void advanceUpgrade(String upgrade) {
		System.out.println("Current level of " + upgrade + game.prefs.getInteger(upgrade));
		game.prefs.putInteger(upgrade, 0);
		game.prefs.flush();
		System.out.println(upgrade + " now leveled to " +game.prefs.getInteger(upgrade));
	}

	public void update() {
		game.update();
	}

	public void reset() {

	}

	public void draw() {
		Gdx.gl.glClearColor(bgColour.r, bgColour.g, bgColour.b, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.scrollBg.update();

		game.renderer.begin(ShapeType.Filled);
		game.renderer.setColor(Colour.greenB.r, Colour.greenB.g, Colour.greenB.b, 0.8f);
		game.renderer.rect(0.15f * game.getScreenSize().x, 0.82f * game.getScreenSize().y, 0.7f * game.getScreenSize().x, 0.15f * game.getScreenSize().y);
		game.renderer.end();

		game.batch.begin();

		Assets.scoreFont.setColor(Color.WHITE);
		game.layout.setText(Assets.scoreFont, "Upgrades");
		Assets.scoreFont.draw(game.getBatch(), "Upgrades", game.getScreenSize().x * 0.5f - (game.layout.width / 2), game.getScreenSize().y * 0.6f + (game.layout.height / 2 + ( 0.3f * game.getScreenSize().y)));

		game.batch.end();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(InputHandler);
		game.fade.FadeIn(10);

	}

	@Override
	public void render(float delta) {
		update();
		draw();
		upgradeManager.render();
		buttonSet.render();
		gemBuyButtonSet.render();
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
			gemBuyButtonSet.checkSelection(screenX, downY, false);
			upgradeManager.checkSelection(screenX, downY, false);

			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			upY = game.getScreenSize().y - screenY;
			upgradeManager.checkSelection(screenX, upY, true);
			upgradeManager.resetButtons();
			buttonSet.checkSelection(screenX, upY, true);
			buttonSet.resetButtons();
			gemBuyButtonSet.checkSelection(screenX, upY, true);
			gemBuyButtonSet.resetButtons();
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

	class UpgradeManager {
		public List<Upgrade> upgrades;
		private Rectangle boundary;
		private float padding;

		public UpgradeManager(float x, float y, float width, float height, float padding) {
			upgrades = new ArrayList<Upgrade>();
			boundary = new Rectangle();
			boundary.x = x;
			boundary.y = y;
			boundary.width = width;
			boundary.height = height;

			this.padding = padding;
		}


		public void render() {
			for(Upgrade u : upgrades)
				u.draw();
		}	


		public void checkSelection(float touchX, float touchY, boolean isSelected) {
			for(Upgrade u : upgrades)
				if(u.getBounds().contains(touchX, touchY)) 
					if(isSelected)
						u.execute();
					else
						u.isPressed = true;
		}

		public void addUpgrade(Upgrade upgrade) {
			upgrades.add(upgrade);

			for(int i = 0; i < upgrades.size(); i++) {
				upgrades.get(i).getBounds().y = boundary.y - (i * (boundary.height + padding));
			}
		}

		public void resetButtons() {
			for(Upgrade u : upgrades)
				u.isPressed = false;
		}

		public Rectangle getBounds() {
			return boundary;
		}

		public class Upgrade {
			private Rectangle bounds;
			private Rectangle upgradeBounds;
			private Color bgColour;
			public String label = "";
			private float texSize = 48;
			private Texture tex;
			private String upgradePath;
			public boolean isPressed;
			private int cost = 10;

			public Upgrade(String label, Texture tex, Color bgColour, String upgradePath) {
				this.label = label;
				this.bgColour = bgColour;
				this.tex = tex;
				this.upgradePath = upgradePath;
				texSize = game.getScreenSize().y / 13.3f;
				bounds = new Rectangle();
				bounds.height = boundary.height;
				bounds.width = boundary.width;				
				bounds.x = boundary.x;		
				bounds.y = boundary.y - (upgrades.size() * (bounds.height + padding));
				
				upgradeBounds = new Rectangle();
				upgradeBounds.height = boundary.height;
				upgradeBounds.width = 0.12f * game.getScreenSize().x;				
				upgradeBounds.x = 0.88f * game.getScreenSize().x;		
				upgradeBounds.y = boundary.y - (upgrades.size() * (bounds.height + padding));
				
			}

			public void draw() {
				Gdx.gl.glEnable(GL20.GL_BLEND);
				Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

				//	Background
				game.renderer.begin(ShapeType.Filled);
				game.renderer.setColor(bgColour.r, bgColour.g, bgColour.b, 0.88f);
				game.renderer.rect(0, bounds.y,0.88f * game.getScreenSize().x, bounds.height);
				if(!isPressed)
					game.renderer.setColor(bgColour.r, bgColour.g, bgColour.b, 0.8f);
				else
					game.renderer.setColor(bgColour.r, bgColour.g, bgColour.b, 1f);
				game.renderer.rect(upgradeBounds.x, bounds.y,upgradeBounds.width, bounds.height);
				game.renderer.end();

				//	Text
				game.batch.begin();
				//	Label
				game.layout.setText(Assets.subheadingFont, label);
				Assets.subheadingFont.draw(game.batch, label, 0.29f * game.getScreenSize().x, bounds.y + (bounds.height / 1.5f));
				
				//	Current upgrade level
				game.layout.setText(Assets.optionFont, Integer.toString(game.prefs.getInteger(upgradePath)));
				Assets.optionFont.draw(game.batch, Integer.toString(game.prefs.getInteger(upgradePath)), 0.05f * game.getScreenSize().x, bounds.y + (bounds.height / 1.3f));
				
				//	Upgrade type icon
				game.batch.setColor(Color.WHITE);
				game.batch.draw(tex, 0.15f * game.getScreenSize().x, (bounds.y + (bounds.height / 2)) - (texSize / 2), texSize, texSize);

				//	Numerical cost 
				Assets.optionFont.draw(game.batch, Integer.toString(cost), 0.7f * game.getScreenSize().x, bounds.y + (bounds.height / 1.3f));
				game.batch.draw(Assets.gemIcon, 0.8f * game.getScreenSize().x, (bounds.y + (bounds.height / 2)) - (game.getScreenSize().y / 20 / 2), game.getScreenSize().y / 20, game.getScreenSize().y / 20);

				game.batch.setColor(Color.WHITE);
				game.batch.draw(Assets.levelUpIcon, 0.9f * game.getScreenSize().x, (bounds.y + (bounds.height / 2)) - (game.getScreenSize().y / 20 / 2), game.getScreenSize().y / 20, game.getScreenSize().y / 20);

				//	Icon
				game.batch.end();
			}

			public void execute() {
				System.out.println(label + " was pressed! ");
				advanceUpgrade(upgradePath);

			}

			public Rectangle getBounds() {
				return upgradeBounds;
			}
		}
	}
}
