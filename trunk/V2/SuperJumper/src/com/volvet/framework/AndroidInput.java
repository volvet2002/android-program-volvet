package com.volvet.framework;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.os.Build.VERSION;

public class AndroidInput implements Input {
	AccelerometerHandler  accelHandler;
	TouchHandler          touchHandler;
	KeyboardHandler       keyboardHandler;
	
	public AndroidInput(Context  context,  View view, float scaleX, float scaleY) {
		accelHandler = new AccelerometerHandler(context);
		keyboardHandler = new KeyboardHandler(view);
		
		if( Integer.parseInt(VERSION.SDK) < 5 ){
			touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
		} else {
			touchHandler = new MultiTouchHandler(view, scaleX, scaleY);
		}
	}

	@Override
	public boolean isKeyPressed(int keyCode) {
		// TODO Auto-generated method stub
		return keyboardHandler.isKeyPressed(keyCode);
	}

	@Override
	public boolean isTouchDown(int pointer) {
		// TODO Auto-generated method stub
		return touchHandler.isTouchDown(pointer);
	}

	@Override
	public int getTouchX(int pointer) {
		// TODO Auto-generated method stub
		return touchHandler.getTouchX(pointer);
	}

	@Override
	public int getTouchY(int pointer) {
		// TODO Auto-generated method stub
		return touchHandler.getTouchY(pointer);
	}

	@Override
	public float getAccelX() {
		// TODO Auto-generated method stub
		return accelHandler.getAccelX();
	}

	@Override
	public float getAccelY() {
		// TODO Auto-generated method stub
		return accelHandler.getAccelY();
	}

	@Override
	public float getAccelZ() {
		// TODO Auto-generated method stub
		return accelHandler.getAccelZ();
	}

	@Override
	public List<KeyEvent> getKeyEvent() {
		// TODO Auto-generated method stub
		return keyboardHandler.getKeyEvents();
	}

	@Override
	public List<TouchEvent> getTouchEvent() {
		// TODO Auto-generated method stub
		return touchHandler.getTouchEvents();
	}

}
