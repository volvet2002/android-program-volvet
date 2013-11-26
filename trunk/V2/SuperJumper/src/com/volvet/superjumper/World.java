package com.volvet.superjumper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.volvet.framework.OverlapTester;
import com.volvet.framework.Vector2;


public class World {
	public interface WorldListener {
		public void jump(); 
		public void highJump();
		public void hit();
		public void coin();
	}
	
	public static final float WORLD_WIDTH = 10;
	public static final float WORLD_HEIGHT = 15*20;
	public static final int   WORLD_STATE_RUNNING = 0;
	public static final int   WORLD_STATE_NEXT_LEVEL = 1;
	public static final int   WORLD_STATE_GAME_OVER = 2;
	public static final Vector2   gravity = new Vector2(0, -12);
	
	public final Bob              bob;
	public final List<Platform>   platforms;
	public final List<Spring>     springs; 
	public final List<Squirrel>   squirrels;
	public final List<Coin>       coins;
	public Castle                 castle;
	public final WorldListener    listener;
	public final Random           rand;
	
	public float                  heightSoFar;
	public int                    score;
	public int                    state;
	
	public World(WorldListener  listener){
		this.bob = new Bob(5, 1);
		this.platforms = new ArrayList<Platform>();
		this.springs = new ArrayList<Spring>();
		this.squirrels = new ArrayList<Squirrel>();
		this.coins = new ArrayList<Coin>();
		this.listener = listener;		
		rand = new Random();
		
		//FIXME:   generate level
		generateLevel();
		
		this.heightSoFar = 0;
		this.score = 0;
		this.state = WORLD_STATE_RUNNING;		
	}
	
	private void generateLevel(){
		float y = Platform.PLATFORM_HEIGHT/2;
		float maxJumpHeight = Bob.BOB_JUMP_VELOCITY * Bob.BOB_JUMP_VELOCITY /(2*-gravity.y);
		
		while( y < WORLD_HEIGHT - WORLD_WIDTH/2 ){
	        int type = rand.nextFloat() > 0.8f ? Platform.PLATFORM_TYPE_MOVING : Platform.PLATFORM_TYPE_STATIC;
	        float x = rand.nextFloat() * (WORLD_WIDTH - Platform.PLATFORM_WIDTH) + Platform.PLATFORM_WIDTH/2;
	        Platform platform = new Platform(type, x, y);
	        platforms.add(platform);
	        
	        if( (rand.nextFloat()>0.9f) && (type!=Platform.PLATFORM_TYPE_MOVING) ){
	        	Spring spring = new Spring(platform.position.x, platform.position.y + Platform.PLATFORM_HEIGHT/2);
	        	springs.add(spring);	        	
	        }
	        
	        if( y>WORLD_HEIGHT/3 && rand.nextFloat()>0.8f ){
	        	Squirrel squirrel = new Squirrel(platform.position.x + rand.nextFloat(),
	        			platform.position.y + Squirrel.SQUIRREL_HEIGHT + rand.nextFloat() * 2);
	        	squirrels.add(squirrel);
	        }
	        
	        if( rand.nextFloat() > 0.6f ){
	        	Coin coin = new Coin(platform.position.x + rand.nextFloat(), 
	        			platform.position.y + Coin.COIN_HEIGHT + rand.nextFloat()*3);
	        	coins.add(coin);
	        }
	        
	        y += (maxJumpHeight - 0.5f);
	        y -= rand.nextFloat() * (maxJumpHeight/3);	        
		}
		
		castle = new Castle(WORLD_WIDTH/2, y);
	}

	public void update(float deltaTime, float accelX){
		updateBob(deltaTime, accelX);
		updatePlatforms(deltaTime);
		updateSquirrels(deltaTime);
		updateCoins(deltaTime);
		
		//FIXME:  
		if( bob.state != Bob.BOB_STATE_HIT)
		    checkCollisions();
		
		checkGameOver();
	}
	
	private void updateBob(float deltaTime, float accelX) {
		if( bob.state != Bob.BOB_STATE_HIT && bob.position.y <= 0.5f ){
			bob.hitPlatform();
		}
		
		if( bob.state != Bob.BOB_STATE_HIT ){
			bob.velocity.x = -accelX/10*Bob.BOB_MOVE_VELOCITY;		
		}
		bob.update(deltaTime);
		heightSoFar = Math.max(bob.position.y, heightSoFar);
	}
	
	private void updatePlatforms(float deltaTime) {
		int size = platforms.size();
		
		for( int i=0;i<size;i++ ){
			Platform platform = platforms.get(i);
			platform.update(deltaTime);
			
			if( platform.state == Platform.PLATFORM_STATE_PULVERIZING && 
					platform.stateTime > Platform.PLATFORM_PULVERZING_TIME ){
				platforms.remove(i);
				size -= 1;
			}
		}
	}
	
	private void updateSquirrels(float deltaTime) {
		int size = squirrels.size();
		
		for( int i=0;i<size;i++ ){
			Squirrel squirrel = squirrels.get(i);
			squirrel.update(deltaTime);
		}
	}
	
	private void updateCoins(float deltaTime) {
        int size = coins.size();
		
		for( int i=0;i<size;i++ ){
			Coin coin = coins.get(i);
			coin.update(deltaTime);
		}
	}
	
	private void checkCollisions() {
	    checkPlatformCollisions();	
	    checkSquirrelCollisions();
	    checkItemsCollisions();
	    checkCastleCollisioins();
	}
	
	private void checkPlatformCollisions() {
		if( bob.velocity.y > 0 ) return;
		
		int size = platforms.size();
		
		for( int i=0;i<size;i++ ){
			Platform platform = platforms.get(i);
			
			if( bob.position.y > platform.position.y ){
				if(OverlapTester.overlatRectangle(bob.bounds, platform.bounds)){
					bob.hitPlatform();
					listener.jump();
					
					if( rand.nextFloat() > 0.5f ){
						platform.pulverize();
					}
					break;
				}				
			}
		}
	}
	
	private void checkSquirrelCollisions() {
		int size = squirrels.size();
		
		for( int i=0;i<size;i++ ){
			Squirrel squirrel = squirrels.get(i);
			if( OverlapTester.overlatRectangle(bob.bounds, squirrel.bounds)){
				bob.hitSquirrel();
				listener.hit();
				//need break?
				break;
			}
		}
	}
	
	private void checkItemsCollisions() {
		int size = coins.size();		
		int i; 
		
		for( i=0;i<size;i++ ){
			Coin coin = coins.get(i);
			if( OverlapTester.overlatRectangle(bob.bounds, coin.bounds)){
				coins.remove(coin);
				size = coins.size();
				listener.coin();
				score += Coin.COIN_SCORE;
				//need break?
				break;
			}
		}
		
		if( bob.velocity.y > 0 ) return;
		
		size = springs.size();
		for( i=0;i<size;i++ ){
			Spring spring = springs.get(i);
			if( OverlapTester.overlatRectangle(bob.bounds, spring.bounds)){
				bob.hitSpring();
				listener.highJump();
				//need break?
				break;
			}
		}
	}
	
	private void checkCastleCollisioins() {
		if( OverlapTester.overlatRectangle(bob.bounds,	castle.bounds)){
			state = WORLD_STATE_NEXT_LEVEL;
		}
	}
	
	private void checkGameOver() {
		if( heightSoFar - 7.5f > bob.position.y ){
			state = WORLD_STATE_GAME_OVER;
		}
	}
}
