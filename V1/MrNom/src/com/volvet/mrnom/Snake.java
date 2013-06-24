package com.volvet.mrnom;

import java.util.List;
import java.util.ArrayList;

public class Snake {
	public static final int UP = 0;
	public static final int LEFT = 1;
	public static final int DOWN = 2;
	public static final int RIGHT = 3;
	
	public List<SnakePart>  parts = new ArrayList<SnakePart>();
	public int    direction;
	private final int  worldWidth, worldHeight; 
	
	public Snake(int worldWidth, int worldHeight) {
		direction = UP;
		parts.add(new SnakePart(5, 6));
		parts.add(new SnakePart(5, 7));
		parts.add(new SnakePart(5, 8));
		
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
	}
	
	public void turnLeft() {
		direction += 1;
		
		if( direction > RIGHT ){
			direction = UP;
		}
	}
	
	public void turnRight() {
		direction -= 1;
		
		if( direction < UP ){
			direction = RIGHT;
		}
	}
	
	public void eat() {
		SnakePart end = parts.get(parts.size() - 1);
		parts.add(new SnakePart(end.x, end.y));
	}
	
    public void advance() {
    	SnakePart head = parts.get(0);
    	
    	int size = parts.size();
    	
    	for( int i=size - 1;i>0;i-- ){
    		SnakePart part = parts.get(i);
    		SnakePart before = parts.get(i-1);
    		
    		part.x = before.x;
    		part.y = before.y;
    	}
    	
    	if( direction == UP ){
    		head.y -= 1;
    	}
    	if( direction == LEFT ){
    		head.x -= 1;
    	}
    	if( direction == DOWN ){
    		head.y += 1;
    	}
    	if( direction == RIGHT ){
    		head.x += 1;
    	}
    	
    	if( head.x < 0 ){
    		head.x = worldWidth - 1;
    	}
    	if( head.x >= worldWidth ){
    		head.x = 0;
    	}
    	if( head.y < 0 ){
    		head.y = worldHeight - 1;
    	}
    	if( head.y >= worldHeight ){
    		head.y = 0;
    	}    	
    }
    
    public boolean checkBitten() {
        int size = parts.size();
        
        SnakePart head = parts.get(0);
        
        for( int i=1;i<size;i++ ){
        	SnakePart part = parts.get(i);
        	if( head.x == part.x && part.y == head.y ){
        		return true;
        	}
        }
        return false;
    }
}
