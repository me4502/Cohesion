package com.me4502.Cohesion.map;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.Cohesion;

public class Map {

	List<MapInstance> instances = new ArrayList<MapInstance>();

	int currentX = 150;

	int lastTileBase;

	int lastScoreX = 0;
	int score = 0;

	public Map() {

		instances.add(new MapInstance(new Color(1.0f, 0f, 0f, 0.33f)));
		instances.add(new MapInstance(new Color(0f, 1.0f, 0f, 0.33f)));
		instances.add(new MapInstance(new Color(0f, 0f, 1.0f, 0.33f)));

		lastTileBase = 50;
	}

	public void render(SpriteBatch batch) {

		for(MapInstance instance : instances)
			instance.render(batch);
	}

	public void update() {

		MapInstance furtherest = null;
		float smallestDistance = Float.MAX_VALUE;

		for(MapInstance instance : instances) {
			instance.update();

			if(instance.getDistanceFromStart() < smallestDistance)
				smallestDistance = instance.getDistanceFromStart();
		}

		for(MapInstance instance : instances) {
			if(instance.getDistanceFromStart() > smallestDistance && instance.getDistanceFromStart() - smallestDistance > 800*800)
				furtherest = instance;
		}

		if(furtherest != null) {
			instances.remove(furtherest);
		}

		if(instances.size() == 1)
			instances.clear();

		if(instances.isEmpty()) { //GameOver
			gameOver();
			return; //Do stuff.
		}

		if(getCentrePoint().x > currentX - 500) {
			for(MapInstance map : instances) {
				map.generateNext(currentX, lastTileBase = lastTileBase-25);
			}

			currentX += 75;
		}

		float averageX = 0, averageY = 0;

		Vector2 pos = null;

		for(MapInstance instance : instances) {
			averageX += instance.player.getPosition().x + instance.player.sprite.getWidth()/2;
			averageY += instance.player.getPosition().y + instance.player.sprite.getHeight()/2;

			pos = instance.player.getPosition().add(instance.player.sprite.getWidth()/2, instance.player.sprite.getHeight()/2);
		}

		averageX /= instances.size();
		averageY /= instances.size();

		if(pos != null && pos.dst2(averageX, averageY) < 16*16 && (int)averageX > lastScoreX) {
			lastScoreX = (int)averageX;

			score += instances.size();
			System.out.println(score);
			if(score % 100 == 0) {
				//Generate new player.
				//TODO
			}
		}
	}

	public Vector2 getCentrePoint() {

		Vector2 result = new Vector2(0f,0f);

		for(MapInstance instance : instances) {
			result.add(instance.player.getPosition().x + instance.player.sprite.getWidth()/2, instance.player.getPosition().y + instance.player.sprite.getHeight()/2);
		}

		result.scl(1f/instances.size());

		return result;
	}

	public void gameOver() {

		Cohesion.instance.map = new Map();
	}
}
