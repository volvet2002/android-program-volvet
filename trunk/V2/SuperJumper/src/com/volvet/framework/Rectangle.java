package com.volvet.framework;

public class Rectangle {
	public final Vector2 lowerleft;
	public final float   width, height;

	public Rectangle(float x, float y, float width, float height) {
		lowerleft = new Vector2(x,y);
		this.width = width;
		this.height = height;
	}
}
