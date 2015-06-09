package com.me4502.Cohesion.map.wgen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.map.Chunk;
import com.me4502.Cohesion.tile.Platform;

public class FlatPlatformGenerator implements Generator {

	int tick;

	@Override
	public void generate(Chunk chunk) {

		tick = 0;

		for(int i = 0; i < Chunk.CHUNK_WIDTH; i += TILE_WIDTH) {

			tick ++;
			if(tick % 2 == 0) continue;

			chunk.addTile(new Platform(chunk.map, new Sprite(Cohesion.instance.platform), new Vector2(i, 64), Cohesion.TEXTURE_SIZE / 32));
		}
	}
}
