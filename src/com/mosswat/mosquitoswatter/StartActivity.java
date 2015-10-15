package com.mosswat.mosquitoswatter;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.engine.Engine;
import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

import android.util.Log;

public class StartActivity extends SimpleBaseGameActivity implements IOnAreaTouchListener{
	// ================================================================
	// Constants
	// ================================================================
	private static final int CAMERA_WIDTH=800;
	private static final int CAMERA_HEIGHT=480;

	// ================================================================
	// VARIABLES
	// ================================================================
	
	private ITextureRegion mBackgroundTextureRegion, mMosquitoTextureRegion,mSwatterTextureRegion;
	private Sprite mMosquitoSprite,mSwatterSprite;
	private Scene thisScene;


	// ================================================================
	// CREATE ENGINE OPTIONS
	// ================================================================	
	@Override
	public EngineOptions onCreateEngineOptions() {

		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, 
		    new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
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
	protected void onCreateResources() {
		try {
		    // 1 - Set up bitmap textures
		    ITexture backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/background.png");
		        }
		    });
		    
		    ITexture mosquitoTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/mosquito.png");
		        }
		    });
		    

		    ITexture swatterTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		        @Override
		        public InputStream open() throws IOException {
		            return getAssets().open("gfx/swatter.png");
		            }
		    });
		    
		    
		    
		    // 2 - Load bitmap textures into VRAM
		    backgroundTexture.load();
		    mosquitoTexture.load();
		    
		 // 3 - Set up texture regions
		    this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
		    this.mMosquitoTextureRegion= TextureRegionFactory.extractFromTexture(mosquitoTexture);
		    this.mSwatterTextureRegion=TextureRegionFactory.extractFromTexture(swatterTexture);
		} catch (IOException e) {
		    Debug.e(e);
		}
		
		
		
		
	}
	
	//=============================================================
	//Create Scene
	//=============================================================
	@Override	
	protected Scene onCreateScene() {
		// 1 - Create new scene
		thisScene = new Scene();
		
		Sprite backgroundSprite = new Sprite(0, 0, this.mBackgroundTextureRegion, getVertexBufferObjectManager());
		thisScene.attachChild(backgroundSprite);
		
		// 2 - Add the mosquito
		for (int i=0; i<5;i++){
			generateMosquito();
		}
		
		
		return thisScene;
	}

	
	
	//==============================================================
	//onAreatouchListener
	//==============================================================

	@Override
	public boolean onAreaTouched(TouchEvent arg0, ITouchArea arg1, float arg2,
			float arg3) {
		mSwatterSprite=new Sprite(arg2,arg3,
					this.mSwatterTextureRegion, getVertexBufferObjectManager());
		
		thisScene.attachChild(mSwatterSprite);
		
		Log.d("touched","touched");
		
		
		return false;
	}
	
	
	
	public void generateMosquito(){
		mMosquitoSprite = new Sprite(MathUtils.random(1, CAMERA_WIDTH-100),MathUtils.random(1, CAMERA_HEIGHT-100), 
				this.mMosquitoTextureRegion, getVertexBufferObjectManager());

		thisScene.attachChild(mMosquitoSprite);
		thisScene.registerTouchArea(mMosquitoSprite);
	}
	




}
