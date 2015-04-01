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
		if(ent instanceof Projectile && !((Projectile) ent).hasCollided) {
			velocity.sub(ent.velocity.cpy().scl(ent.collisionDrag));
			((Projectile)ent).hasCollided = true;
		}
	}

	@Override
	public boolean hasGravity() {
		return true;
	}
}
