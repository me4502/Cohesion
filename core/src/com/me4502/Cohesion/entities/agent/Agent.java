package com.me4502.Cohesion.entities.agent;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.entities.Entity;
import com.me4502.Cohesion.entities.agent.ai.AIBase;
import com.me4502.Cohesion.map.MapInstance;

import java.util.LinkedList;
import java.util.List;

public abstract class Agent extends Entity {

	public List<AIBase> aiBehaviour = new LinkedList<>();

	public Agent(MapInstance map, Sprite sprite, Vector2 position) {
		super(map, sprite, position);
	}

	public abstract void initializeAI();

	@Override
	public void update() {

		for(AIBase ai : aiBehaviour) {
			ai.update();
			if(ai.isBlocking()) break;
		}

		super.update();
	}
}
