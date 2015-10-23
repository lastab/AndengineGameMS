package com.mosswat.mosquitoswatter;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.engine.Engine;
import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

import com.mosswat.gameelements.DeadInsect;
import com.mosswat.gameelements.LiveInsect;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class GamePlay extends SimpleBaseGameActivity implements IOnSceneTouchListener, IOnAreaTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int CAMERA_WIDTH = 800;
	public static final int CAMERA_HEIGHT = 480;
	// ===========================================================
	// Fields
	// ===========================================================

	public static int score=0;
	//public static int miss;
	
	
	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mMosquitoTextureRegion,mDeadMosquitoTextureRegion;
	private ITextureRegion mBackgroundTextureRegion;
	private LiveInsect snapmosquito;
	private DeadInsect spriteDeadMosquito;
	private Scene myscene;
	

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
		
	}	

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		// TODO Auto-generated method stub
		return  new FixedStepEngine (pEngineOptions, 60);
	}


	@Override
	public void onCreateResources() {
		try{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		ITexture backgroundTexture= new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
			    @Override
			    public InputStream open() throws IOException {
			        return getAssets().open("gfx/background.png");
			    }
			});
		
		ITexture deadMosquitoTexture= new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
		    @Override
		    public InputStream open() throws IOException {
		        return getAssets().open("gfx/deadmosquito.png");
		    }
		});
		

		this.mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(this.getTextureManager(), 512, 256, TextureOptions.NEAREST);
		
		
		this.mMosquitoTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "mosquitospritesheet.png", 4, 1);
		this.mDeadMosquitoTextureRegion= BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas,this,"deadmosquito.png",5,1);
				//TextureRegionFactory.extractFromTexture(deadMosquitoTexture);
		
		this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
			
		try {
			this.mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
			this.mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
		
		
		//load texture
		backgroundTexture.load();
		deadMosquitoTexture.load();
		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();		
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		Sprite backgroundSprite = new Sprite(0, 0, this.mBackgroundTextureRegion, getVertexBufferObjectManager());
		
		scene.attachChild(backgroundSprite);
	
	
		
		myscene=scene;
		myscene.setOnSceneTouchListener(this);
		myscene.setOnAreaTouchListener(this);
		generateMosquito(scene);
		generateMosquito(scene);
		
		
		

		

		return scene;

	}

	// ===========================================================
	// Methods
	// ===========================================================
	public void generateMosquito(Scene scene){
		snapmosquito = new LiveInsect(MathUtils.random(1, CAMERA_WIDTH-100),MathUtils.random(1, CAMERA_HEIGHT-100), this.mMosquitoTextureRegion, this.getVertexBufferObjectManager());
		snapmosquito.animate(50);
		
		
		
		scene.registerTouchArea(snapmosquito);
		scene.attachChild(snapmosquito);
	}

	@Override
	public boolean onSceneTouchEvent(Scene arg0, TouchEvent arg1) {
		/*if (arg1.getAction()==TouchEvent.ACTION_DOWN)
		{/* Snapmosquito. */		
			//generateMosquito(myscene);}
		return false;
	}

	@Override
	public boolean onAreaTouched(TouchEvent arg0, ITouchArea arg1, float arg2,
			float arg3) {
		
		if (arg0.getAction()==TouchEvent.ACTION_DOWN)
		{
			//delete mosquito
			this.myscene.unregisterTouchArea((AnimatedSprite)arg1);
			this.myscene.detachChild((AnimatedSprite)arg1);
			
			//show dead mosquito
			this.spriteDeadMosquito=new DeadInsect(arg0.getX()-61, arg0.getY()-50, mDeadMosquitoTextureRegion, getVertexBufferObjectManager());
			
			
			
			myscene.attachChild(spriteDeadMosquito);
			generateMosquito(myscene);
			
			
			//increase the score
			score++;
			
			//call garbage collection
			System.gc();
		}
		return false;
	}
	
	
	
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
