package com.volvet.framework;


import java.io.IOException;
import java.io.InputStream;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

public class AndroidGraphics implements Graphics {
	AssetManager  assets;
	Bitmap        frameBuffer;
	Canvas        canvas;
	Paint         paint;
	Rect          srcRect = new Rect();
	Rect          dstRect = new Rect();
	
	public AndroidGraphics(AssetManager assets, Bitmap frameBuffer){
	    this.assets = assets;
	    this.frameBuffer = frameBuffer;
	    this.canvas = new Canvas(this.frameBuffer);
	    this.paint = new Paint();
	}

	@Override
	public Pixmap newPixmap(String fileName, PixmapFormat format) {
		// TODO Auto-generated method stub
		Config  config = null;
		if( format == PixmapFormat.RGB565 ){
		    config = Config.RGB_565;	
		} else if( format == PixmapFormat.ARGB8888 ){
			config = Config.ARGB_8888;
		} else {
			config = Config.ARGB_4444;
		}
		
		Options options = new Options();
		options.inPreferredConfig = config;
		
		InputStream inputStream = null;
		Bitmap   bitmap = null;
		
		try {
			inputStream = assets.open(fileName);
			bitmap = BitmapFactory.decodeStream(inputStream);
			if( bitmap == null ){
				throw new RuntimeException("Could't load bitmap from asset " + fileName );
			}
		} catch(IOException e){
			throw new RuntimeException("Could't load bitmap from asset " + fileName );
		}
		
		finally {
			if( inputStream != null ){
				try{
					inputStream.close();
				} catch(IOException e) {
					
				}
			}
		}
		
		if( bitmap.getConfig() == Config.ARGB_8888 ){
			format = PixmapFormat.ARGB8888;
		} else if( bitmap.getConfig() == Config.ARGB_4444 ){
			format = PixmapFormat.ARGB4444;
		} else {
			format = PixmapFormat.RGB565;
		}
		
		return new AndroidPixmap(bitmap, format);
	}

	@Override
	public void clear(int color) {
		// TODO Auto-generated method stub
        canvas.drawRGB((color&0xff0000)>>16, (color&0xff00)>>8, color&0xff);
	}

	@Override
	public void drawPixel(int x, int y, int color) {
		// TODO Auto-generated method stub
        paint.setColor(color);
        canvas.drawPoint(x,  y, paint);
	}

	@Override
	public void drawLine(int x, int y, int x1, int y1, int color) {
		// TODO Auto-generated method stub
        paint.setColor(color);
        canvas.drawLine(x, y, x1, y1, paint);
	}

	@Override
	public void drawRect(int x, int y, int width, int height, int color) {
		// TODO Auto-generated method stub
        paint.setColor(color);
        canvas.drawRect(x, y, x+width-1, y+height-1, paint);
	}

	@Override
	public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY,
			int srcWidth, int srcHeight) {
		// TODO Auto-generated method stub
        srcRect.left = srcX;
        srcRect.top = srcY;
        srcRect.right = srcX + srcWidth - 1;
        srcRect.bottom = srcY + srcHeight - 1;
        
        dstRect.left = x;
        dstRect.top = y;
        dstRect.right = x + srcWidth - 1;
        dstRect.bottom = y + srcHeight - 1;
        
        canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, srcRect, dstRect, null);
	}

	@Override
	public void drawPixmap(Pixmap pixmap, int x, int y) {
		// TODO Auto-generated method stub
        canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, x, y, null);
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return frameBuffer.getWidth();
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return frameBuffer.getHeight();
	}

}
