package com.volvet.superjumper;

import java.util.List;
import javax.microedition.khronos.opengles.GL10;

import com.volvet.framework.GLScreen;
import com.volvet.framework.Game;
import com.volvet.framework.OverlapTester;
import com.volvet.framework.Texture;
import com.volvet.framework.TextureRegion;
import com.volvet.framework.Vector2;
import com.volvet.framework.Rectangle;
import com.volvet.framework.Camera2D;
import com.volvet.framework.SpriteBatcher;
import com.volvet.framework.Input.TouchEvent;

public class HighScoresScreen extends GLScreen {
	Camera2D    guiCam;
	SpriteBatcher   batcher;
	Rectangle   backBounds;
	Vector2     touchPoint;
	String[]    highScores;
	float       xOffset = 0;
	
	public HighScoresScreen(Game  game){
		super(game);
		
		guiCam = new Camera2D(glGraphics, 320, 480);
		batcher = new SpriteBatcher(glGraphics, 100);
		backBounds = new Rectangle(0, 0, 64, 64);
		touchPoint = new Vector2();
		highScores = new String[5];
		for( int i=0;i<5;i++ ){
			highScores[i] = (i+1) + ". " + Setting.highScores[i];
			xOffset = Math.max(highScores[i].length() + Assets.font.glyphWidth, xOffset);
		}
		xOffset = 160 - xOffset/2;
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
        game.getInput().getKeyEvent();
        
        List<TouchEvent> events = game.getInput().getTouchEvent();
        int len = events.size();
        for( int i=0;i<len;i++ ){
        	TouchEvent event = events.get(i);
        	touchPoint.set(event.x, event.y);
        	guiCam.touchToWorld(touchPoint);
        	
        	if( event.type == TouchEvent.TOUCH_UP ){
        		if( OverlapTester.pointInRectangle(touchPoint, backBounds)){
        			Assets.playSound(Assets.clickSound);
        			game.setScreen(new MainMenuScreen(game));
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
        
        batcher.drawSprite(160, 360, 300, 33, Assets.highScoresRegion);
        
        float y = 240;
        for( int i=4;i>=0;i-- ){
        	Assets.font.drawText(batcher, highScores[i], xOffset, y);
        	y += Assets.font.glyphHeight;
        }
        
        batcher.drawSprite(32, 32, 64, 64, Assets.arrow);        
        batcher.endBatch();
        
        gl.glDisable(GL10.GL_BLEND);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

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
