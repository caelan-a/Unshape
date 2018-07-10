package com.unshape.game;

public class Level {
	private static Main game;
	
	public static void setLevel(Main game) {
		Level.game = game;
		game.prefs.putInteger("level", 1);
		
		switch (game.prefs.getInteger("level")) {
		case 1: 
			showLevelMarker(game.prefs.getInteger("level"));
			setFrequency(1.5f);
			setShapeComplexity(4);
			setShapeCount(5);
			setSpeed();
			
			break;
		}
	}
	
	public static void showLevelMarker(int level) {
		game.gameScreen.hud.levelMarker.showLevel(level);
	}
	
	public static void setFrequency(float freq) {
		game.gameScreen.getShapeHandler().getSpawner().setFrequency(1.5f);
	}
	
	public static void setShapeCount(int duration) {
		//game.gameScreen.level.shapeCount = duration;
		game.gameScreen.getShapeHandler().getSpawner().shapesToSpawn = duration;
		game.gameScreen.getShapeHandler().getSpawner().shapesSpawned = 0;
	}
	
	public static void setSpeed() {
		//
	}
	
	public static void setShapeComplexity(int columns) {
		game.gameScreen.getShapeHandler().getSpawner().setMaxShapeNum(columns);
	}
	
	public static void setColumns(int columns) {
		game.gameScreen.getShapeHandler().getSpawner().calibrateColumns(columns);
	}
}
