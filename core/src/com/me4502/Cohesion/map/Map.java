package com.me4502.Cohesion.map;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.map.wgen.WorldGenTypes;

public class Map {

	List<MapInstance> instances = new ArrayList<MapInstance>();

	int currentX = 150;

	int lastScoreX = 0;
	public int score = 0;

	int updateTick = 0;

	Vector2 centrePoint = new Vector2(0,0);

	public Map() {

		instances.add(new MapInstance(new Color(1.0f, 0f, 0f, 0.33f)));
		instances.add(new MapInstance(new Color(0f, 1.0f, 0f, 0.33f)));
		instances.add(new MapInstance(new Color(0f, 0f, 1.0f, 0.33f)));

		for(MapInstance instance : instances)
			instance.generateNext(0);
	}

	public void render(SpriteBatch batch) {

		for(MapInstance instance : instances)
			instance.render(batch);
	}

	public boolean isSlowed() {
		return Gdx.input.isKeyPressed(Keys.SHIFT_LEFT);
	}

	public boolean isAccelerated() {
		return Gdx.input.isKeyPressed(Keys.CONTROL_LEFT);
	}

	public void update() {

		updateTick ++;

		if(isSlowed() && updateTick < 3)
			return;

		centrePoint.set(0, 0);

		for(MapInstance instance : instances)
			centrePoint.add(instance.player.getPosition().x + instance.player.sprite.getWidth()/2, instance.player.getPosition().y + instance.player.sprite.getHeight()/2);

		centrePoint.scl(1f/instances.size());

		updateTick = 0;

		for(MapInstance instance : instances)
			instance.update();
		if(isAccelerated())
			for(MapInstance instance : instances)
				instance.update();

		for(MapInstance instance : instances)
			if(instance.player.getPosition().y < -128) {
				instances.remove(instance);
				break;
			}

		if(instances.size() == 1)
			instances.clear();

		if(instances.isEmpty()) { //GameOver
			gameOver();
			return; //Do stuff.
		}

		if(instances.get(0).getChunks(getCentrePoint().add(Chunk.CHUNK_WIDTH, 0)).isEmpty()) {

			int gen = Cohesion.RANDOM.nextInt(WorldGenTypes.values().length);

			for(MapInstance map : instances)
				map.generateNext(gen);

			currentX += 75;
		}

		Vector2 pos = instances.get(0).player.getPosition();

		if(pos != null && pos.dst2(getCentrePoint()) < 32*32 && (int)pos.x > lastScoreX) {
			lastScoreX = (int)pos.x;

			score += instances.size();
			System.out.println(score);
			if(score % 100 == 0) {
				//Generate new player.
				//TODO
			}
		}
	}

	public Vector2 getCentrePoint() {

		return centrePoint.cpy();
	}

	public void gameOver() {

		Cohesion.instance.lastFrame = null;
		Cohesion.instance.map = new Map();
	}
}
