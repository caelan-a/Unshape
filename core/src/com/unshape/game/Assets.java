package com.unshape.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.unshape.game.SmartFontGenerator;

public class Assets {
	//	Visual
	public static TextureAtlas gameTextureAtlas;
	
	public static TextureRegion point;
	public static TextureRegion highscoreBg;
	public static TextureRegion musicOn;
	public static TextureRegion musicOff;
	public static Texture settingsCog;
	public static Texture target;
	public static Texture time;
	
	//Icons
	public static Texture settingsIcon;
	public static Texture quaverIcon;
	public static Texture playIcon;
	public static Texture shareIcon;
	public static Texture starIcon;
	public static Texture trophyIcon;
	public static Texture gemIcon;
	public static Texture heartIcon;
	public static Texture levelUpIcon;
	public static Texture killAllIcon;
	public static Texture timeIcon;
	public static Texture buyGemIcon;
	public static Texture backIcon;
	
	//	Audio
	public static Sound ting;
	public static Sound chime;
	public static Music ost;
	
	//	Values
	public static int highscore;
	public static int lastScore;
	public static int level;
	
	//	Fonts
	public static BitmapFont titleFont;			//	For mainscreen title
	public static BitmapFont subheadingFont; 	//	For mainscreen subheading
	public static BitmapFont highscoreFont;		//	For mainscreen highscore
	public static BitmapFont optionFont;		//	For 'tap to ____'
	public static BitmapFont scoreFont;				// 	For gamescreen font
	
	//	Particle Effects
	public static ParticleEffect shapeExplosion;
	
	//	Preferences
	public static Preferences prefs;
	
	public static void generateFonts(SmartFontGenerator fontGen, float x, float y) {
		FileHandle squareFont = Gdx.files.internal("kenpixel_square.ttf");
		
		titleFont = fontGen.createFont(squareFont, "titleFont", (int) (x / 5.65f), 4, 2, 0.4f);
		subheadingFont = fontGen.createFont(squareFont, "subheadingFont", (int) (x / 24), 1, 1, 0.4f);
		highscoreFont = fontGen.createFont(squareFont, "highscoreFont", (int) (x / 4.6f), 8, 4, 0);
		optionFont = fontGen.createFont(squareFont, "optionFont", (int) (x / 16), 4, 2, 0.4f);
		scoreFont = fontGen.createFont(squareFont, "scoreFont", (int) (y / 12.8f), 4, 2, 0.4f);
	}
	
	public static void loadAssets(Preferences prefs) {
		Assets.prefs = prefs;
		prefs.putInteger("level", 1);
		prefs.flush();
		System.out.println("Loading...");
	
		//	
		settingsIcon = new Texture(Gdx.files.internal("icons/settings_icon.png"));
		gemIcon = new Texture(Gdx.files.internal("icons/gem2.png"));
		quaverIcon = new Texture(Gdx.files.internal("icons/quaver_icon.png"));
		playIcon = new Texture(Gdx.files.internal("icons/play_icon.png"));
		shareIcon = new Texture(Gdx.files.internal("icons/share_icon.png"));
		starIcon = new Texture(Gdx.files.internal("icons/star_icon.png"));
		trophyIcon = new Texture(Gdx.files.internal("icons/trophy_icon.png"));
		heartIcon = new Texture(Gdx.files.internal("icons/heart.png"));
		levelUpIcon = new Texture(Gdx.files.internal("icons/level.png"));
		killAllIcon = new Texture(Gdx.files.internal("icons/comet.png"));
		timeIcon = new Texture(Gdx.files.internal("icons/hourglass.png"));
		buyGemIcon = new Texture(Gdx.files.internal("icons/buyGem.png"));
		backIcon = new Texture(Gdx.files.internal("icons/back.png"));
		
		
		highscore = prefs.getInteger("highscore", 0);
		level = prefs.getInteger("level", 1);
		
		settingsCog = new Texture(Gdx.files.internal("settings_icon.png"));
		time = new Texture(Gdx.files.internal("time.png"));
		target = new Texture(Gdx.files.internal("target.png"));
		
		gameTextureAtlas = new TextureAtlas(Gdx.files.internal("assets.txt"));
		point = gameTextureAtlas.findRegion("point");
		highscoreBg = gameTextureAtlas.findRegion("highscore_bg");
		musicOn = gameTextureAtlas.findRegion("music");
		musicOff = gameTextureAtlas.findRegion("music_off");
		
		System.out.println("[Textures] loaded..");
		
		System.out.println("[highscore_bg.png] loaded..");
		ting = Gdx.audio.newSound(Gdx.files.internal("blop.wav"));
		System.out.println("[blop.wav] loaded..");
		chime = Gdx.audio.newSound(Gdx.files.internal("chime.mp3"));
		System.out.println("[chime.mp3] loaded..");
		ost = Gdx.audio.newMusic(Gdx.files.internal("ost.mp3"));
		System.out.println("[ost.mp3] loaded..");
	}
	
	public static void setLevel(int level) {
		prefs.putInteger("level", level);
		Assets.level = prefs.getInteger("level", 1);
		prefs.flush();
		System.out.println("Current Level: " + Assets.level + " to new level: " + level);
	}
	
	public static void setMusic() {
		prefs.putBoolean("music", !prefs.getBoolean("music"));
	}
	
	public static boolean getMusic() {
		return prefs.getBoolean("music");
	}
}
