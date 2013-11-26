package com.volvet.superjumper;

import com.volvet.framework.GameObject;

public class Spring extends GameObject {
	public static final float SPRING_WIDTH = 0.3f;
	public static final float SPRINT_HEIGHT = 0.3f;
	public Spring(float x, float y){
		super(x, y, SPRING_WIDTH, SPRINT_HEIGHT);
	}

}
