package com.me4502.Cohesion.map.wgen;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.entities.agent.Flyer;
import com.me4502.Cohesion.map.Chunk;
import com.me4502.Cohesion.screens.GameScreen;
import com.me4502.Cohesion.tile.Platform;

public class NestGenerator implements Generator {

    @Override
    public void generate(Chunk chunk) {
        for(int i = 0; i < Chunk.CHUNK_WIDTH; i += TILE_WIDTH) {
            if(i == 0 || i == Chunk.CHUNK_WIDTH - TILE_WIDTH)
                chunk.addTile(new Platform(chunk.map, new Sprite(GameScreen.platform), new Vector2(i, 50), Cohesion.TEXTURE_SIZE / 32));
            if(i == TILE_WIDTH*2 || i == Chunk.CHUNK_WIDTH - (TILE_WIDTH*3))
                chunk.addTile(new Platform(chunk.map, new Sprite(GameScreen.platform), new Vector2(i, 80), Cohesion.TEXTURE_SIZE / 32));
        }

        chunk.addEntity(new Flyer(chunk.map, new Sprite(GameScreen.flyer), new Vector2(Chunk.CHUNK_WIDTH/2, 200), Cohesion.TEXTURE_SIZE / 32));
    }
}
