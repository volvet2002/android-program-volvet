package com.volvet.mrnom;

import com.volvet.framework.Graphics;
import com.volvet.framework.Screen;
import com.volvet.framework.Game;
import com.volvet.framework.Input.TouchEvent;

import java.util.List;


public class MainMenuScreen extends Screen {
    public MainMenuScreen(Game game) {
    	super(game);
    }
	
	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
        Graphics graphics = game.getGraphics();
        
        List<TouchEvent> touchEvents = game.getInput().getTouchEvent();
        int size = touchEvents.size();
        for( int i=0;i<size;i++ ){
        	TouchEvent event = touchEvents.get(i);
        	if( event.type == TouchEvent.TOUCH_UP ){
        		if( inBounds(event, 0, graphics.getHeight() - 64, 64, 64)){
        			Setting.soundEnable = !Setting.soundEnable;
        			if( Setting.soundEnable ){
        				Assets.click.play(1);
        			}
        		}
        		if( inBounds(event, 64, 220, 192, 42)){
        			game.setScreen(new GameScreen(game));
        			if( Setting.soundEnable ){
        		    	Assets.click.play(1);
        		    }        			
        		}
        		if( inBounds(event, 64, 220+42, 192, 42)){
        		    game.setScreen(new HighScoreScreen(game));
        		    if( Setting.soundEnable ){
        		    	Assets.click.play(1);
        		    }
        		}
        		if( inBounds(event, 64, 220+84, 192, 42)){
        			game.setScreen(new HelpScreen(game));
        			if( Setting.soundEnable ){
        				Assets.click.play(1);
        			}
        		}
        	}        	
        }
	}

	@Override
	public void present(float deltaTime) {
		// TODO Auto-generated method stub
        Graphics  graphics = game.getGraphics();
        
        graphics.drawPixmap(Assets.background, 0, 0);
        graphics.drawPixmap(Assets.logo, 32, 32);
        graphics.drawPixmap(Assets.mainMenu, 64, 220);
        
        if( Setting.soundEnable ){
            graphics.drawPixmap(Assets.buttons, 0, 416, 0, 0, 64, 64);
        } else {
        	graphics.drawPixmap(Assets.buttons, 0, 416, 64, 0, 64, 64);        	
        }
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
	
	private boolean inBounds(TouchEvent  event, int x, int y, int width, int height){
		if( event.x > x && event.x < x + width - 1 && 
				event.y > y && event.y < y + height - 1){
			return true;
		} else {
			return false;
		}
	}

}
