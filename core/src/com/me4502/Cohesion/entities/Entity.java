package com.me4502.Cohesion.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.map.MapInstance;
import com.me4502.Cohesion.util.Bounds;
import com.me4502.Cohesion.util.RectangularBounds;

public abstract class Entity {

	public static final Vector2 GRAVITY = new Vector2(0, 3);

	Vector2 position;
	Vector2 velocity;

	Sprite sprite;
	
	Bounds bounds;
	
	MapInstance map;

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

		if(hasGravity())
			velocity.sub(GRAVITY);

		if(velocity.len2() > 0) {
			if(!move(getPosition().add(velocity)))
				velocity.add(GRAVITY).dot(new Vector2(0.7f,0.7f));
		}
		
		sprite.setPosition(position.x, position.y);
	}

	protected boolean move(Vector2 position) {
		
		for(Entity ent : map.entities) {
			if(ent == this) continue;
			if(ent.getBoundingBox().doesIntersect(ent.getPosition(), this))
				return false;
		}
		
		this.position = position;
		return true;
	}
	
	public abstract boolean hasGravity();
}