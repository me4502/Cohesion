package com.me4502.Cohesion.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Entity {

	public static final Vector2 GRAVITY = new Vector2(0, 3);

	Vector2 position;
	Vector2 velocity;

	Sprite sprite;

	public Entity(Sprite sprite) {

		this.sprite = sprite;
	}

	public void setPosition(Vector2 position) {
		move(position);
	}

	public Vector2 getPosition() {
		return position;
	}

	public void render(SpriteBatch batch) {
		sprite.draw(batch);
	}

	public void update() {

		velocity.add(GRAVITY);

		move(position.add(velocity));
	}

	protected void move(Vector2 position) {
		this.position = position;
	}
}