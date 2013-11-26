package com.volvet.superjumper;

import com.volvet.framework.GameObject;

public class Coin extends GameObject {
	public static final float COIN_WIDTH = 0.2f;
	public static final float COIN_HEIGHT = 0.3f;
	public static final int   COIN_SCORE = 10;
	
	float   startTime; 
	public Coin(float x, float y){
		super(x, y, COIN_WIDTH, COIN_HEIGHT);
		startTime = 0;
	}
	
	public void update(float deltaTime){
		startTime += deltaTime;
	}
}
