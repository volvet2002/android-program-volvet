package com.volvet.superjumper;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;
import com.volvet.framework.GLGame;
import com.volvet.framework.Screen;

public class MainActivity extends GLGame {
	boolean   firstTimeCreate = true;

	@Override
	public Screen getStartScreen() {
		return new MainMenuScreen(this);  
	}
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		super.onSurfaceCreated(gl, config);
		
		if(firstTimeCreate){
			Assets.load(this);
			Setting.load(getFileIO());
			firstTimeCreate = false;
		} else {
			Assets.reload();
		}
		
	}

	@Override
	public void onPause() {
		super.onPause();
		
		if(Setting.soundEnable){
			Assets.music.pause();
		}
	}
}
