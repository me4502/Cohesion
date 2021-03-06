package com.me4502.Cohesion.map.wgen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.map.Chunk;
import com.me4502.Cohesion.screens.GameScreen;
import com.me4502.Cohesion.tile.Platform;

public class VariablePlatformGenerator implements Generator {

	private static final int START_Y = 32;
	private static final int VARIANCE = 64;

	@Override
	public void generate(Chunk chunk) {
		int lastY = START_Y;
		int tick = 0;

		for(int i = 0; i < Chunk.CHUNK_WIDTH; i += TILE_WIDTH) {

			tick++;
			if(tick % 2 == 0) continue;

			lastY = lastY + VARIANCE/2-Cohesion.RANDOM.nextInt(VARIANCE);

			if(lastY < START_Y) lastY = START_Y;

			chunk.addTile(new Platform(chunk.map, new Sprite(GameScreen.platform), new Vector2(i, lastY), Cohesion.TEXTURE_SIZE / 32));
		}

	}
}
