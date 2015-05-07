package com.me4502.Cohesion.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.map.MapInstance;

public class Blockade extends Entity {

	public Blockade(MapInstance map, Sprite sprite, Vector2 position) {
		super(map, sprite, position);
	}

	@Override
	public void onCollision(Entity ent) {
		if(ent instanceof Projectile && ent.timeSinceHit > 5) {
			velocity.sub(ent.velocity.cpy().scl(ent.collisionDrag));
			ent.timeSinceHit = 0;
			timeSinceHit = 0;
		}
	}

	@Override
	public boolean hasGravity() {
		return true;
	}
}
