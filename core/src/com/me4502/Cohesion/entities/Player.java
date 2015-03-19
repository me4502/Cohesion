package com.me4502.Cohesion.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.flowpowered.math.vector.Vector2d;
import com.me4502.Cohesion.map.MapInstance;

public class Player extends Entity {

	public Player(MapInstance map, Sprite sprite, Vector2d position) {
		super(map, sprite, position);
	}

	@Override
	public boolean hasGravity() {
		return true;
	}

	@Override
	public void update() {

		if(Gdx.input.isKeyPressed(Keys.A))
			velocity = velocity.add(onGround ? -1 : -0.1f, 0);
		if(Gdx.input.isKeyPressed(Keys.D))
			velocity = velocity.add(onGround ? 1 : 0.1f, 0);
		if(Gdx.input.isKeyPressed(Keys.SPACE) && onGround)
			velocity = velocity.add(0, 8);

		super.update();
	}
}
