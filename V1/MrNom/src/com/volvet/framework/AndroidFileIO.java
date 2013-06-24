package com.volvet.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.preference.PreferenceManager;

public class AndroidFileIO implements FileIO {	
	AssetManager   assets;
	String         externalStoragePath;

	public  AndroidFileIO(AssetManager   assets) {	    
	    this.assets = assets;
	    this.externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath() + 
	    		File.separator;
	}
	
	@Override
	public InputStream readAsset(String fileName) throws IOException {
		// TODO Auto-generated method stub
		return assets.open(fileName);
	}

	@Override
	public InputStream readFile(String fileName) throws IOException {
		// TODO Auto-generated method stub
		return new FileInputStream(externalStoragePath + fileName);
	}

	@Override
	public OutputStream writeFile(String fileName) throws IOException {
		// TODO Auto-generated method stub
		return new FileOutputStream(externalStoragePath + fileName);
	}
	
}
