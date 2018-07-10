package com.unshape.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;

public class ButtonSet {
	private Main game;
	
	private List<Button> buttons;
	
	private Rectangle boundary;
	private float padding;
	
	private boolean isHorizontal = true;
	
	//	List Buttons
	public ButtonSet(Main game, float x, float y, float width, float height, float padding, boolean isHorizontal) {
		this.game = game;
		
		buttons = new ArrayList<Button>();
		
		boundary = new Rectangle();
		boundary.x = x;
		boundary.y = y;
		boundary.width = width;
		boundary.height = height;
		
		this.padding = padding;
		
		this.isHorizontal = isHorizontal;
	}

	
	public void render() {
		for(Button b : buttons)
			b.render();
	}	
	
	
	public void checkSelection(float touchX, float touchY, boolean isSelected) {
		for(Button b : buttons)
			if(b.getBounds().contains(touchX, touchY)) 
				if(isSelected)
					b.execute();
				else
					b.isPressed = true;
	}
	
	public void addButton(Button button) {
		buttons.add(button);
		
		if(isHorizontal) {
			for(int i = 0; i < buttons.size(); i++) {
				buttons.get(i).getBounds().width = (boundary.width / buttons.size()) - padding;
				buttons.get(i).getBounds().x = boundary.x + (i * (buttons.get(i).getBounds().width + padding)) + (padding / 2);
			}
		} else if(!isHorizontal) {
			for(int i = 0; i < buttons.size(); i++) {
				buttons.get(i).getBounds().y = boundary.y - (i * (boundary.height + padding));
			}
		}
	}
	
	public void resetButtons() {
		for(Button b : buttons)
			b.isPressed = false;
	}
	
	public Rectangle getBounds() {
		return boundary;
	}
	
	public List<Button> getButtons() {
		return buttons;
	}
	
	public void alignToSides(float sidePadding) {

		if(buttons.size() < 3) {
			buttons.get(0).getBounds().x = sidePadding;
			buttons.get(buttons.size() - 1).getBounds().x = game.getScreenSize().x - buttons.get(0).getBounds().width;
		} else {
			Gdx.app.error("error", "Button count exceeds 3. Please use only 2 buttons with this type of set." );
			Gdx.app.exit();
		}
		/*
		for(int i = 0; i < buttons.size(); i++) {
		}
		*/
	}
	
	public class Button {
		private Rectangle bounds;
		private Color bgColour;
		public String label = "";
		private Texture tex = null;
		private float texSize = 64;
		private boolean border = false;
		public boolean downTab = false; //	Extended Bottom to indicate active
		
		public boolean isPressed;
		
		public Button(String label, Color bgColour) {
			this.label = label;
			this.bgColour = bgColour;
			
			bounds = new Rectangle();
			// Equal width based on number of other buttons
			bounds.x = boundary.x;
			if(isHorizontal) {
				if(buttons.size() > 0) 
					bounds.width = (boundary.width / (buttons.size() + 1)) - padding;
				else 
					bounds.width = boundary.width - padding;
				bounds.height = boundary.height;				
				// Position based on number of previous buttons
				bounds.x = buttons.size() * bounds.width;		
				bounds.y = boundary.y - (bounds.height / 2);
				System.out.println(boundary.y + ", " + (boundary.y - (buttons.size() * (bounds.height + padding))));
			} else if(!isHorizontal){
				bounds.height = boundary.height;
				bounds.width = boundary.width;				
				// Position based on number of previous buttons
				bounds.x = boundary.x;		
				bounds.y = boundary.y - (buttons.size() * (bounds.height + padding));
				System.out.println(boundary.y + ",asd " + (boundary.y - (buttons.size() * (bounds.height + padding))));
			}
			//System.out.println(bounds.x + ", " + bounds.y + ", " + bounds.width + ", " + bounds.height);
		}	
		
		public Button(Texture tex, float texSize, Color bgColour) {
			this.tex = tex;
			this.texSize = texSize;
			this.bgColour = bgColour;
			
			bounds = new Rectangle();
			bounds.x = boundary.x;
			if(isHorizontal) {
				if(buttons.size() > 0) 
					bounds.width = (boundary.width / (buttons.size() + 1)) - padding;
				else 
					bounds.width = boundary.width - padding;
				bounds.height = boundary.height;				
				// Position based on number of previous buttons
				bounds.x = buttons.size() * bounds.width;		
				bounds.y = boundary.y - (bounds.height / 2);
			} else {
				bounds.height = boundary.height;
				bounds.width = boundary.width;				
				// Position based on number of previous buttons
				bounds.x = boundary.x;		
				bounds.y = boundary.y ;
			}

			//System.out.println(bounds.x + ", " + bounds.y + ", " + bounds.width + ", " + bounds.height);
			
		}	
		
		public void render() {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

			if(isHorizontal) {
				if(downTab) {
					bounds.height = boundary.height + game.getScreenSize().y * 0.02f;
					bounds.y = boundary.y - (bounds.height / 2) - (game.getScreenSize().y * 0.01f) ;
				} else{
					bounds.height = boundary.height;
					bounds.y = boundary.y - (bounds.height / 2);
				}
			}
			//	Background
			game.renderer.begin(ShapeType.Filled);
			if(!isPressed)
				game.renderer.setColor(bgColour.r, bgColour.g, bgColour.b, 0.8f);
			else
				game.renderer.setColor(bgColour.r, bgColour.g, bgColour.b, 1f);
			
			game.renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
			game.renderer.end();
			
			//	Border
			if(border) {
				game.renderer.begin(ShapeType.Line);
				game.renderer.setColor(Color.BLACK);
				game.renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
				game.renderer.end();
			}
			
			//	Text

			game.batch.begin();
			if(label != "") {
				game.layout.setText(Assets.subheadingFont, label);
				Assets.subheadingFont.draw(game.batch, label, (bounds.x + (bounds.width / 2)) - (game.layout.width / 2), bounds.y + (bounds.height / 1.5f));
			}
			if(tex != null) {
				game.batch.setColor(Color.WHITE);
				game.batch.draw(tex, (bounds.x + (bounds.width / 2)) - (texSize / 2), (bounds.y + (bounds.height / 2)) - (texSize / 2), texSize, texSize);
			}
			
			//	Icon
			game.batch.end();
		}

		public void execute() {
			System.out.println(label + " was pressed! ");
		}
		
		public Rectangle getBounds() {
			return bounds;
		}
	}
}