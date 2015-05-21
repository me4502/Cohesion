package com.me4502.Cohesion.tile;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.map.MapInstance;
import com.me4502.Cohesion.util.Bounds;
import com.me4502.Cohesion.util.Collidable;
import com.me4502.Cohesion.util.RectangularBounds;

public class Tile implements Collidable {

	Vector2 position;

	public Sprite sprite;

	Bounds bounds;

	public MapInstance map;

	public Tile(MapInstance map, Sprite sprite, Vector2 position) {

		this.map = map;
		this.position = position;
		bounds = new RectangularBounds(sprite.getTexture().getWidth(), sprite.getTexture().getHeight());

		sprite.setPosition(position.x, position.y);

		this.sprite = sprite;
	}

	@Override
	public Bounds getBoundingBox() {
		return bounds;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	@Override
	public Vector2 getPosition() {
		return position.cpy();
	}

	public void render(SpriteBatch batch) {
		sprite.draw(batch);
	}

	public void update() {

		sprite.setPosition(position.x, position.y);

		if(Cohesion.DEBUG) bounds.drawDebugBounds(position);
	}
}
