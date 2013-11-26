package com.volvet.superjumper;

import com.volvet.framework.DynamicGameObject;

public class Platform extends DynamicGameObject {
	public static final float PLATFORM_WIDTH = 2;
	public static final float PLATFORM_HEIGHT = 0.5f;
	public static final int PLATFORM_TYPE_STATIC = 0;
	public static final int PLATFORM_TYPE_MOVING = 1;
	public static final int PLATFORM_STATE_NORMAL = 0;
	public static final int PLATFORM_STATE_PULVERIZING = 1;
	public static final float PLATFORM_PULVERZING_TIME = 0.2f * 4;
	public static final float PLATFORM_VELOCITY = 2;
	
	float stateTime;
	int   type;
	int   state;
	public Platform(int type, float x, float y){
		super(x, y, PLATFORM_WIDTH, PLATFORM_HEIGHT);
		this.type = type;
		this.stateTime = 0;
		this.state = PLATFORM_STATE_NORMAL;
		if( type == PLATFORM_TYPE_MOVING ){
			velocity.x = PLATFORM_VELOCITY;
		}
	}
	
	public void update(float deltaTime){
		if( type == PLATFORM_TYPE_MOVING ){
			position.add(velocity.x * deltaTime, velocity.y * deltaTime);
			bounds.lowerleft.set(position).sub(PLATFORM_WIDTH/2, PLATFORM_HEIGHT/2);
			
			//FIXME
			if(position.x < PLATFORM_WIDTH / 2) {
	            velocity.x = -velocity.x;
	            position.x = PLATFORM_WIDTH / 2;
	        }
	        if(position.x > World.WORLD_WIDTH - PLATFORM_WIDTH / 2) {
	            velocity.x = -velocity.x;
	            position.x = World.WORLD_WIDTH - PLATFORM_WIDTH / 2;
	        }       
		}
		
		stateTime += deltaTime;
	}
	
	public void pulverize() {
        state = PLATFORM_STATE_PULVERIZING;
        stateTime = 0;
        velocity.x = 0;
    }

}
