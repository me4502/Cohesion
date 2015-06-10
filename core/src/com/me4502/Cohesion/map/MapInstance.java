package com.me4502.Cohesion.map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.entities.Entity;
import com.me4502.Cohesion.entities.agent.Agent;
import com.me4502.Cohesion.entities.player.Player;
import com.me4502.Cohesion.map.wgen.Generator;
import com.me4502.Cohesion.map.wgen.WorldGenTypes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class MapInstance {

	public List<Entity> entities = new ArrayList<>();

	Player player;

	Color color;

	Vector2 playerStartLocation;

	private Queue<Entity> spawningQueue = new ArrayDeque<>();

	private static final int CHUNK_LOADING_RANGE = 1;

	int chunkIndex = -1;
	private Chunk[] chunks = new Chunk[CHUNK_LOADING_RANGE];

	public MapInstance(Color color) {
		this.color = color;

		playerStartLocation = new Vector2(50, 100);
		entities.add(player = new Player(this, new Sprite(Cohesion.instance.player), new Vector2(50, 100), Cohesion.TEXTURE_SIZE / 32));
	}

	public void generateNext(int generatorId) {

		chunkIndex ++;
		if(chunkIndex >= chunks.length)
			chunks = Arrays.copyOf(chunks, chunks.length + CHUNK_LOADING_RANGE);

		chunks[chunkIndex] = new Chunk(this, chunkIndex * Chunk.CHUNK_WIDTH);

		Generator gen = WorldGenTypes.getGenerator(generatorId);
		gen.generate(chunks[chunkIndex]);
	}

	public List<Chunk> getChunks(Vector2 position) {

		List<Chunk> chunks = new ArrayList<>();

		for(Chunk chunk : this.chunks) {
			if(chunk == null) continue;

			if(chunk.startingX <= position.x && chunk.startingX + Chunk.CHUNK_WIDTH > position.x || chunk.startingX <= position.x+32 && chunk.startingX + Chunk.CHUNK_WIDTH > position.x+32)
				chunks.add(chunk);
		}
		return chunks;
	}

	List<Chunk> loadedChunkCache = new ArrayList<>();

	public List<Chunk> getLoadedChunks() {

		if(!loadedChunkCache.isEmpty())
			return loadedChunkCache;

		Vector2 position = Cohesion.instance.getMap().getCentrePoint();

		for(Chunk chunk : this.chunks) {
			if(chunk == null) continue;

			if(chunk.startingX <= position.x && chunk.startingX + Chunk.CHUNK_WIDTH > position.x
					|| chunk.startingX <= position.x - Chunk.CHUNK_WIDTH && chunk.startingX + Chunk.CHUNK_WIDTH > position.x - Chunk.CHUNK_WIDTH
					|| chunk.startingX <= position.x + Chunk.CHUNK_WIDTH && chunk.startingX + Chunk.CHUNK_WIDTH > position.x + Chunk.CHUNK_WIDTH)
				loadedChunkCache.add(chunk);
		}

		return loadedChunkCache;
	}

	public int randomRange(int min, int max) {

		return min + Cohesion.RANDOM.nextInt(max - min + 1);
	}

	public void render(SpriteBatch batch) {

		if(Cohesion.instance.colorize.isCompiled()) {
			batch.setShader(Cohesion.instance.colorize);
			Cohesion.instance.colorize.setUniformf("color", color.r, color.g, color.b, color.a);
		}

		getLoadedChunks().stream().forEach(chunk -> chunk.render(batch));

		for(Entity ent : entities)
			ent.render(batch);

		batch.setShader(Cohesion.instance.simple);
	}

	public void update() {

		loadedChunkCache.clear();

		while(!spawningQueue.isEmpty()) {
			Entity ent = spawningQueue.poll();
			if(ent instanceof Agent)
				((Agent) ent).initializeAI();
			entities.add(ent);
		}

		List<Chunk> loadedChunks = getLoadedChunks();

		loadedChunks.stream().forEach(Chunk::update);

		Iterator<Entity> iter = entities.iterator();
		while(iter.hasNext()) {
			Entity ent = iter.next();
			if(isPositionInChunks(ent.getPosition(), loadedChunks) || ent.getPosition().x <= 0) {
				ent.update();
				if (ent.shouldRemove())
					iter.remove();
			}
		}
	}

	public boolean isPositionInChunks(Vector2 position, List<Chunk> chunks) {
		for(Chunk chunk : chunks)
			if(chunk.contains(position))
				return true;
		return false;
	}

	public <T extends Entity> T spawnEntity(T entity) {

		spawningQueue.add(entity);
		return entity;
	}

	public float getDistanceFromStart() {

		return player.getPosition().dst2(playerStartLocation);
	}
}
