package com.volvet.framework;

import java.util.List;
import java.util.ArrayList;

import android.view.MotionEvent;
import android.view.View;

import com.volvet.framework.Input.TouchEvent;
import com.volvet.framework.Pool.PoolObjectFactory;

public class MultiTouchHandler implements TouchHandler {
	
	private static final int MAX_TOUCHPOINTS = 20;
	boolean[]   isTouched = new boolean[MAX_TOUCHPOINTS];
	int[]  touchX = new int[MAX_TOUCHPOINTS];
	int[]  touchY = new int[MAX_TOUCHPOINTS];
	//int[]  id = new int[MAX_TOUCHPOINTS];
	
	Pool<TouchEvent> touchEventPool;
	List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
	List<TouchEvent> touchEventBuffer = new ArrayList<TouchEvent>();
	float  scaleX;
	float  scaleY;
	
	public MultiTouchHandler(View view, float scaleX, float scaleY) {
		PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
			@Override
			public TouchEvent createObject() {
				return new TouchEvent();
			}
		};
		touchEventPool = new Pool<TouchEvent>(factory, 100);
		view.setOnTouchListener(this);
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		synchronized(this) {
			int action = event.getAction() & MotionEvent.ACTION_MASK;
			int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
			int pointerCount = event.getPointerCount();
			int pointerId = event.getPointerId(pointerIndex);
			
			TouchEvent touchEvent;
			switch(action) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				touchEvent = touchEventPool.newObject();
				touchEvent.type = TouchEvent.TOUCH_DOWN;
				touchEvent.pointer = pointerId;
				touchEvent.x = touchX[pointerId] = (int)(event.getX() * scaleX);
				touchEvent.y = touchY[pointerId] = (int)(event.getY() * scaleY);
				isTouched[pointerId] = true;
				touchEventBuffer.add(touchEvent);
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
			case MotionEvent.ACTION_CANCEL:
				touchEvent = touchEventPool.newObject();
				touchEvent.type = TouchEvent.TOUCH_UP;
				touchEvent.pointer = pointerId;
				touchEvent.x = touchX[pointerId] = (int)(event.getX() * scaleX);
				touchEvent.y = touchY[pointerId] = (int)(event.getY() * scaleY);
				isTouched[pointerId] = false;
				touchEventBuffer.add(touchEvent);	
				break;
			case MotionEvent.ACTION_MOVE:
				touchEvent = touchEventPool.newObject();
				touchEvent.type = TouchEvent.TOUCH_DRAGGED;
				touchEvent.pointer = pointerId;
				touchEvent.x = touchX[pointerId] = (int)(event.getX() * scaleX);
				touchEvent.y = touchY[pointerId] = (int)(event.getY() * scaleY);			
				touchEventBuffer.add(touchEvent);	
				break;
			}
			return true;			
		}		
	}	
	
	@Override
	public boolean isTouchDown(int pointer) {
		// TODO Auto-generated method stub		
		if( pointer < 0 || pointer >= MAX_TOUCHPOINTS ) {
			return false;
		} else {
		    return isTouched[pointer];
		}
	}

	@Override
	public int getTouchX(int pointer) {
		// TODO Auto-generated method stub
		if( pointer < 0 || pointer >= MAX_TOUCHPOINTS ) {
			return 0;
		} else {
		    return touchX[pointer];
		}
	}

	@Override
	public int getTouchY(int pointer) {
		// TODO Auto-generated method stub
		if( pointer < 0 || pointer >= MAX_TOUCHPOINTS ) {
			return 0;
		} else {
		    return touchY[pointer];
		}
	}

	@Override
	public List<TouchEvent> getTouchEvents() {
		// TODO Auto-generated method stub
		synchronized(this) {
			int len = touchEvents.size();
			for( int i=0;i<len;i++ ){
				touchEventPool.free(touchEvents.get(i));
			}
			touchEvents.clear();
			touchEvents.addAll(touchEventBuffer);
			touchEventBuffer.clear();
			
			return touchEvents;
		}
	}

}
