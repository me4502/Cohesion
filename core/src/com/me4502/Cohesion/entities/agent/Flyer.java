package com.me4502.Cohesion.entities.agent;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.entities.agent.ai.AIHoming;
import com.me4502.Cohesion.map.MapInstance;

public class Flyer extends Agent {

	public Flyer(MapInstance map, Sprite sprite, Vector2 position) {
		super(map, sprite, position);

		health = 5;
		baseAI = new AIHoming(this, position);
	}

	@Override
	public boolean hasGravity() {
		return true;
	}

	@Override
	public void update() {

		timeSinceHit ++;

		if(timeSinceHit > 5)
			velocity.set(0, GRAVITY.y + (float)Math.cos(age / 5));

		super.update();
	}

	@Override
	public void damage() {
		health -= 1;
	}

}
