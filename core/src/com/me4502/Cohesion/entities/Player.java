package com.me4502.Cohesion.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.map.MapInstance;

public class Player extends Entity {

	public Player(MapInstance map, Sprite sprite, Vector2 position) {
		super(map, sprite, position);
	}

	@Override
	public boolean hasGravity() {
		return true;
	}

	@Override
	public void update() {

		sprite.setRotation(sprite.getRotation() - velocity.x  *4);
		
		if(Gdx.input.isKeyPressed(Keys.A))
			velocity.add(onGround ? -1 : -0.2f, 0);
		if(Gdx.input.isKeyPressed(Keys.D))
			velocity.add(onGround ? 1 : 0.2f, 0);
		if(Gdx.input.isKeyPressed(Keys.SPACE) && onGround)
			velocity.add(0, 10);
		
		if(Gdx.input.isKeyPressed(Keys.W)) {
			Projectile proj = null;
			
			Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.input.getY());
			
			
			
			map.spawnEntity(proj = new Projectile(map, new Sprite(Cohesion.instance.projectile), getPosition()));
		
			proj.velocity.set(mouse.add(getPosition()).scl(0.5f));
		}
		
		super.update();
	}
}
