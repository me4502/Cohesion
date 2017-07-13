package com.me4502.Cohesion.entities.agent.ai;

import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.entities.agent.Agent;

public class AIFollow extends AIBase {

    private double searchRadius;

    public AIFollow(Agent agent, double searchRadius) {
        super(agent);

        this.searchRadius = searchRadius;
    }

    @Override
    public void stall() {
        if(agent.getTarget() != null && agent.getTarget().getPosition().dst2(agent.getPosition()) > searchRadius*searchRadius) {
            setStatus(AIStatus.WORKING);
        }
    }

    @Override
    public void search() {

    }

    @Override
    public void work() {
        Vector2 offset = agent.getTarget().getPosition().sub(agent.getPosition());
        agent.move(agent.getPosition().add(offset.setLength2(0.5f)));

        if(agent.getPosition().dst2(agent.getTarget().getPosition()) <= searchRadius*searchRadius) {
            setStatus(AIStatus.DONE);
        }
    }

    @Override
    public void done() {

    }
}
