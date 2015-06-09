package com.me4502.Cohesion.entities.agent.ai;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.entities.Entity;
import com.me4502.Cohesion.entities.agent.Agent;
import com.me4502.Cohesion.entities.player.Player;
import com.me4502.Cohesion.entities.projectile.Projectile;

import java.util.function.Predicate;

public class AIFight extends AIBase {

    private static Predicate<Entity> defaultPredicate = (entity) -> entity instanceof Player;

    double attackRadius;
    Predicate<Entity> targetPredicate;

    int lastShootTime;
    int stallTime;

    public AIFight(Agent agent, double attackRadius, Predicate<Entity> targetPredicate) {
        super(agent);

        this.attackRadius = attackRadius;
        this.targetPredicate = targetPredicate;
    }

    public AIFight(Agent agent, double attackRadius) {
        this(agent, attackRadius, defaultPredicate);
    }

    public void setTarget(Entity target) {
        agent.setTarget(target);
        if(target == null)
            setStatus(AIStatus.DONE);
        else
            setStatus(AIStatus.WORKING);
    }

    @Override
    public void stall() {

        stallTime ++;

        if(stallTime % 25 == 0)
            setStatus(AIStatus.SEARCHING);
    }

    @Override
    public void search() {
        agent.getMap().entities.stream()
                .filter(entity -> targetPredicate.test(entity) && agent.getPosition().dst2(entity.getPosition()) <= attackRadius*attackRadius)
                .forEach(this::setTarget);
        if(agent.getTarget() == null)
            setStatus(AIStatus.STALLING);
    }

    @Override
    public void work() {
        if(lastShootTime > 50) {
            lastShootTime = 0;
            Projectile projectile = agent.getMap().spawnEntity(new Projectile(agent.getMap(), new Sprite(Cohesion.instance.projectile), agent.getPosition().add(agent.sprite.getWidth() / 2, agent.sprite.getHeight() / 2), agent.scaleFactor, agent));

            Vector2 targetPosition = agent.getTarget().getPosition().add(agent.getTarget().sprite.getWidth()/2, agent.getTarget().sprite.getHeight()/2);
            projectile.velocity.set(targetPosition.sub(agent.getPosition().add(agent.sprite.getWidth() / 2, agent.sprite.getHeight() / 2))).setLength(50);
        }

        lastShootTime ++;

        if(agent.getTarget() == null || agent.getTarget().shouldRemove() || agent.getPosition().dst2(agent.getTarget().getPosition()) > attackRadius*attackRadius*2) { //Only forget after leaves distance doubled.
            setTarget(null);
            setStatus(AIStatus.STALLING);
        }
    }

    @Override
    public void done() {
    }
}
