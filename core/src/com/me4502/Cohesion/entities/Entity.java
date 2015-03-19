package com.me4502.Cohesion.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.flowpowered.math.vector.Vector2d;
import com.flowpowered.math.vector.Vector2f;
import com.me4502.Cohesion.map.MapInstance;
import com.me4502.Cohesion.util.Bounds;
import com.me4502.Cohesion.util.RectangularBounds;

public abstract class Entity {

	public static final Vector2f GRAVITY = new Vector2f(0, 3);

	Vector2d position;
	Vector2f velocity;

	Sprite sprite;

	Bounds bounds;

	MapInstance map;

	boolean onGround;

	public Entity(MapInstance map, Sprite sprite, Vector2d position) {

		this.map = map;
		this.sprite = sprite;

		bounds = new RectangularBounds(sprite.getTexture().getWidth(), sprite.getTexture().getHeight());

		this.position = position;
		velocity = new Vector2f(0,0);
	}

	public Bounds getBoundingBox() {
		return bounds;
	}

	public void setPosition(Vector2d position) {
		move(position);
	}

	public Vector2d getPosition() {
		return position;
	}

	public void render(SpriteBatch batch) {
		sprite.draw(batch);
	}

	public void update() {

		onGround = doesIntersect(getPosition().sub(0, 3));

		if(hasGravity() && !onGround && velocity.getY() > -9) //Only apply gravity if we aren't on the ground
			velocity = velocity.sub(GRAVITY);

		if(velocity.lengthSquared() > 0) {
			int tries = 0;
			while(!move(getPosition().add(velocity.toDouble())) && tries < 3) {
				velocity = velocity.mul(new Vector2f(0.01f,0.01f));
				tries ++;
			}
			if(onGround && tries >= 3) velocity = new Vector2f(velocity.getX(), 0);
		}

		velocity = velocity.mul(new Vector2f(0.9f,0.9f));

		sprite.setPosition((float)position.getX(), (float)position.getY());

		bounds.drawDebugBounds(position);
	}

	protected boolean doesIntersect(Vector2d position) {

		for(Entity ent : map.entities) {
			if(ent == this) continue;
			if(getBoundingBox().doesIntersect(position, ent))
				return true;
		}

		return false;
	}

	protected boolean move(Vector2d position) {

		if(doesIntersect(position)) return false;
		this.position = position;
		return true;
	}

	public abstract boolean hasGravity();
}