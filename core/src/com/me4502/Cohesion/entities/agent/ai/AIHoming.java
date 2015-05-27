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
		if(agent.getPosition().dst2(homeLocation) > 64) {
			setStatus(AIStatus.WORKING);
		}
	}

	@Override
	public void search() {
		setStatus(AIStatus.WORKING);
	}

	@Override
	public void work() {
		Vector2 offset = homeLocation.cpy().sub(agent.getPosition());
		agent.move(agent.getPosition().add(offset.setLength2(0.5f)));

        if(agent.getPosition().dst2(homeLocation) <= 64) {
            setStatus(AIStatus.DONE);
        }
	}

	@Override
	public void done() {

	}

}
