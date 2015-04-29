package com.me4502.Cohesion.map.wgen;

import com.me4502.Cohesion.map.Chunk;

public abstract class Generator {

	public static final int TILE_WIDTH = 64;

	public abstract void generate(Chunk chunk);
}