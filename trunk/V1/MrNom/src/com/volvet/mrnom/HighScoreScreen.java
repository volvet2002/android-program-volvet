package com.volvet.mrnom;

import java.util.List;

import com.volvet.framework.Screen;
import com.volvet.framework.Game;
import com.volvet.framework.Graphics;
import com.volvet.framework.Input.TouchEvent;

public class HighScoreScreen extends Screen {
	String  lines[] = new String[5];
	
	public HighScoreScreen(Game game){
		super(game);
		
		for( int i=0;i<5;i++ ){
			lines[i] = " " + (i+1) + "." + Setting.highScores[i];
		}
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		
        List<TouchEvent>  touchEvents = game.getInput().getTouchEvent();
        
        int size = touchEvents.size();
        for( int i=0;i<size;i++ ){
        	TouchEvent event = touchEvents.get(i);
        	if( event.type == TouchEvent.TOUCH_UP ){
        		if( event.x <64 && event.y > 415 ){
        			if( Setting.soundEnable ){
        				Assets.click.play(1);
        			}
        			game.setScreen(new MainMenuScreen(game));
        		}
        	}
        }
	}

	@Override
	public void present(float deltaTime) {
		// TODO Auto-generated method stub
        game.getGraphics().drawPixmap(Assets.background, 0, 0);
        game.getGraphics().drawPixmap(Assets.mainMenu, 64, 20, 0, 42, 196, 42);
        
        int y = 100;
        for( int i=0;i<lines.length;i++ ){
        	drawText(game.getGraphics(), lines[i], 20, y);
        	y += 50;
        }
        game.getGraphics().drawPixmap(Assets.buttons, 0, 416, 64, 64, 64, 64);
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

	private void drawText(Graphics g, String line, int x, int y) {
		int  len = line.length();
		
		for( int i=0;i<len;i++ ){
			char  c = line.charAt(i);
			
			if( c == ' ' ){
				x += 20;
				continue;
			}
			
			int srcX = 0;
			int srcWidth = 0;
			
			if( c == '.' ){
				srcX = 200;
				srcWidth = 10;
			} else {
				srcX = (c - '0') * 20;
				srcWidth = 20;
			}
			
			g.drawPixmap(Assets.numbers, x, y, srcX, 0, srcWidth, 32);
			x += srcWidth;
			
		}
	}
}
