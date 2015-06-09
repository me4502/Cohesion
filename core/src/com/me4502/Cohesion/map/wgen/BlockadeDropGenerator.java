package com.me4502.Cohesion.map.wgen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.entities.Blockade;
import com.me4502.Cohesion.map.Chunk;
import com.me4502.Cohesion.tile.Platform;

public class BlockadeDropGenerator implements Generator {

	@Override
	public void generate(Chunk chunk) {

		for(int i = 0; i < Chunk.CHUNK_WIDTH; i += TILE_WIDTH)
			if(i / TILE_WIDTH == 0)
				chunk.addTile(new Platform(chunk.map, new Sprite(Cohesion.instance.platform), new Vector2(i, 72), Cohesion.TEXTURE_SIZE / 32));
			else if(i / TILE_WIDTH == 1)
				chunk.addTile(new Platform(chunk.map, new Sprite(Cohesion.instance.platform), new Vector2(i, 100), Cohesion.TEXTURE_SIZE / 32));
			else if(i / TILE_WIDTH > 2) {
				chunk.addTile(new Platform(chunk.map, new Sprite(Cohesion.instance.platform), new Vector2(i, 0), Cohesion.TEXTURE_SIZE / 32));
				chunk.addEntity(new Blockade(chunk.map, new Sprite(Cohesion.instance.blockade), new Vector2(TILE_WIDTH*3, 64 + i/TILE_WIDTH*64), Cohesion.TEXTURE_SIZE / 32));
			}
	}

}
