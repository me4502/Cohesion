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
	int score = 0;

	int updateTick = 0;

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

		if(isSlowed() && updateTick < 5) {
			return;
		}

		updateTick = 0;

		for(MapInstance instance : instances) {
			instance.update();
		}
		if(isAccelerated()) {
			for(int i = 0; i < 20; i++)
				for(MapInstance instance : instances) {
					instance.update();
				}
		}

		for(MapInstance instance : instances) {
			if(instance.player.getPosition().y < -128) {
				instances.remove(instance);
				break;
			}
		}

		if(instances.size() == 1)
			instances.clear();

		if(instances.isEmpty()) { //GameOver
			gameOver();
			return; //Do stuff.
		}

		if(instances.get(0).getChunks(getCentrePoint().add(Chunk.CHUNK_WIDTH, 0)).isEmpty()) {

			int gen = Cohesion.RANDOM.nextInt(WorldGenTypes.values().length);

			for(MapInstance map : instances) {
				map.generateNext(gen);
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
