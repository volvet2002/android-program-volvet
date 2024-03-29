package com.volvet.superjumper;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.volvet.framework.GLScreen;
import com.volvet.framework.Game;
import com.volvet.framework.Camera2D;
import com.volvet.framework.OverlapTester;
import com.volvet.framework.SpriteBatcher;
import com.volvet.framework.Vector2;
import com.volvet.superjumper.World.WorldListener;
import com.volvet.framework.Rectangle;
import com.volvet.framework.Input.TouchEvent;
import com.volvet.framework.Input.KeyEvent;

public class GameScreen extends GLScreen {
	static final int GAME_READY = 0;
	static final int GAME_RUNNING = 1;
	static final int GAME_PAUSED = 2;
	static final int GAME_LEVEL_END = 3;
	static final int GAME_OVER = 4;
	
	int  state;
	Camera2D   guiCam;
	Vector2    touchPoint;
	SpriteBatcher   batcher;
	World      world;
	WorldListener    worldListener;
	WorldRender    renderer;
	Rectangle     pauseBounds;
	Rectangle     resumeBounds;
	Rectangle     quitBounds;
	int        lastScore;
	String     scoreString;	
	
	public GameScreen(Game game){
		super(game);
		state = GAME_READY;
		guiCam = new Camera2D(glGraphics, 320, 480);
		touchPoint = new Vector2();
		batcher = new SpriteBatcher(glGraphics, 1000);
		worldListener = new WorldListener() {
			@Override
			public void highJump(){
				Assets.playSound(Assets.highJumpSound);
			}
			@Override
			public void hit(){
				Assets.playSound(Assets.hitSound);
			}
			@Override
			public void jump(){
				Assets.playSound(Assets.jumpSound);
			}
			@Override
			public void coin(){
				Assets.playSound(Assets.coinSound);
			}
		};
		world = new World(worldListener);
		renderer = new WorldRender(glGraphics, batcher, world);
		pauseBounds = new Rectangle(320-64, 640-64, 64, 64);
		resumeBounds = new Rectangle(160-96, 240, 192, 36);
		quitBounds = new Rectangle(160-96, 240-36, 192, 36);
		lastScore = 0;
		scoreString = "socre: 0";
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		if( deltaTime > 0.1f ){
			deltaTime = 0;
		}		
		@SuppressWarnings("unused")
		List<KeyEvent>   keyEvents = game.getInput().getKeyEvent();		
		switch(state)
		{
		case GAME_READY:
			updateReady();
			break;
		case GAME_RUNNING:
			updateRunning(deltaTime);
			break;
		case GAME_PAUSED:		
			updatePaused();
			break;
		case GAME_LEVEL_END:
			updateLevelEnd();
			break;
		case GAME_OVER:
			updateGameOver();
			break;
		}
	}
	
	private void updateReady()
	{		
		List<TouchEvent> touchEvents = game.getInput().getTouchEvent();
		
		if( touchEvents.size() > 0 ){
			state = GAME_RUNNING;
		}
	}
	
	private void updateRunning(float deltaTime){
		List<TouchEvent> touchEvents = game.getInput().getTouchEvent();
		int size = touchEvents.size();
		
		for( int i=0;i<size;i++ ){
			TouchEvent event = touchEvents.get(i);
			if( event.type != TouchEvent.TOUCH_UP ){
				continue;
			}
			touchPoint.set(event.x, event.y);
			guiCam.touchToWorld(touchPoint);
			
			if( OverlapTester.pointInRectangle(touchPoint, pauseBounds) ){
				Assets.playSound(Assets.clickSound);
				state = GAME_PAUSED;
				return;
			}
		}	
		
		world.update(deltaTime, game.getInput().getAccelX());
		
		if( world.score != lastScore ){
			lastScore = world.score;
			scoreString = "" + lastScore;
		}
		if( world.state == World.WORLD_STATE_NEXT_LEVEL ){
			state = GAME_LEVEL_END;
		}
		if( world.state == World.WORLD_STATE_GAME_OVER ){
			state = GAME_OVER;
			if( lastScore > Setting.highScores[4] ){
				scoreString = "new high score: " + lastScore;
			} else {
				scoreString = "score: " + lastScore;
			}
			Setting.addScore(lastScore);
			Setting.save(game.getFileIO());
		}
	}
	
	private void updatePaused(){
		List<TouchEvent>  touchEvents = game.getInput().getTouchEvent();
		int size = touchEvents.size();
		for( int i=0;i<size;i++ ){
			TouchEvent event = touchEvents.get(i);
			if( event.type != TouchEvent.TOUCH_UP ){
				continue;
			}
			touchPoint.set(event.x, event.y);
			guiCam.touchToWorld(touchPoint);
			
			if( OverlapTester.pointInRectangle(touchPoint, resumeBounds)){
				Assets.playSound(Assets.clickSound);
				state = GAME_RUNNING;
				return;
			}
			
			if( OverlapTester.pointInRectangle(touchPoint, quitBounds)){
				Assets.playSound(Assets.clickSound);
				game.setScreen(new MainMenuScreen(game));
			}
		}
	}
	
	private void updateLevelEnd(){
	    List<TouchEvent> touchEvents = game.getInput().getTouchEvent();
	    int size = touchEvents.size();
	    for( int i=0;i<size;i++ ){
	    	TouchEvent event = touchEvents.get(i);
	    	if( event.type != TouchEvent.TOUCH_UP ){
	    		continue;
	    	}
	    	
	    	world = new World(worldListener);
	    	renderer = new WorldRender(glGraphics, batcher, world);
	    	state = GAME_READY;
	    }
	}
	
	private void updateGameOver(){
		List<TouchEvent> touchEvents = game.getInput().getTouchEvent();
	    int size = touchEvents.size();
	    for( int i=0;i<size;i++ ){
	    	TouchEvent event = touchEvents.get(i);
	    	if( event.type != TouchEvent.TOUCH_UP ){
	    		continue;
	    	}
	    	game.setScreen(new MainMenuScreen(game));
	    }
	}
	


	@Override
	public void present(float deltaTime) {
		// TODO Auto-generated method stub
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		renderer.render();
		
		guiCam.setViewportAndMatrices();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		batcher.beginBatch(Assets.items);
		switch(state)
		{
		case GAME_READY:
			presentReady();
			break;
		case GAME_RUNNING:
			presentRunning();
			break;
		case GAME_PAUSED:
			presentPaused();
			break;
		case GAME_LEVEL_END:
			presentLevelEnd();
			break;
		case GAME_OVER:
			presendLevelGameOver();
			break;
		}
		batcher.endBatch();
		gl.glDisable(GL10.GL_BLEND);

	}
	
	private void presentReady(){
		batcher.drawSprite(160, 240, 192, 32, Assets.ready);
	}
	
	private void presentRunning(){
		batcher.drawSprite(320-32, 480-32, 64, 64, Assets.pause);
		Assets.font.drawText(batcher, scoreString, 16, 480-20);
	}
	
	private void presentPaused(){
		batcher.drawSprite(160, 240, 192, 96, Assets.pauseMenu);
	    Assets.font.drawText(batcher, scoreString, 16, 480-20);
	}
	
	private void presentLevelEnd(){
		String topText = "the princess is ...";
	    String bottomText = "in another castle!";
	    float topWidth = Assets.font.glyphWidth * topText.length();
	    float bottomWidth = Assets.font.glyphWidth * bottomText.length();
	    Assets.font.drawText(batcher, topText, 160 - topWidth / 2, 480 - 40);
	    Assets.font.drawText(batcher, bottomText, 160 - bottomWidth / 2, 40);
	}
	
	private void presendLevelGameOver(){
		batcher.drawSprite(160, 240, 160, 96, Assets.gameOver);        
		float scoreWidth = Assets.font.glyphWidth * scoreString.length();
		Assets.font.drawText(batcher, scoreString, 160 - scoreWidth / 2, 480-20);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
        if( state == GAME_RUNNING ){
        	state = GAME_PAUSED;
        }
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
