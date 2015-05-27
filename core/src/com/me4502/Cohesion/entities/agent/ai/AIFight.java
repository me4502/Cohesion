package com.me4502.Cohesion.entities.agent.ai;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.entities.Entity;
import com.me4502.Cohesion.entities.agent.Agent;
import com.me4502.Cohesion.entities.projectile.Projectile;

public class AIFight extends AIBase {

    Entity target;
    int lastShootTime;

    public AIFight(Agent agent) {
        super(agent);
    }

    public void setTarget(Entity target) {
        this.target = target;
        if(target == null)
            setStatus(AIStatus.STALLING);
        else
            setStatus(AIStatus.WORKING);
    }

    @Override
    public void stall() {
    }

    @Override
    public void search() {
    }

    @Override
    public void work() {
        if(lastShootTime > 50) {
            lastShootTime = 0;
            Projectile projectile = (Projectile) agent.getMap().spawnEntity(new Projectile(agent.getMap(), new Sprite(Cohesion.instance.projectile), agent.getPosition(), agent));

            Vector2 offset = target.getPosition().sub(projectile.getPosition());
            projectile.velocity.set(projectile.getPosition().add(offset.setLength2(0.5f)));

        }

        lastShootTime ++;
    }

    @Override
    public void done() {
    }
}
