package com.volvet.superjumper;

import com.volvet.framework.GameObject;

public class Castle extends GameObject {	
	public static final float CASTLE_WIDTH = 1.7f;
	public static final float CASTLE_HEIGHT = 1.7f;
	
	public Castle(float x, float y){
		super(x, y, CASTLE_WIDTH, CASTLE_HEIGHT);
	}
}
