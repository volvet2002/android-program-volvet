package com.volvet.mrnom;

import com.volvet.framework.Graphics.PixmapFormat;
import com.volvet.framework.Screen;
import com.volvet.framework.Game;
import com.volvet.framework.Graphics;

public class LoadingScreen extends Screen {
	
	public LoadingScreen(Game  game){
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
        Graphics graphics = game.getGraphics();
        
        Assets.background = graphics.newPixmap("background.png", PixmapFormat.RGB565);
        Assets.logo = graphics.newPixmap("logo.png", PixmapFormat.ARGB4444);
        Assets.mainMenu = graphics.newPixmap("mainmenu.png", PixmapFormat.ARGB4444);
        Assets.buttons = graphics.newPixmap("buttons.png", PixmapFormat.ARGB4444);
        Assets.help1 = graphics.newPixmap("help1.png", PixmapFormat.ARGB4444);
        Assets.help2 = graphics.newPixmap("help2.png", PixmapFormat.ARGB4444);
        Assets.help3 = graphics.newPixmap("help3.png", PixmapFormat.ARGB4444);
        Assets.numbers = graphics.newPixmap("numbers.png", PixmapFormat.ARGB4444);
        Assets.ready = graphics.newPixmap("ready.png", PixmapFormat.ARGB4444);
        Assets.pause = graphics.newPixmap("pausemenu.png", PixmapFormat.ARGB4444);
        Assets.gameOver = graphics.newPixmap("gameover.png", PixmapFormat.ARGB4444);
        Assets.headUp = graphics.newPixmap("headup.png", PixmapFormat.ARGB4444);
        Assets.headLeft = graphics.newPixmap("headleft.png", PixmapFormat.ARGB4444);
        Assets.headDown = graphics.newPixmap("headdown.png", PixmapFormat.ARGB4444);
        Assets.headRight = graphics.newPixmap("headright.png", PixmapFormat.ARGB4444);
        Assets.tail = graphics.newPixmap("tail.png", PixmapFormat.ARGB4444);
        Assets.stain1 = graphics.newPixmap("stain1.png", PixmapFormat.ARGB4444);
        Assets.stain2 = graphics.newPixmap("stain2.png", PixmapFormat.ARGB4444);
        Assets.stain3 = graphics.newPixmap("stain3.png", PixmapFormat.ARGB4444);
        
        Assets.click = game.getAudio().newSound("click.ogg");
        Assets.eat = game.getAudio().newSound("eat.ogg");
        Assets.bitten = game.getAudio().newSound("bitten.ogg");
        
        Setting.load(game.getFileIO());
        // TODO:  set main menu screen.  
        game.setScreen(new MainMenuScreen(game));
    }

	@Override
	public void present(float deltaTime) {
		// TODO Auto-generated method stub

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
