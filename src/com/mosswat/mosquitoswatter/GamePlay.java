package com.mosswat.mosquitoswatter;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.FixedStepEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
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

import android.graphics.Color;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.util.Log;
import android.view.KeyEvent;

import com.mosswat.gameelements.DeadInsect;
import com.mosswat.gameelements.LiveButterfly;
import com.mosswat.gameelements.LiveMosquito;
import com.mosswat.gameelements.Moon;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class GamePlay extends SimpleBaseGameActivity implements  IOnAreaTouchListener ,IOnMenuItemClickListener,IAccelerationListener{
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int CAMERA_WIDTH = 800;
	public static final int CAMERA_HEIGHT = 480;

	protected static final int MENU_RESET = 0;
	protected static final int MENU_QUIT = MENU_RESET + 1;

	// ===========================================================
	// Fields
	// ===========================================================


	protected MenuScene mMenuScene;

	private BitmapTextureAtlas mMenuTexture;
	protected ITextureRegion mMenuResetTextureRegion;
	protected ITextureRegion mMenuQuitTextureRegion;





	public static int score=0;
	//	public static int combo=0;
	//public static int miss;

	private Camera camera;

	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mMosquitoTextureRegion,mButterflyTextureRegion,mDeadMosquitoTextureRegion,mMoonTextureRegion;
	private ITextureRegion mBackgroundTextureRegion;
	private LiveMosquito snapmosquito;
	private LiveButterfly snapbutterfly;
	private DeadInsect spriteDeadMosquito;
	private Moon spriteMoon;
	private Scene mMainScene;

	private Sound mSwattingSound;
	private Music mMusic, mMosquitoWings;
	private Font mFont;
	public Text scoreText; 

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================backgroundSprite
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final ZoomCamera mcamera = new ZoomCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);

		//sound needed
		engineOptions.getAudioOptions().setNeedsSound(true);
		//need music
		engineOptions.getAudioOptions().setNeedsMusic(true);

		return engineOptions;


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
					return getAssets().open("gfx/background.jpg");
				}
			});

			ITexture deadMosquitoTexture= new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("gfx/deadmosquito.png");
				}
			});


			this.mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(this.getTextureManager(), 812, 812, TextureOptions.NEAREST);

			this.mButterflyTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "butterflysprite.png", 4, 2);
			this.mMosquitoTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "mosquitosprite.png", 4, 1);
			this.mDeadMosquitoTextureRegion= BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas,this,"deadmosquito.png",5,1);
			this.mMoonTextureRegion= BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas,this,"moon.png",1,1);
			//TextureRegionFactory.extractFromTexture(deadMosquitoTexture);

			this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);

			try {
				this.mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
				this.mBitmapTextureAtlas.load();
			} catch (TextureAtlasBuilderException e) {
				Debug.e(e);
			}


			this.mMenuTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
			this.mMenuResetTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mMenuTexture, this, "menu_reset.png", 0, 0);
			this.mMenuQuitTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mMenuTexture, this, "menu_quit.png", 0, 50);
			this.mMenuTexture.load();

			//load texture
			backgroundTexture.load();
			deadMosquitoTexture.load();


			//fonts
			final ITexture fontTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
			this.mFont = new Font(this.getFontManager(), fontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 40, true, Color.WHITE);
			this.mFont.load();

		} catch (IOException e1) {			
			e1.printStackTrace();
		}


		//=============================================================================================================
		//sounds
		//=============================================================================================================

		SoundFactory.setAssetBasePath("mfx/");
		//swatting sound
		try {
			this.mSwattingSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "swatting.ogg");

		} catch (final IOException e) {
			Debug.e(e);
		}




		//=============================================================================================================
		//Music
		//=============================================================================================================
		MusicFactory.setAssetBasePath("mfx/");
		try {
			this.mMusic = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "music.ogg");
			this.mMusic.setLooping(true);
		} catch (final IOException e) {
			Debug.e(e);
		}
		//wings sound
		try {
			this.mMosquitoWings= MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(),this, "fly.ogg");
			this.mMosquitoWings.setLooping(true);
		} catch (final IOException e) {
			Debug.e(e);
		}

	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.createMenuScene();



		final Scene scene = new Scene();		
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		Sprite backgroundSprite = new Sprite(0, 0, this.mBackgroundTextureRegion, getVertexBufferObjectManager());


		scene.attachChild(backgroundSprite);
		mMainScene=scene;

		mMainScene.setOnAreaTouchListener(this);

		spriteMoon= new Moon(MathUtils.random(1, CAMERA_WIDTH-100),MathUtils.random(1, CAMERA_HEIGHT-100), this.mMoonTextureRegion, this.getVertexBufferObjectManager());
		//scene.attachChild(spriteMoon);
		generateMosquito(scene);
		generateMosquito(scene);
		//generateButterfly(scene);
		mMusic.play();
		mMosquitoWings.play();


		//text
		scoreText = new Text(CAMERA_WIDTH-200,10, this.mFont, "Score:", "Score: XXXXXXXXXXXXX".length(), this.getVertexBufferObjectManager());
		mMainScene.attachChild(scoreText);


		//camera.setCenter(CAMERA_WIDTH, CAMERA_HEIGHT/2);
		return scene;


	}




	@Override
	public void onResumeGame() {
		super.onResumeGame();

		this.enableAccelerationSensor(this);
		if (mMosquitoWings!=null)
			this.mMosquitoWings.play();
		this.mMusic.play();
	}

	@Override
	public void onPauseGame() {
		super.onPauseGame();

		this.disableAccelerationSensor();
		if (mMosquitoWings!=null)
			this.mMosquitoWings.pause();
		this.mMusic.pause();
	}



	// ===========================================================
	// Methods
	// ===========================================================
	public void generateMosquito(Scene scene){
		snapmosquito = new LiveMosquito(MathUtils.random(1, CAMERA_WIDTH-100),MathUtils.random(1, CAMERA_HEIGHT-100), this.mMosquitoTextureRegion, this.getVertexBufferObjectManager());
		snapmosquito.setHeight(62);
		snapmosquito.setWidth(62);
		snapmosquito.animate(40);




		scene.registerTouchArea(snapmosquito);
		scene.attachChild(snapmosquito);
		this.enableAccelerationSensor(this);




	}
	
	public void generateButterfly(Scene scene){
		snapbutterfly= new LiveButterfly(MathUtils.random(1, CAMERA_WIDTH-100),MathUtils.random(1, CAMERA_HEIGHT-100), this.mButterflyTextureRegion, this.getVertexBufferObjectManager());
		snapbutterfly.setHeight(62);
		snapbutterfly.setWidth(62);
		snapbutterfly.animate(40);




		scene.registerTouchArea(snapbutterfly);
		scene.attachChild(snapbutterfly);
		this.enableAccelerationSensor(this);




	}



	@Override
	public boolean onAreaTouched(TouchEvent arg0, ITouchArea arg1, float arg2,
			float arg3) {

		if (arg0.getAction()==TouchEvent.ACTION_DOWN)
		{
			AnimatedSprite mosquito;
			try{
				mosquito=(LiveMosquito)arg1;
				//delete mosquito

				this.mMainScene.unregisterTouchArea(mosquito);
				this.mMainScene.detachChild(mosquito);





				//show dead mosquito
				this.spriteDeadMosquito=new DeadInsect(arg0.getX()-61, arg0.getY()-50, mDeadMosquitoTextureRegion, getVertexBufferObjectManager());


				//Scaling the size of dead mosquito=size of live mosquito
				float height=mosquito.getHeightScaled();
				float width=mosquito.getWidthScaled();
				this.spriteDeadMosquito.setHeight(height);
				this.spriteDeadMosquito.setWidth(width);


				mMainScene.attachChild(spriteDeadMosquito);
				generateMosquito(mMainScene);
				

				//generate swatting sound
				this.mSwattingSound.play();

				//stop wing sound
				//this.mMosquitoWings.stop();

				score+=1;


				//display Score
				scoreText.setText("Kills:"+score);

				//if kills is divisible by 10 add new mosquito
				if (score%10==0)
					generateMosquito(mMainScene);

				//call garbage collection
				System.gc();

			}catch(Exception e){
				try{
					mosquito=(LiveButterfly)arg1;
					//delete mosquito

					this.mMainScene.unregisterTouchArea(mosquito);
					this.mMainScene.detachChild(mosquito);





					//show dead mosquito
					//this.spriteDeadMosquito=new DeadInsect(arg0.getX()-61, arg0.getY()-50, mDeadMosquitoTextureRegion, getVertexBufferObjectManager());


					//Scaling the size of dead mosquito=size of live mosquito
					//float height=mosquito.getHeightScaled();
					//float width=mosquito.getWidthScaled();
					//this.spriteDeadMosquito.setHeight(height);
					//this.spriteDeadMosquito.setWidth(width);


					//mMainScene.attachChild(spriteDeadMosquito);
					//generateMosquito(mMainScene);

					//generate swatting sound
					this.mSwattingSound.play();

					//stop wing sound
					//this.mMosquitoWings.stop();

					score-=1;


					//display Score
					scoreText.setText("Kills:"+score);

					//if kills is divisible by 10 add new mosquito
					//if (score%10==0)
						//generateMosquito(mMainScene);

					//call garbage collection
					System.gc();


				}catch(Exception e2){


					return false;}
			}


		}
		return false;
	}

	@Override
	public boolean onMenuItemClicked(MenuScene arg0, IMenuItem pMenuItem,
			float arg2, float arg3) {

		switch(pMenuItem.getID()) {
		case MENU_RESET:
			/* Restart the animation. */
			this.mMainScene.reset();
			/* Remove the menu and reset it. */
			this.mMainScene.clearChildScene();
			this.mMenuScene.reset();
			if (mMosquitoWings!=null)
				this.mMosquitoWings.play();
			this.mMusic.play();
			return true;
		case MENU_QUIT:
			/* End Activity. */
			this.finish();
			return true;
		default:
			return false;
		}



	}

	@Override
	public void onAccelerationAccuracyChanged(AccelerationData arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAccelerationChanged(AccelerationData arg0) {
		if (arg0.getY()>1){
			LiveMosquito.rotationAngle=((float) -Math.atan(arg0.getX()/arg0.getY())*180*7/22);
			DeadInsect.speedX=arg0.getX();
			DeadInsect.speedY=arg0.getY();
			Moon.rotationAngle=LiveMosquito.rotationAngle;
			//	scoreText.setText( arg0.getX()+"\n"+arg0.getY());
		}

	}




	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if(pKeyCode == KeyEvent.KEYCODE_BACK && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if(this.mMainScene.hasChildScene()) {
				/* Remove the menu and reset it. */
				this.mMenuScene.back();
				if (mMosquitoWings!=null)
					this.mMosquitoWings.play();
				this.mMusic.play();

			} else {
				/* Attach the menu. */
				this.mMainScene.setChildScene(this.mMenuScene, false, true, true);
				if (mMosquitoWings!=null)
					this.mMosquitoWings.pause();
				this.mMusic.pause();

			}
			return true;
		} else {
			return super.onKeyDown(pKeyCode, pEvent);
		}
	}







	//menu scene
	protected void createMenuScene() {
		this.mMenuScene = new MenuScene(this.camera);

		final SpriteMenuItem resetMenuItem = new SpriteMenuItem(MENU_RESET, this.mMenuResetTextureRegion, this.getVertexBufferObjectManager());
		resetMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.mMenuScene.addMenuItem(resetMenuItem);

		final SpriteMenuItem quitMenuItem = new SpriteMenuItem(MENU_QUIT, this.mMenuQuitTextureRegion, this.getVertexBufferObjectManager());
		quitMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		this.mMenuScene.addMenuItem(quitMenuItem);

		this.mMenuScene.buildAnimations();

		this.mMenuScene.setBackgroundEnabled(false);

		this.mMenuScene.setOnMenuItemClickListener(this);
	}





	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
