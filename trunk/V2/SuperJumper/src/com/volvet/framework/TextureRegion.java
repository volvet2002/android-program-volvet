package com.volvet.framework;

public class TextureRegion {
	public float u1, u2;
	public float v1, v2;
	
	public TextureRegion(Texture texture, float x, float y, float width, float height){
		u1 = x / texture.width;
		v1 = y / texture.height;
		u2 = u1 + width/texture.width;
		v2 = v1 + height/texture.height;
	}

}
