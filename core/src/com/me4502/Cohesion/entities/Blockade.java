package com.me4502.Cohesion.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.map.MapInstance;

public class Blockade extends Entity {

	public Blockade(MapInstance map, Sprite sprite, Vector2 position, int scaleFactor) {
		super(map, sprite, position, scaleFactor);
	}

	@Override
	public boolean hasGravity() {
		return true;
	}
}
