package com.me4502.Cohesion.map;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Map {

	List<MapInstance> instances = new ArrayList<MapInstance>();
	
	public Map() {

		instances.add(new MapInstance(Color.GREEN));
		instances.add(new MapInstance(Color.BLUE));
	}

	public void render(SpriteBatch batch) {

		for(MapInstance instance : instances)
			instance.render(batch);
	}

	public void update() {

		for(MapInstance instance : instances)
			instance.update();
	}
}
