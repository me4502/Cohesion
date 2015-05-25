package com.me4502.Cohesion.entities.agent.ai;

import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.entities.agent.Agent;

public class AIHoming extends AIBase {

	Vector2 homeLocation;

	public AIHoming(Agent agent, Vector2 homeLocation) {
		super(agent);

		this.homeLocation = homeLocation;
	}

	@Override
	public void stall() {

	}

	@Override
	public void search() {

	}

	@Override
	public void work() {

	}

	@Override
	public void done() {

	}

}
