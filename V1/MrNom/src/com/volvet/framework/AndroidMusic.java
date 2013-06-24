package com.volvet.framework;

import java.io.IOException;
import  android.content.res.AssetFileDescriptor;
import  android.media.MediaPlayer;
import  android.media.MediaPlayer.OnCompletionListener;

public class AndroidMusic implements Music, OnCompletionListener {

	MediaPlayer   mediaPlayer;
	boolean       isPrepared = false;
	
	AndroidMusic(AssetFileDescriptor  assetFileDescriptor) {
	    mediaPlayer = new MediaPlayer();
	    
	    try {
	    	mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor());
	    	mediaPlayer.prepare();
	    	
	    	isPrepared = true;
	    	mediaPlayer.setOnCompletionListener(this);
	    } catch(Exception e) {
	    	throw new RuntimeException("Couldn't load music");
	    }
	}
	
	@Override
	public void play() {
		// TODO Auto-generated method stub      
		if( mediaPlayer.isPlaying() ){
			return;
		}
		try {
			synchronized(this) {
				if( !isPrepared ){
					mediaPlayer.prepare();
					isPrepared = true;
					mediaPlayer.start();
				}
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
        mediaPlayer.stop();
        synchronized(this) {
        	isPrepared = false;
        }
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
        if( mediaPlayer.isPlaying() ){
        	mediaPlayer.pause();
        }
	}

	@Override
	public void setLooping(boolean looping) {
		// TODO Auto-generated method stub
        mediaPlayer.setLooping(looping);
	}

	@Override
	public void setVolume(float volume) {
		// TODO Auto-generated method stub
        mediaPlayer.setVolume(volume, volume);
	}

	@Override
	public boolean isPlaying() {
		// TODO Auto-generated method stub
		return mediaPlayer.isPlaying();
	}

	@Override
	public boolean isLooping() {
		// TODO Auto-generated method stub
		return mediaPlayer.isLooping();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
        if( mediaPlayer.isPlaying() ){
        	mediaPlayer.stop();
        }
        mediaPlayer.release();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		synchronized(this) {
			isPrepared = false;
		}
	}

}
