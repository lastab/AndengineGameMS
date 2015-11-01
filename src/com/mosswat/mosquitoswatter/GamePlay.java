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
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
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

import com.mosswat.gameelements.DeadInsect;
import com.mosswat.gameelements.LiveInsect;
import com.mosswat.gameelements.Moon;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich
 * @since 11:54:51 - 03.04.2010
 */
public class GamePlay extends SimpleBaseGameActivity implements  IOnAreaTouchListener {
	// ===========================================================
	// Constants
	// ===========================================================

	public static final int CAMERA_WIDTH = 800;
	public static final int CAMERA_HEIGHT = 480;
	// ===========================================================
	// Fields
	// ===========================================================

	public static int score=0;
	//	public static int combo=0;
	//public static int miss;


	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mMosquitoTextureRegion,mDeadMosquitoTextureRegion,mMoonTextureRegion;
	private ITextureRegion mBackgroundTextureRegion;
	private LiveInsect snapmosquito;
	private DeadInsect spriteDeadMosquito;
	private AnimatedSprite spriteMoon;
	private Scene myscene;

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
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
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
					return getAssets().open("gfx/background.png");
				}
			});

			ITexture deadMosquitoTexture= new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("gfx/deadmosquito.png");
				}
			});


			this.mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(this.getTextureManager(), 512, 512, TextureOptions.NEAREST);


			this.mMosquitoTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "mosquitospritesheet.png", 4, 1);
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

		final Scene scene = new Scene();		
		scene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		Sprite backgroundSprite = new Sprite(0, 0, this.mBackgroundTextureRegion, getVertexBufferObjectManager());



		scene.attachChild(backgroundSprite);

		myscene=scene;

		myscene.setOnAreaTouchListener(this);
		
		spriteMoon= new Moon(MathUtils.random(1, CAMERA_WIDTH-100),MathUtils.random(1, CAMERA_HEIGHT-100), this.mMoonTextureRegion, this.getVertexBufferObjectManager());
		scene.attachChild(spriteMoon);
		generateMosquito(scene);
		generateMosquito(scene);
		mMusic.play();
		mMosquitoWings.play();



		//text
		scoreText = new Text(CAMERA_WIDTH-200,10, this.mFont, "Score:", "Score: XXXXX".length(), this.getVertexBufferObjectManager());
		myscene.attachChild(scoreText);

		return scene;


	}
	
	
	

	@Override
	public void onResumeGame() {
		super.onResumeGame();

		if (mMosquitoWings!=null)
			this.mMosquitoWings.play();
		this.mMusic.play();
	}

	@Override
	public void onPauseGame() {
		super.onPauseGame();

		if (mMosquitoWings!=null)
			this.mMosquitoWings.pause();
		this.mMusic.pause();
	}



	// ===========================================================
	// Methods
	// ===========================================================
	public void generateMosquito(Scene scene){
		snapmosquito = new LiveInsect(MathUtils.random(1, CAMERA_WIDTH-100),MathUtils.random(1, CAMERA_HEIGHT-100), this.mMosquitoTextureRegion, this.getVertexBufferObjectManager());
		snapmosquito.setHeight(62);
		snapmosquito.setWidth(62);
		snapmosquito.animate(50);




		scene.registerTouchArea(snapmosquito);
		scene.attachChild(snapmosquito);


		

	}


	@Override
	public boolean onAreaTouched(TouchEvent arg0, ITouchArea arg1, float arg2,
			float arg3) {

		if (arg0.getAction()==TouchEvent.ACTION_DOWN)
		{
			AnimatedSprite mosquito;
			try{
				mosquito=(AnimatedSprite)arg1;
			}catch(Exception e){
		
				return false;
			}


			//delete mosquito

			this.myscene.unregisterTouchArea(mosquito);
			this.myscene.detachChild(mosquito);





			//show dead mosquito
			this.spriteDeadMosquito=new DeadInsect(arg0.getX()-61, arg0.getY()-50, mDeadMosquitoTextureRegion, getVertexBufferObjectManager());


			//Scaling the size of dead mosquito=size of live mosquito
			float height=mosquito.getHeightScaled();
			float width=mosquito.getWidthScaled();
			this.spriteDeadMosquito.setHeight(height);
			this.spriteDeadMosquito.setWidth(width);


			myscene.attachChild(spriteDeadMosquito);
			generateMosquito(myscene);

			//generate swatting sound
			this.mSwattingSound.play();

			//stop wing sound
			//this.mMosquitoWings.stop();

			score+=1;


			//display Score
			scoreText.setText("Score:"+score);

			//call garbage collection
			System.gc();
		}
		return false;
	}




	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
