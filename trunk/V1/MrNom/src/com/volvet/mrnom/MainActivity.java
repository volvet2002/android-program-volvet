package com.volvet.mrnom;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import com.volvet.framework.AndroidGame;
import com.volvet.framework.Screen;

public class MainActivity extends AndroidGame {
    public Screen getStartScreen() {
    	return new LoadingScreen(this);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    }
	
}
