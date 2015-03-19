package com.me4502.Cohesion.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.flowpowered.math.vector.Vector2d;
import com.me4502.Cohesion.map.MapInstance;

public class Platform extends Entity {

	public Platform(MapInstance map, Sprite sprite, Vector2d position) {
		super(map, sprite, position);
	}

	@Override
	public boolean hasGravity() {
		return false;
	}

}
