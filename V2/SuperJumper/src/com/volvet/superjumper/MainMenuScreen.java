package com.volvet.superjumper;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;
import com.volvet.framework.GLScreen;
import com.volvet.framework.Game;
import com.volvet.framework.Input.TouchEvent;
import com.volvet.framework.Camera2D;
import com.volvet.framework.SpriteBatcher;
import com.volvet.framework.OverlapTester;
import com.volvet.framework.Rectangle;
import com.volvet.framework.Vector2;

public class MainMenuScreen extends GLScreen {
	Camera2D         guiCam;
	SpriteBatcher    batcher;
	Rectangle        soundBounds;
	Rectangle        playBounds;
	Rectangle        highScoresBounds;
	Rectangle        helpBounds;
	Vector2          touchPoint;
	
	public MainMenuScreen(Game game) {
		super(game);
		
		guiCam = new Camera2D(glGraphics, 320, 480);
		batcher = new SpriteBatcher(glGraphics, 100);
		soundBounds = new Rectangle(0, 0, 64, 64);
		playBounds = new Rectangle(160-150, 200+18, 300, 36);
		highScoresBounds = new Rectangle(160-150, 200-18, 300, 36);
		helpBounds = new Rectangle(160-150, 200-18-36, 300, 36);
		touchPoint = new Vector2();
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		game.getInput().getKeyEvent();
		List<TouchEvent>  events = game.getInput().getTouchEvent();
		
		int len = events.size();
		for( int i=0;i<len;i++ ){
			TouchEvent event = events.get(i);
			touchPoint.set(event.x, event.y);
			guiCam.touchToWorld(touchPoint);
			
			if(event.type == TouchEvent.TOUCH_UP){			
			    if( OverlapTester.pointInRectangle(touchPoint, playBounds) ){
				    Assets.playSound(Assets.clickSound);
				    //TODO:  play screen
				    game.setScreen(new GameScreen(game));
				    return;
			    } else if( OverlapTester.pointInRectangle(touchPoint, highScoresBounds)){
				    Assets.playSound(Assets.clickSound);
				    //TODO:   high scores screen
				    game.setScreen(new HighScoresScreen(glGame));
				    return;
			    } else if( OverlapTester.pointInRectangle(touchPoint, helpBounds) ){
				    Assets.playSound(Assets.clickSound);
				    //TODO:   help screen
				    game.setScreen(new HelpScreen(glGame));
				    return;
			    } else if( OverlapTester.pointInRectangle(touchPoint, soundBounds)){
				//Assets.playSound(Assets.clickSound);
				    Setting.soundEnable = ! Setting.soundEnable;
				    if( Setting.soundEnable ){
					    Assets.music.play();
			 	    } else {
					    Assets.music.pause();
				    }
				    return;
			    }
			}
		}
	}

	@Override
	public void present(float deltaTime) {
		// TODO Auto-generated method stub
        GL10  gl = glGraphics.getGL();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        guiCam.setViewportAndMatrices();
        gl.glEnable(GL10.GL_TEXTURE_2D);
        
        batcher.beginBatch(Assets.background);
        batcher.drawSprite(160, 240, 320, 480, Assets.backgroundRegion);
        batcher.endBatch();
        
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        
        batcher.beginBatch(Assets.items);
        batcher.drawSprite(160, 480-10-71, 274, 142, Assets.logo);
        batcher.drawSprite(160, 200, 300, 110, Assets.mainMenu);
        batcher.drawSprite(32, 32, 64, 64, Setting.soundEnable?Assets.soundOn:Assets.soundOff);
        batcher.endBatch();
        
        gl.glDisable(GL10.GL_BLEND);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
        Setting.save(game.getFileIO());
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
