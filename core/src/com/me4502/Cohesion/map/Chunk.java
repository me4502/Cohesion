package com.me4502.Cohesion.map;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.tile.Tile;

public class Chunk {

	public static final int CHUNK_WIDTH = 64 * 8;

	public int startingX;
	public MapInstance map;

	private List<Tile> tiles;

	public Chunk(MapInstance map, int startingX) {
		this.map = map;
		this.startingX = startingX;

		tiles = new ArrayList<Tile>();
	}

	public void addTile(Tile tile) {
		tile.setPosition(tile.getPosition().add(startingX, 0));
		tiles.add(tile);
	}

	public List<Tile> getTiles() {
		return tiles;
	}

	public void render(SpriteBatch batch) {

		for(Tile tile : tiles)
			tile.render(batch);
	}

	public void update() {
		for(Tile tile : tiles)
			tile.update();

		//drawChunkGrid();
	}

	public void drawChunkGrid() {
		Cohesion.instance.shapes.setProjectionMatrix(Cohesion.instance.camera.combined);
		Cohesion.instance.shapes.setColor(Color.WHITE);
		Cohesion.instance.shapes.begin(ShapeType.Line);
		Cohesion.instance.shapes.rect(startingX, 0, CHUNK_WIDTH, 1000);
		Cohesion.instance.shapes.end();
	}
}
