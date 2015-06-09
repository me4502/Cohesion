package com.me4502.Cohesion.map.wgen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.map.Chunk;
import com.me4502.Cohesion.tile.Ground;

public class GrateGenerator implements Generator {

	//Do not tell Taylor Swift about this code.
	int blankSpaces = 0;

	@Override
	public void generate(Chunk chunk) {

		blankSpaces = 0;

		for(int i = 0; i < Chunk.CHUNK_WIDTH; i += TILE_WIDTH) {

			if(i > 0 && blankSpaces == 0 && Cohesion.RANDOM.nextFloat() >= 0.4f) {
				blankSpaces ++;
				continue;
			}

			blankSpaces = 0;

			chunk.addTile(new Ground(chunk.map, new Sprite(Cohesion.instance.ground), new Vector2(i, 32), Cohesion.TEXTURE_SIZE / 32));
		}
	}

}
