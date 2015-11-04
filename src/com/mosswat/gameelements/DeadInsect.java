package com.mosswat.gameelements;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

import com.badlogic.gdx.math.Vector2;
import com.mosswat.mosquitoswatter.GamePlay;

public class DeadInsect extends AnimatedSprite{

	//public static float accleration=50.0f;
	 public static float speedX,speedY; 
	PhysicsHandler mPhysicsHandler;
	private int gravestoneNo;
	
	public DeadInsect(final float pX, final float pY, final TiledTextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);
		
		//this.mPhysicsHandler.setVelocity(0.0f, 100.0f);
		//this.mPhysicsHandler.setAcceleration(0.0f, ACCLERATION);
		
		//randomize the gravestone
		gravestoneNo=MathUtils.random(1, 4);
	}
	

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		
		
		//check if the dead mosquito has touched the edge of the screen or not
		if (this.mY>GamePlay.CAMERA_HEIGHT-this.getHeight()){
			this.mPhysicsHandler.setVelocity(0.0f,0.0f);
			this.mPhysicsHandler.setAcceleration(0.0f,0.0f);
			
			this.setCurrentTileIndex(gravestoneNo);
			
			//dont know why I did this
			this.clearUpdateHandlers();
		}
		else
			this.mPhysicsHandler.setVelocity(speedX*40,speedY*40);
		super.onManagedUpdate(pSecondsElapsed);
	}
	
}
