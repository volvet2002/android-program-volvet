package com.volvet.framework;

import com.volvet.framework.Graphics.PixmapFormat;

public interface Pixmap {
	public int getWidth();
	
	public int getHeight();
	
	public PixmapFormat GetFormat();
	
	public void  dispose();
}
