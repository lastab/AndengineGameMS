package com.mosswat.gameelements;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

import com.mosswat.mosquitoswatter.GamePlay;

public class LiveInsect extends AnimatedSprite{
	public final float SPEED_X=100.0f;
	public final float SPEED_Y=100.0f;
	
	private float oldmX=0,
					oldmY=0;
	float oldDistance,newDistance;
	float randomX,randomY;
	float currentSpeedX=SPEED_X,currentSpeedY=SPEED_Y;
	
	
	PhysicsHandler mPhysicsHandler;
	public LiveInsect(final float pX, final float pY, final TiledTextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);		
		this.mPhysicsHandler.setVelocity(SPEED_X,SPEED_Y);
		this.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier(new ScaleModifier(10, 1, 2))));
		//this.mPhysicsHandler.setAcceleration(0.0f, SPEED);
		
	}
	
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		oldDistance=(float)Math.sqrt( this.mX*this.mX-this.mY*this.mY);
		newDistance= (float) Math.sqrt(oldmX*oldmX-oldmY*oldmY);
		//changing direction
		if (oldDistance-newDistance >200 ||oldDistance-newDistance <-200){
			randomX=MathUtils.random(-2.0f, 2.0f);
			randomY=MathUtils.random(-2.0f, 2.0f);
			currentSpeedX=SPEED_X*randomX;
			currentSpeedY=SPEED_Y*randomY;
			
			oldmX=mX;
			oldmY=mY;
			
		}
		
		//checking boundary condition
		if(this.mX < 0||this.mX>GamePlay.CAMERA_WIDTH-50) {
				currentSpeedX=-1*currentSpeedX;
		}
		if(this.mY < 0||this.mY>GamePlay.CAMERA_HEIGHT-50) {
			currentSpeedY=-1*currentSpeedY;
		}
		
		
		
		this.mPhysicsHandler.setVelocity(currentSpeedX,currentSpeedY);
		/*if (this.mY>GamePlay.CAMERA_HEIGHT-50){
			this.mPhysicsHandler.setVelocity(0.0f,0.0f);
			this.mPhysicsHandler.setAcceleration(0.0f,0.0f);
		}*/
		super.onManagedUpdate(pSecondsElapsed);
	}
	

	
	
}
