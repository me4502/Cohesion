package com.me4502.Cohesion.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me4502.Cohesion.entities.Entity;

public class Chunk {

	public static final int CHUNK_WIDTH = 16 * 8;

	public int startingX;
	public MapInstance map;

	public Chunk(MapInstance map, int startingX) {
		this.map = map;
		this.startingX = startingX;
	}

	/**
	 * Spawns an entity with
	 * 
	 * @param ent
	 */
	public void addEntity(Entity ent) {

		ent.getPosition().add(startingX, 0);
		map.entities.add(ent);
	}

	public void render(SpriteBatch batch) {

	}
}
