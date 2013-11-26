package com.volvet.superjumper;

import java.util.List;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

import com.volvet.framework.GLScreen;
import com.volvet.framework.Game;
import com.volvet.framework.Input.TouchEvent;
import com.volvet.framework.Camera2D;
import com.volvet.framework.SpriteBatcher;
import com.volvet.framework.Texture;
import com.volvet.framework.TextureRegion;
import com.volvet.framework.Rectangle;
import com.volvet.framework.Vector2;
import com.volvet.framework.OverlapTester;

public class HelpScreen extends GLScreen {
	Camera2D          guiCam;
	SpriteBatcher     batcher;
	Rectangle         nextBounds;
	Vector2           touchPoint;
	Texture           helpImage;
	TextureRegion     helpRegion;
	int               idx;
	ArrayList<String> helpPngs;
		
	public HelpScreen(Game game){
		super(game);
		guiCam = new Camera2D(glGraphics, 320, 480);
		nextBounds = new Rectangle(320-64, 0, 64, 64);
		touchPoint = new Vector2();
		batcher = new SpriteBatcher(glGraphics, 1);		
		idx = 0;
		helpPngs = new ArrayList<String>();
		
		helpPngs.add("help.png");
		helpPngs.add("help2.png");
		helpPngs.add("help3.png");
		helpPngs.add("help4.png");
		helpPngs.add("help5.png");
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
        glGame.getInput().getKeyEvent();
        List<TouchEvent> touchEvents = glGame.getInput().getTouchEvent();
        int  len = touchEvents.size();
        
        for( int i=0;i<len;i++ ){
            TouchEvent event = touchEvents.get(i);
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);
            
            if( event.type == TouchEvent.TOUCH_UP ){
            	if( OverlapTester.pointInRectangle(touchPoint, nextBounds)){
            		Assets.playSound(Assets.clickSound);
            		idx ++; 
            		if( idx >= 5 ){          		
            			idx = 0;
            			game.setScreen(new MainMenuScreen(glGame));
            		} else {
            			helpImage.dispose();
            			helpImage = new Texture(glGame, helpPngs.get(idx));
                        helpRegion = new TextureRegion(helpImage, 0, 0, 320, 480);
            		}
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
        
        batcher.beginBatch(helpImage);
        batcher.drawSprite(160, 240, 320, 480, helpRegion);
        batcher.endBatch();
        
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        batcher.beginBatch(Assets.items);
        batcher.drawSprite(320-32, 32, -64, 64, Assets.arrow);
        batcher.endBatch();
        
        gl.glDisable(GL10.GL_BLEND);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		helpImage.dispose();
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
        helpImage = new Texture(glGame, helpPngs.get(idx));
        helpRegion = new TextureRegion(helpImage, 0, 0, 320, 480);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
