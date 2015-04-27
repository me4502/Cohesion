package com.me4502.Cohesion.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.map.MapInstance;

public class Ground extends Entity {

	public Ground(MapInstance map, Sprite sprite, Vector2 position) {
		super(map, sprite, position);
	}

	@Override
	public boolean hasGravity() {
		return false;
	}

}
