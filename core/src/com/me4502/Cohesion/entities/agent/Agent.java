package com.me4502.Cohesion.entities.agent;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.entities.Entity;
import com.me4502.Cohesion.entities.Projectile;
import com.me4502.Cohesion.map.MapInstance;

public abstract class Agent extends Entity {

	protected double health;

	public Agent(MapInstance map, Sprite sprite, Vector2 position) {
		super(map, sprite, position);
	}

	public abstract void damage();

	@Override
	public void onCollision(Entity ent) {
		if(ent instanceof Projectile && ent.timeSinceHit > 5) {
			velocity.sub(ent.velocity.cpy().scl(ent.collisionDrag));
			ent.timeSinceHit = 0;
			timeSinceHit = 0;
			damage();
		}
	}
}
