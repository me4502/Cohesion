package com.me4502.Cohesion.map;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Map {

	List<MapInstance> instances = new ArrayList<MapInstance>();
	
	public Map() {

		instances.add(new MapInstance(new Color(1.0f, 0f, 0f, 0.33f)));
		instances.add(new MapInstance(new Color(0f, 1.0f, 0f, 0.33f)));
		instances.add(new MapInstance(new Color(0f, 0f, 1.0f, 0.33f)));
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
