package com.unshape.game;

import java.util.ArrayList;
import java.util.List;

public class ShapeForm {
	public static List<Point> tempPoints;
	
	public static List<Point> createTriangle(float radius, Main game) {
		tempPoints = new ArrayList<Point>();
		tempPoints.add(new Point(-1f, -1f, radius, game));
		tempPoints.add(new Point(1f, -1f, radius, game));
		tempPoints.add(new Point(0, 1f, radius, game));
		return tempPoints;
	}

	public static List<Point> createSquare(float radius, Main game) {
		tempPoints = new ArrayList<Point>();
		tempPoints.add(new Point(-1f,-1f, radius, game));
		tempPoints.add(new Point(1f,-1f, radius, game));
		tempPoints.add(new Point(1, 1f, radius, game));
		tempPoints.add(new Point(-1, 1f, radius, game));
		return tempPoints;
	}
	
	public static  List<Point> createPentagon(float radius, Main game) {
		tempPoints = new ArrayList<Point>();
		tempPoints.add(new Point(-0.6f,-1f, radius, game));
		tempPoints.add(new Point(0.6f,-1f, radius, game));
		tempPoints.add(new Point(1, 0.1f, radius, game));
		tempPoints.add(new Point(0f, 1f, radius, game));
		tempPoints.add(new Point(-1f, 0.1f, radius, game));
		return tempPoints;
	}
	
	public static List<Point> createHexagon(float radius, Main game) {
		tempPoints = new ArrayList<Point>();
		tempPoints.add(new Point(-0.5f,-1f, radius, game));
		tempPoints.add(new Point(0.5f,-1f, radius, game));
		tempPoints.add(new Point(1, 0f, radius, game));
		tempPoints.add(new Point(0.5f, 1f, radius, game));
		tempPoints.add(new Point(-0.5f, 1f, radius, game));
		tempPoints.add(new Point(-1f, 0f, radius, game));
		return tempPoints;
	}
	
	public static List<Point> createSeptagon(float radius) {
		
		return tempPoints;
	}
	
	public static List<Point> createOctogon(float radius) {
		
		return tempPoints;
	}
	
	public static List<Point> createNonogon(float radius) {
		
		return tempPoints;
	}
	
	public static List<Point> createDecagon(float radius) {
		
		return tempPoints;
	}	
}
