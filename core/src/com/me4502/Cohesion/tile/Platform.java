package com.me4502.Cohesion.tile;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.map.MapInstance;

public class Platform extends Tile {

	public Platform(MapInstance map, Sprite sprite, Vector2 position, int scaleFactor) {
		super(map, sprite, position, scaleFactor);
	}

}
