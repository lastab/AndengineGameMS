package com.mosswat.mosquitoswatter;

import org.andengine.engine.Engine;
import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class StartActivity extends BaseGameActivity {
	// ================================================================
	// Constants
	// ================================================================
	private static final int WIDTH=800;
	private static final int HEIGHT=480;
	
	// ================================================================
	// VARIABLES
	// ================================================================
	private Camera mCamera;
	private Scene mScene;
	
	
	
	// ================================================================
	// CREATE ENGINE OPTIONS
	// ================================================================	
	@Override
	public EngineOptions onCreateEngineOptions() {
	
		//Create our game's camera (view)
		mCamera= new Camera(0,0,WIDTH,HEIGHT);
		
		/*Setup our engine options. Including resolution policy, screen 
		 *orientation and full screen settings.
		 */
		EngineOptions engineOptions=new EngineOptions(true,ScreenOrientation.LANDSCAPE_FIXED,
				new FillResolutionPolicy(), mCamera);
		
		//allow our engine to play sound and music
		engineOptions.getAudioOptions().setNeedsMusic(true);
		engineOptions.getAudioOptions().setNeedsSound(true);
		
		//do not allow our game to sleep while it's running
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		
		return engineOptions;
	}
	
	//=============================================================
	//Create Engine	
	//=============================================================
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		return new FixedStepEngine (pEngineOptions, 60);
	}

	//=============================================================
	//Create Resources
	//=============================================================
	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
		
	}
	
	
	//=============================================================
	//Create Scene
	//=============================================================
		@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback ) throws Exception {
		mScene=new Scene();
		pOnCreateSceneCallback.onCreateSceneFinished(mScene);
		
	}
		

	//=============================================================
	//Populate Scene
	//=============================================================		
	@Override
	public void onPopulateScene(Scene arg0, OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws Exception {
		pOnPopulateSceneCallback.onPopulateSceneFinished();
		
	}
}
