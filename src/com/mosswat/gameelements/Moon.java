package com.mosswat.gameelements;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

import com.mosswat.mosquitoswatter.GamePlay;

public class Moon extends AnimatedSprite{

	private final int p0x=0,
			p1x=GamePlay.CAMERA_WIDTH/2,
			p2x=GamePlay.CAMERA_WIDTH,
			p0y=GamePlay.CAMERA_HEIGHT,
			p1y=0,
			p2y=GamePlay.CAMERA_HEIGHT;
	
	
	int x,y;
	
	PhysicsHandler mPhysicsHandler;
	private int gravestoneNo;
	
	public Moon(final float pX, final float pY, final TiledTextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		
		
		
		x=p0x;
		y=p0y;
		
		
		
		this.mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);
		
		//this.mPhysicsHandler.setVelocity( 100.0f,0.0f);
		
		
	}
	

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		
		
		//check if the dead mosquito has touched the edge of the screen or not
		/*if (this.mY>GamePlay.CAMERA_HEIGHT-this.getHeight()){
			this.mPhysicsHandler.setVelocity(0.0f,0.0f);
			this.mPhysicsHandler.setAcceleration(0.0f,0.0f);
			
			this.setCurrentTileIndex(gravestoneNo);
			
			//dont know why I did this
			this.clearUpdateHandlers();
		}*/
		
		//check if moon has reached its end
		//if (this.mX>GamePlay.CAMERA_WIDTH-10)
		//{
			this.mPhysicsHandler.setVelocity(0.0f,0.0f);
			this.clearUpdateHandlers();
		//}
		
		super.onManagedUpdate(pSecondsElapsed);
	}
	
}
