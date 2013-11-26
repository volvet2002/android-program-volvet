package com.volvet.framework;

public class OverlapTester {
    static public boolean overlatCircles(Circle left, Circle right){
    	float dist = left.center.dist(right.center);
    	if( dist < left.radius + right.radius ){
    		return true;
    	}
    	return false;
    }
    
    static public boolean overlatRectangle(Rectangle left, Rectangle right) {
    	if( left.lowerleft.x < right.lowerleft.x + right.width && 
    		left.lowerleft.x + left.width > right.lowerleft.x && 
    		left.lowerleft.y < right.lowerleft.y + right.height && 
    		left.lowerleft.y + left.height > right.lowerleft.y ){
    		return true;
    	}
    	
    	return false;
    }
    
    static public boolean overlapRectangleCircle(Rectangle rect, Circle circle) {
    	float closedX = circle.center.x;
    	float closedY = circle.center.y;
    	
    	if( closedX < rect.lowerleft.x ){
    		closedX = rect.lowerleft.x;
    	} else if( closedX > rect.lowerleft.x + rect.width ){
    		closedX = rect.lowerleft.x + rect.width;
    	}
    	
    	if( closedY < rect.lowerleft.y ){
    		closedY = rect.lowerleft.y;
    	} else if( closedY > rect.lowerleft.y + rect.height ){
    		closedY = rect.lowerleft.y + rect.height;
    	}
    	
    	return pointInCircle(closedX, closedY, circle);    	
    }
    
    static public boolean pointInCircle(float x, float y, Circle circle){
    	return circle.center.distSquared(x, y) < circle.radius * circle.radius;
    }
    
    static public boolean pointInCircle(Vector2 point, Circle circle) {
    	return pointInCircle(point.x, point.y, circle);
    }
    
    static public boolean pointInRectangle(float x, float y, Rectangle rect) {
    	return rect.lowerleft.x <= x && rect.lowerleft.x + rect.width >= x &&
    			rect.lowerleft.y <= y && rect.lowerleft.y + rect.height >= y;
    }
    
    static public boolean pointInRectangle(Vector2 point, Rectangle rect) {
    	return pointInRectangle(point.x, point.y, rect);
    }
}
