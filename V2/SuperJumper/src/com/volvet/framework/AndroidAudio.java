package com.volvet.framework;

import   java.io.IOException;
import   android.app.Activity;
import   android.content.res.AssetFileDescriptor;
import   android.content.res.AssetManager;
import   android.media.AudioManager;
import   android.media.SoundPool;

public class AndroidAudio implements Audio {
	
	AssetManager    assets;
	SoundPool       soundPool;
	
	AndroidAudio(Activity  activity) {
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.assets = activity.getAssets();
		this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
	}

	@Override
	public Music newMusic(String fileName) {
		// TODO Auto-generated method stub
		try {
			AssetFileDescriptor assetDescriptor = assets.openFd(fileName);
			return new AndroidMusic(assetDescriptor);
		} catch(IOException e) {
			throw new RuntimeException("Couldn't load sound '" + fileName + "'");
		}		
	}

	@Override
	public Sound newSound(String fileName) {
		// TODO Auto-generated method stub
		try {
			AssetFileDescriptor assetDescriptor = assets.openFd(fileName);
			int soundId = soundPool.load(assetDescriptor, 0);
			return new AndroidSound(soundPool, soundId);
		} catch(IOException e) {
			throw new RuntimeException("Couldn't load sound '" + fileName + "'");
		}	
	}

}
