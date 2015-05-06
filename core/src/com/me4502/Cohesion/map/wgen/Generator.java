package com.me4502.Cohesion.map.wgen;

import com.me4502.Cohesion.map.Chunk;

@FunctionalInterface
public interface Generator {

	public static final int TILE_WIDTH = 64;

	public void generate(Chunk chunk);
}