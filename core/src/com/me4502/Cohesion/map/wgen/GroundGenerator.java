package com.me4502.Cohesion.map.wgen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.entities.Ground;
import com.me4502.Cohesion.map.Chunk;

public class GroundGenerator extends Generator {

	@Override
	public void generate(Chunk chunk) {

		for(int i = 0; i < Chunk.CHUNK_WIDTH; i += 16) {
			chunk.addEntity(new Ground(chunk.map, new Sprite(Cohesion.instance.ground), new Vector2(i, 32)));
		}
	}
}
