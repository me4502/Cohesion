package com.me4502.Cohesion.entities;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.map.Chunk;
import com.me4502.Cohesion.map.MapInstance;
import com.me4502.Cohesion.tile.Tile;
import com.me4502.Cohesion.util.Bounds;
import com.me4502.Cohesion.util.Collidable;
import com.me4502.Cohesion.util.RectangularBounds;

public abstract class Entity implements Collidable {

	public static final Vector2 GRAVITY = new Vector2(0, 1.4f);

	public static float COLLISION_ACCURACY = 100f; //Collision Accuracy. Higher = Less Wall Clipping & More Lag

	Vector2 position;
	public Vector2 velocity;

	public Sprite sprite;

	Bounds bounds;

	public MapInstance map;

	boolean onGround;
	boolean remove;

	public int timeSinceHit;

	/* Movement Modifiers */
	public float collisionDrag = 0.01f;
	public float airDrag = 0.99f;
	public float groundDrag = 0.8f;

	public Entity(MapInstance map, Sprite sprite, Vector2 position) {

		this.map = map;
		this.position = position;
		bounds = new RectangularBounds(sprite.getTexture().getWidth(), sprite.getTexture().getHeight());

		if(!move(position)) {
			remove = true;
			return;
		}
		sprite.setPosition(position.x, position.y);

		this.sprite = sprite;

		velocity = new Vector2(0,0);
	}

	public boolean shouldRemove() {
		return remove;
	}

	@Override
	public Bounds getBoundingBox() {
		return bounds;
	}

	public void setPosition(Vector2 position) {
		move(position);
	}

	@Override
	public Vector2 getPosition() {
		return position.cpy();
	}

	public void render(SpriteBatch batch) {
		if(!shouldRemove())
			sprite.draw(batch);
	}

	public void update() {

		if(shouldRemove()) return;

		onGround = doesIntersect(getPosition().sub(0, 2));

		if(hasGravity() && !onGround) //Only apply gravity if we aren't on the ground
			velocity.sub(GRAVITY);

		if(velocity.len2() > 0) {
			boolean moved = false;
			for(float i = 1f; i > 0.000001f; i-=1f/COLLISION_ACCURACY) {
				Vector2 tmp = velocity.cpy().scl(i);
				Vector2 cpy = tmp.cpy();
				if(!move(getPosition().add(tmp))) {
					tmp = tmp.scl(collisionDrag,1.0f);
					if(!move(getPosition().add(tmp))) {
						tmp = cpy.cpy();
						tmp = tmp.scl(1.0f,collisionDrag);
						if(!move(getPosition().add(tmp))) {
							tmp = cpy.cpy();
							tmp = tmp.scl(collisionDrag,collisionDrag);
							if(!move(getPosition().add(tmp)))
								continue;
							else {
								velocity = tmp;
								break;
							}
						} else {
							velocity = tmp;
							break;
						}
					} else {
						velocity = tmp;
						break;
					}
				} else {
					velocity = tmp;
					break;
				}
			}

			if(onGround && !moved)
				velocity.y = 0f;
		}

		velocity.scl(new Vector2(onGround ? groundDrag : airDrag, onGround ? groundDrag : airDrag));
		sprite.setPosition(position.x, position.y);

		//bounds.drawDebugBounds(position);
	}

	public void onCollision(Entity ent) {

	}

	protected boolean doesIntersect(Vector2 position) {

		List<Chunk> chunks = map.getChunks(position);
		for(Chunk chunk : chunks)
			for(Tile tile : chunk.getTiles())
				if(getBoundingBox().doesIntersect(position, tile))
					return true;

		for(Entity ent : map.entities) {
			if(ent == this || !ent.doesHardCollide()) continue;
			if(getBoundingBox().doesIntersect(position, ent)) {
				ent.onCollision(this);
				return true;
			}
		}

		return false;
	}

	protected boolean move(Vector2 position) {

		if(doesIntersect(position)) return false;
		this.position = position;
		return true;
	}

	public abstract boolean hasGravity();

	public boolean doesHardCollide() {
		return true;
	}
}