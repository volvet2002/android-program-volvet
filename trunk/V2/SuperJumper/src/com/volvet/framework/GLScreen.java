package com.volvet.framework;

public abstract class GLScreen extends Screen{
	protected final GLGame  glGame;
	protected final GLGraphics  glGraphics;
	
	public GLScreen(Game game) {
		super(game);
		this.glGame = (GLGame)game;
		this.glGraphics = this.glGame.getGLGraphics();
	}

}
