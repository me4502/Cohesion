package com.me4502.Cohesion.map;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.entities.Entity;
import com.me4502.Cohesion.entities.Platform;
import com.me4502.Cohesion.entities.Player;
import com.me4502.Cohesion.map.wgen.Generator;
import com.me4502.Cohesion.map.wgen.WorldGenTypes;

public class MapInstance {

	public List<Entity> entities = new ArrayList<Entity>();

	Player player;

	Color color;

	Vector2 playerStartLocation;

	private Queue<Entity> spawningQueue = new ArrayDeque<Entity>();

	private static final int CHUNK_LOADING_RANGE = 8;

	int chunkIndex = -1;
	private Chunk[] chunks = new Chunk[CHUNK_LOADING_RANGE];

	public MapInstance(Color color) {
		this.color = color;

		playerStartLocation = new Vector2(50, 100);
		entities.add(player = new Player(this, new Sprite(Cohesion.instance.player), new Vector2(50, 100)));

		entities.add(new Platform(this, new Sprite(Cohesion.instance.platform), new Vector2(50,50)));
	}

	public void generateNext(int generatorId) {

		chunkIndex ++;
		if(chunkIndex >= chunks.length)
			chunks = Arrays.copyOf(chunks, chunks.length + CHUNK_LOADING_RANGE);

		chunks[chunkIndex] = new Chunk(this, chunkIndex * Chunk.CHUNK_WIDTH);

		Generator gen = WorldGenTypes.getGenerator(generatorId);
		gen.generate(chunks[chunkIndex]);
	}

	public int randomRange(int min, int max) {

		return min + Cohesion.RANDOM.nextInt(max - min + 1);
	}

	public void render(SpriteBatch batch) {

		if(Cohesion.instance.colorize.isCompiled()) {
			batch.setShader(Cohesion.instance.colorize);
			Cohesion.instance.colorize.setUniformf("color", color.r, color.g, color.b, color.a);
		}

		for(Entity ent : entities)
			ent.render(batch);

		batch.setShader(Cohesion.instance.simple);
	}

	public void update() {

		while(!spawningQueue.isEmpty())
			entities.add(spawningQueue.poll());

		for(Entity ent : entities)
			ent.update();

		Iterator<Entity> iter = entities.iterator();
		while(iter.hasNext()) {
			Entity ent = iter.next();
			if(ent.shouldRemove())
				iter.remove();
		}
	}

	public void spawnEntity(Entity entity) {

		spawningQueue.add(entity);
	}

	public float getDistanceFromStart() {

		return player.getPosition().dst2(playerStartLocation);
	}
}
