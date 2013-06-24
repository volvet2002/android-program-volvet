package com.volvet.mrnom;

import java.util.List;
import android.graphics.Color;

import com.volvet.framework.Screen;
import com.volvet.framework.Game;
import com.volvet.framework.Input.TouchEvent;
import com.volvet.framework.Pixmap;
import com.volvet.framework.Graphics;


public class GameScreen extends Screen {
	enum GameState {
		Ready,
		Running, 
		Pause,
		GameOver
	}
	
	GameState state = GameState.Ready;
	World     world;
	int       oldScore = 0;
	String    score = "0";
	
	public GameScreen(Game game) {
		super(game);
		
		world = new World();
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
        List<TouchEvent> touchEvents = game.getInput().getTouchEvent();
        if( state == GameState.Ready ){
        	updateReady(touchEvents, deltaTime);
        } else if( state == GameState.Running ){
        	updateRunning(touchEvents, deltaTime);
        } else if( state == GameState.Pause ){ 
        	updatePause(touchEvents, deltaTime);
        } else if( state == GameState.GameOver ){
        	updateGameOver(touchEvents, deltaTime);
        }
	}

	@Override
	public void present(float deltaTime) {
		// TODO Auto-generated method stub
        Graphics graphics = game.getGraphics(); 
        
        graphics.drawPixmap(Assets.background, 0, 0);        
        drawWorld(world);
        if(state == GameState.Ready) 
            drawReadyUI();
        if(state == GameState.Running)
            drawRunningUI();
        if(state == GameState.Pause)
            drawPausedUI();
        if(state == GameState.GameOver)
            drawGameOverUI();
        
        drawText(graphics, score, graphics.getWidth() / 2 - score.length()*20 / 2, graphics.getHeight() - 42);       
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
        if( state == GameState.Running ){
        	state = GameState.Pause;
        }
        if( state == GameState.GameOver ){
        	Setting.addScore(world.score);
        	Setting.save(game.getFileIO());
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
	
	private void updateReady(List<TouchEvent> touchEvents, float deltaTime) {
		if( touchEvents.size() > 0){
			state = GameState.Running;
		}
	}
	
	private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) {
		int size = touchEvents.size();
		
		for( int i=0;i<size;i++ ){
			TouchEvent event = touchEvents.get(i);
			
			if( event.type == TouchEvent.TOUCH_UP ) {
				if( event.x < 64 && event.y < 64 ){
					if( Setting.soundEnable ){
						Assets.click.play(1);
					}
					state = GameState.Pause;
					return;
				}
			}
			if( event.type == TouchEvent.TOUCH_DOWN ){
			    if( event.x < 64 && event.y > 416 ){
				    world.snake.turnLeft();
			    }
			    if( event.x > 256 && event.y > 416 ){
			    	world.snake.turnRight();
			    }
			}			
		}
		
		world.update(deltaTime);
		
		if( world.gameOver ){
			if( Setting.soundEnable ){
				Assets.bitten.play(1);
			}
			state = GameState.GameOver;
		}
		if( oldScore != world.score ){
			oldScore = world.score;
			score = "" + oldScore;
			if( Setting.soundEnable ){
				Assets.eat.play(1);
			}				
		}
	}
	
	private void updatePause(List<TouchEvent> touchEvents, float delatTime) {
		int size = touchEvents.size();
		
		for( int i=0;i<size;i++ ){
			TouchEvent event = touchEvents.get(i);
			if( event.type == TouchEvent.TOUCH_UP ){
				if( event.x > 80 && event.x <= 240 ){
					if( event.y > 100 && event.y <= 148 ){
						if( Setting.soundEnable ){
							Assets.click.play(1);
						}
						state = GameState.Running;
						return;
					} else if( event.y > 148 && event.y <= 196 ){
						if( Setting.soundEnable ){
							Assets.click.play(1);
						}
						game.setScreen(new MainMenuScreen(game));
						return;
					}
				}
			}
		}
	}
	
	private void updateGameOver(List<TouchEvent> touchEvents, float deltaTime) {
		int size = touchEvents.size();
		
		for( int i=0;i<size;i++ ){
			TouchEvent event = touchEvents.get(i);
			
			if( event.type == TouchEvent.TOUCH_UP ){
				if( event.x >=120 && event.x <=192 && event.y >=200 && event.y <= 264 ){
					if( Setting.soundEnable ){
						Assets.click.play(1);
					}
					game.setScreen(new MainMenuScreen(game));
				}
			}
		}
	}
	
	private void drawWorld(World world) {
		Graphics g = game.getGraphics();
		Snake snake = world.snake;
		SnakePart head = snake.parts.get(0);
		Stain   stain = world.stain;
		
		Pixmap  stainPixmap = null;
		
		if( stain.type == Stain.TYPE_1 ){
			stainPixmap = Assets.stain1;
		} else if( stain.type == Stain.TYPE_2 ){
			stainPixmap = Assets.stain2;
		} else {
			stainPixmap = Assets.stain3;
		}
		g.drawPixmap(stainPixmap, stain.x * 32, stain.y * 32);
		
		int size = snake.parts.size();		
		for( int i=1;i<size;i++ ){
			SnakePart part = snake.parts.get(i);
			g.drawPixmap(Assets.tail, part.x * 32, part.y * 32);
		}
		Pixmap  snakeHead = null;
		if( snake.direction == snake.UP ){
			snakeHead = Assets.headUp;
		} else if( snake.direction == snake.LEFT ){
			snakeHead = Assets.headLeft;
		} else if( snake.direction == snake.DOWN ){
			snakeHead = Assets.headDown;
		} else {
			snakeHead = Assets.headRight;
		}
		
		int x = head.x * 32 + 16;
        int y = head.y * 32 + 16;
        g.drawPixmap(snakeHead, x - snakeHead.getWidth() / 2, y - snakeHead.getHeight() / 2);
	}
	
	private void drawReadyUI() {
        Graphics g = game.getGraphics();
        
        g.drawPixmap(Assets.ready, 47, 100);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
    }
    
    private void drawRunningUI() {
        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.buttons, 0, 0, 64, 128, 64, 64);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
        g.drawPixmap(Assets.buttons, 0, 416, 64, 64, 64, 64);
        g.drawPixmap(Assets.buttons, 256, 416, 0, 64, 64, 64);
    }
    
    private void drawPausedUI() {
        Graphics g = game.getGraphics();
        
        g.drawPixmap(Assets.pause, 80, 100);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
    }

    private void drawGameOverUI() {
        Graphics g = game.getGraphics();
        
        g.drawPixmap(Assets.gameOver, 62, 100);
        g.drawPixmap(Assets.buttons, 128, 200, 0, 128, 64, 64);
        g.drawLine(0, 416, 480, 416, Color.BLACK);
    }
    
    public void drawText(Graphics g, String line, int x, int y) {
        int len = line.length();
        for (int i = 0; i < len; i++) {
            char character = line.charAt(i);

            if (character == ' ') {
                x += 20;
                continue;
            }

            int srcX = 0;
            int srcWidth = 0;
            if (character == '.') {
                srcX = 200;
                srcWidth = 10;
            } else {
                srcX = (character - '0') * 20;
                srcWidth = 20;
            }

            g.drawPixmap(Assets.numbers, x, y, srcX, 0, srcWidth, 32);
            x += srcWidth;
        }
    }

}
