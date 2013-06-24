package com.volvet.mrnom;

import java.util.List;
import java.util.ArrayList;

import com.volvet.framework.Graphics;
import com.volvet.framework.Screen;
import com.volvet.framework.Game;
import com.volvet.framework.Pixmap;
import com.volvet.framework.Input.TouchEvent;

public class HelpScreen extends Screen {
	ArrayList<Pixmap>   helpPixmapList;
	int                 idx;
	
	public HelpScreen(Game game) {
		super(game);
		
		helpPixmapList = new ArrayList<Pixmap>();
		
		helpPixmapList.add(Assets.help1);
		helpPixmapList.add(Assets.help2);
		helpPixmapList.add(Assets.help3);
		
		idx = 0;
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		List<TouchEvent>  touchEvents = game.getInput().getTouchEvent();
		
		int size = touchEvents.size();
		for( int i=0;i<size;i++ ){
		    TouchEvent event = touchEvents.get(i);
		    
		    if( event.type == TouchEvent.TOUCH_UP ){
		    	if( event.x > 256 && event.y > 416 ){
		    		idx ++;  
		    		if( idx >= 3 ){
			    		game.setScreen(new MainMenuScreen(game));
			    	}
		    		if( Setting.soundEnable ) {
			    		Assets.click.play(1);
			    	}
		    		break;
		    	} 			    	
		    }
		}

	}

	@Override
	public void present(float deltaTime) {
		// TODO Auto-generated method stub
        Graphics graphics = game.getGraphics();
        
        graphics.drawPixmap(Assets.background, 0, 0);
        graphics.drawPixmap(helpPixmapList.get(idx), 64, 100);
        graphics.drawPixmap(Assets.buttons, 256,  412, 0, 64, 64, 64);
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
