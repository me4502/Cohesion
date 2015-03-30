package com.me4502.Cohesion.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.map.MapInstance;

public class Projectile extends Entity {

	public boolean hasCollided;
	
	public Projectile(MapInstance map, Sprite sprite, Vector2 position) {
		super(map, sprite, position);
		
		collisionDrag = -0.3f;
		groundDrag = 0.99f;
	}

	@Override
	public boolean hasGravity() {
		return true;
	}

	public boolean doesHardCollide() {
		return false;
	}
	
	@Override
	public void update() {
		
		sprite.setRotation((float) Math.toDegrees(Math.atan2(velocity.y, velocity.x)));
		
		super.update();
	}
}
