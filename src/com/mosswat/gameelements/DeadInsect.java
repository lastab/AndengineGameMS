package com.mosswat.gameelements;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.mosswat.mosquitoswatter.GamePlay;

public class DeadInsect extends AnimatedSprite{

	public static final float SPEED=50.0f;
	PhysicsHandler mPhysicsHandler;
	public DeadInsect(final float pX, final float pY, final TiledTextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);
		
		this.mPhysicsHandler.setVelocity(0.0f, 100.0f);
		this.mPhysicsHandler.setAcceleration(0.0f, SPEED);
	}
	
	
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		if (this.mY>GamePlay.CAMERA_HEIGHT-55){
			this.mPhysicsHandler.setVelocity(0.0f,0.0f);
			this.mPhysicsHandler.setAcceleration(0.0f,0.0f);
			
			this.setCurrentTileIndex(2);
			
			this.clearUpdateHandlers();
		}
		super.onManagedUpdate(pSecondsElapsed);
	}
	
}
