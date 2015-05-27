package com.me4502.Cohesion.entities.projectile;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.entities.Entity;
import com.me4502.Cohesion.map.MapInstance;

public class Projectile extends Entity {

	Entity shooter;

	public Projectile(MapInstance map, Sprite sprite, Vector2 position, Entity shooter) {
		super(map, sprite, position);

        this.shooter = shooter;
        collisionDrag = -0.3f;
		groundDrag = 0.8f;
	}

    public Entity getShooter() {
        return shooter;
    }

	@Override
	public boolean hasGravity() {
		return true;
	}

	@Override
	public boolean doesHardCollide() {
		return false;
	}

	@Override
	public boolean shouldRemove() {
		return super.shouldRemove() || age > 100;
	}

	@Override
	public void update() {
		sprite.setRotation((float) Math.toDegrees(Math.atan2(velocity.y, velocity.x)));

		super.update();
	}
}
