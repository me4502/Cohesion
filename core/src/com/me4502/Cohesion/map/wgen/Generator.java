package com.me4502.Cohesion.map.wgen;

import com.me4502.Cohesion.map.Chunk;

@FunctionalInterface
public interface Generator {

	int TILE_WIDTH = 64;

	void generate(Chunk chunk);
}