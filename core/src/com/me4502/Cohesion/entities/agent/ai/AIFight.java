package com.me4502.Cohesion.entities.agent.ai;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.entities.Entity;
import com.me4502.Cohesion.entities.agent.Agent;
import com.me4502.Cohesion.entities.player.Player;
import com.me4502.Cohesion.entities.projectile.Projectile;
import com.me4502.Cohesion.screens.GameScreen;

import java.util.function.Predicate;

public class AIFight extends AIBase {

    private static final Predicate<Entity> defaultPredicate = entity -> entity instanceof Player;

    private final double attackRadius;
    private final Predicate<Entity> targetPredicate;

    private int lastShootTime;
    private int stallTime;

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
        for(Entity ent : agent.getMap().entities) {
            if(targetPredicate.test(ent) && agent.getPosition().dst2(ent.getPosition()) <= attackRadius*attackRadius)
                setTarget(ent);
        }
        if(agent.getTarget() == null)
            setStatus(AIStatus.STALLING);
    }

    @Override
    public void work() {
        if(lastShootTime > 50) {
            lastShootTime = 0;
            Projectile projectile = agent.getMap().spawnEntity(new Projectile(agent.getMap(), new Sprite(GameScreen.projectile), agent.getPosition().add(agent.sprite.getWidth() / 2, agent.sprite.getHeight() / 2), agent.scaleFactor, agent));

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
