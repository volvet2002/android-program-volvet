package com.volvet.framework;

public class Circle {
	public final float radius;
	public final Vector2 center;
	
	public Circle(float x, float y, float radius){
		center = new Vector2(x,y);
		this.radius = radius;
	}

}
