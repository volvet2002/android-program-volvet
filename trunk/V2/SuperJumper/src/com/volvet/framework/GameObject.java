package com.volvet.framework;

public class GameObject {
    public final Vector2 position;
    public final Rectangle bounds;
    
    public GameObject(float x, float y, float width, float height) {
    	position = new Vector2(x, y);
    	bounds = new Rectangle(x - width/2, y - height/2, width, height);
    }
	
}
