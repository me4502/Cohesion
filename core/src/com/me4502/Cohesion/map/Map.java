package com.me4502.Cohesion.map;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

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

		MapInstance furtherest = null;
		float difference = -1;

		for(MapInstance instance : instances) {
			instance.update();
		}

		for(MapInstance instance : instances) {
			if(furtherest == null || instance.getDistanceFromStart() > furtherest.getDistanceFromStart()) {
				if(furtherest != null)
					difference = instance.getDistanceFromStart() - furtherest.getDistanceFromStart();
				furtherest = instance;
			} else if(furtherest != null && instance.getDistanceFromStart() - furtherest.getDistanceFromStart() > difference)
				difference = instance.getDistanceFromStart() - furtherest.getDistanceFromStart();
		}

		if(difference > 0)
			System.out.println(difference);

		if(furtherest != null && difference > 100 * 100) {
			instances.remove(furtherest);
		}
	}

	public Vector2 getCentrePoint() {

		Vector2 result = new Vector2(0f,0f);

		for(MapInstance instance : instances) {
			result.add(instance.player.getPosition().x, instance.player.getPosition().y);
		}

		result.scl(1f/instances.size());

		return result;
	}
}
