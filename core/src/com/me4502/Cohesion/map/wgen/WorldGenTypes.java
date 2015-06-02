package com.me4502.Cohesion.map.wgen;

public enum WorldGenTypes {

	FLAT(0, new GroundGenerator()),
	GRATE(1, new GrateGenerator()),
	FLAT_PLATFORM(2, new FlatPlatformGenerator()),
	VARIABLE_PLATFORM(3, new VariablePlatformGenerator()),
	BLOCKADE_DROP(4, new BlockadeDropGenerator()),
	NEST(5, new NestGenerator());

	private int id;
	private Generator generator;

	WorldGenTypes(int id, Generator generator) {
		this.id = id;
		this.generator = generator;
	}

	public static Generator getGenerator(int id) {
		for(WorldGenTypes gen : values())
			if(id == gen.id)
				return gen.generator;
		return FLAT.generator;
	}
}
