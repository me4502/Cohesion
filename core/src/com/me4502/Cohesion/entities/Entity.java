package com.me4502.Cohesion.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.map.MapInstance;
import com.me4502.Cohesion.util.Bounds;
import com.me4502.Cohesion.util.RectangularBounds;

public abstract class Entity {

	public static final Vector2 GRAVITY = new Vector2(0, 1.9f);

	public static int COLLISION_TRY_COUNT = 10;

	Vector2 position;
	Vector2 velocity;

	Sprite sprite;

	Bounds bounds;

	public MapInstance map;

	boolean onGround;

	public Entity(MapInstance map, Sprite sprite, Vector2 position) {

		this.map = map;
		this.sprite = sprite;

		bounds = new RectangularBounds(sprite.getTexture().getWidth(), sprite.getTexture().getHeight());

		this.position = position;
		velocity = new Vector2(0,0);
	}

	public Bounds getBoundingBox() {
		return bounds;
	}

	public void setPosition(Vector2 position) {
		move(position);
	}

	public Vector2 getPosition() {
		return position.cpy();
	}

	public void render(SpriteBatch batch) {
		sprite.draw(batch);
	}

	public void update() {

		onGround = doesIntersect(getPosition().sub(0, 2));

		if(hasGravity() && !onGround && velocity.y > -9) //Only apply gravity if we aren't on the ground
			velocity.sub(GRAVITY);

		if(velocity.len2() > 0) {
			int tries = 0;
			Vector2 oldVel = velocity.cpy();
			while(!move(getPosition().add(velocity)) && tries < COLLISION_TRY_COUNT) {
				velocity.scl(new Vector2(1f,0.01f));
				tries ++;
			}
			if(tries == COLLISION_TRY_COUNT) {
				velocity = oldVel;
				tries = 0;
				while(!move(getPosition().add(velocity)) && tries < COLLISION_TRY_COUNT) {
					velocity.scl(new Vector2(0.01f,1f));
					tries ++;
				}
				if(tries == COLLISION_TRY_COUNT) {
					velocity = oldVel;
					tries = 0;
					while(!move(getPosition().add(velocity)) && tries < COLLISION_TRY_COUNT) {
						velocity.scl(new Vector2(0.01f,0.01f));
						tries ++;
					}

					if(onGround && tries == COLLISION_TRY_COUNT) velocity.y = 0;
				}
			}
		}

		velocity.scl(new Vector2(onGround ? 0.8f : 0.99f, onGround ? 0.8f : 0.99f));
		sprite.setPosition(position.x, position.y);

		//bounds.drawDebugBounds(position);
	}

	protected boolean doesIntersect(Vector2 position) {

		for(Entity ent : map.entities) {
			if(ent == this) continue;
			if(getBoundingBox().doesIntersect(position, ent))
				return true;
		}

		return false;
	}

	protected boolean move(Vector2 position) {

		if(doesIntersect(position)) return false;
		this.position = position;
		return true;
	}

	public abstract boolean hasGravity();
}