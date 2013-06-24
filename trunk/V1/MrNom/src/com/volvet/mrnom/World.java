package com.volvet.mrnom;

import java.util.Random;

public class World {
	static final int WORLD_WIDTH = 10;
	static final int WORLD_HEIGHT = 13;
	static final int SCORE_INCREMENT = 10;
	static final float  TICK_INITIAL = 0.5f;
	static final float  TICK_DECREMENT = 0.05f;
	
	public  Snake  snake;
	public  Stain  stain;
	public  boolean  gameOver = false;
	public  int    score = 0;
	
	boolean fields[][] = new boolean[WORLD_WIDTH][WORLD_HEIGHT];
	Random  random = new Random();
	float   tickTime = 0;
    static  float tick = TICK_INITIAL;
    
    public   World() {
    	snake = new Snake(WORLD_WIDTH, WORLD_HEIGHT);
    	
    	placeStain();
    }
    
    private  void  placeStain() {
    	int x = 0, y = 0;
    	
    	for( x=0;x<WORLD_WIDTH;x++ ){
    		for( y=0;y<WORLD_HEIGHT;y++ ){
    			fields[x][y] = false;
    		}
    	}
    	
    	int size = snake.parts.size();
    	for( x=0;x<size;x++ ){
    		SnakePart part = snake.parts.get(x);
    		fields[part.x][part.y] = true;
    	}
    	
    	int stainX = random.nextInt(WORLD_WIDTH);
    	int stainY = random.nextInt(WORLD_HEIGHT);
    	
    	for( x=stainX;x<WORLD_WIDTH;x++ ){
    		for( y=stainY;y<WORLD_HEIGHT;y++ ){
    			if( fields[x][y] == false ){
    				stain = new Stain(x, y, random.nextInt(3));
    				return;
    			}
    		}
    	}  	
    	
    }
    
    public  void  update(float deltaTime) {
    	if( gameOver ){
    		return;
    	}
    	
    	tickTime += deltaTime;
    	if( tickTime > tick ){
    		tickTime -= tick;
    		snake.advance();
    	}
    	
    	if( snake.checkBitten() ){
    		gameOver = true;
    		return;
    	}
    	
    	SnakePart head = snake.parts.get(0);
    	if( head.x == stain.x && head.y == stain.y ){
    		score += SCORE_INCREMENT;
    		snake.eat();
    		if( snake.parts.size() == WORLD_WIDTH * WORLD_HEIGHT ){
    			gameOver = true;
    			return;
    		} else {
    			placeStain();
    		}
    		
    		if( score % 100 == 0 && tick - TICK_DECREMENT > 0 ){
    			tick -= TICK_DECREMENT;
    		}
    	}
    }

}
