package com.me4502.Cohesion.map.wgen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.map.Chunk;
import com.me4502.Cohesion.tile.Ground;

public class GroundGenerator implements Generator {

	@Override
	public void generate(Chunk chunk) {

		for(int i = 0; i < Chunk.CHUNK_WIDTH; i += TILE_WIDTH)
			chunk.addTile(new Ground(chunk.map, new Sprite(Cohesion.instance.ground), new Vector2(i, 32)));
	}
}
