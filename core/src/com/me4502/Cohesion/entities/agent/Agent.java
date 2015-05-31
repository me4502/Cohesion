package com.me4502.Cohesion.entities.agent;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.entities.Entity;
import com.me4502.Cohesion.entities.projectile.Projectile;
import com.me4502.Cohesion.entities.agent.ai.AIBase;
import com.me4502.Cohesion.map.MapInstance;
import com.me4502.Cohesion.util.DamageSource;

public abstract class Agent extends Entity {

	protected double health;

	public List<AIBase> aiBehaviour = new LinkedList<AIBase>();

	public Agent(MapInstance map, Sprite sprite, Vector2 position) {
		super(map, sprite, position);
	}

	public abstract void damage(DamageSource source);

	@Override
	public boolean shouldRemove() {
		return super.shouldRemove() || health < 0;
	}

	@Override
	public void update() {

		for(AIBase ai : aiBehaviour) {
			ai.update();
			if(ai.isBlocking()) break;
		}

		super.update();
	}

	@Override
	public boolean onCollision(Entity ent) {
		if(ent instanceof Projectile && ent.timeSinceHit > 5) {
			if(((Projectile) ent).getShooter().equals(this)) return false;
			velocity.sub(ent.velocity.cpy().scl(ent.collisionDrag));
			ent.timeSinceHit = 0;
			timeSinceHit = 0;
			damage(((Projectile) ent).getShooter());
			return true;
		}

		return super.onCollision(ent);
	}
}
