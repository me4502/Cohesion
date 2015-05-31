package com.me4502.Cohesion.entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.entities.Entity;
import com.me4502.Cohesion.entities.projectile.Projectile;
import com.me4502.Cohesion.map.MapInstance;

public class Player extends Entity {

	int lastShootTime = 0;

	public Player(MapInstance map, Sprite sprite, Vector2 position) {
		super(map, sprite, position);
	}

	@Override
	public boolean hasGravity() {
		return true;
	}

	@Override
	public void update() {

		lastShootTime ++;

		sprite.setRotation(sprite.getRotation() - velocity.x  *4);

		float modifier = 1.0f;
		if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
			modifier = 2.0f;

		if(Gdx.input.isKeyPressed(Keys.A))
			velocity.add((onGround ? -1 : -0.2f) * modifier, 0);
		if(Gdx.input.isKeyPressed(Keys.D))
			velocity.add((onGround ? 1 : 0.2f) * modifier, 0);
		if((Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.SPACE)) && onGround)
			velocity.add(0, 10);

		if(Gdx.input.isKeyPressed(Keys.S) && lastShootTime > 30) {
			Projectile proj = null;

			Vector3 mouse = Cohesion.instance.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 1));

			Vector2 flatMouse = new Vector2(mouse.x, mouse.y);

			map.spawnEntity(proj = new Projectile(map, new Sprite(Cohesion.instance.projectile), getPosition().add(sprite.getWidth()/2, sprite.getHeight()/2), this));

			proj.velocity.set(flatMouse.sub(getPosition().add(sprite.getWidth()/2, sprite.getHeight()/2)).scl(0.25f));
			lastShootTime = 0;
		}

		super.update();
	}

	@Override
	public boolean doesHardCollide() {
		return false;
	}
}