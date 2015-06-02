package com.me4502.Cohesion.entities.agent;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.entities.Entity;
import com.me4502.Cohesion.entities.agent.ai.AIFight;
import com.me4502.Cohesion.entities.agent.ai.AIHoming;
import com.me4502.Cohesion.map.MapInstance;
import com.me4502.Cohesion.util.DamageSource;

public class Flyer extends Agent {

	AIHoming homingAI;
	AIFight fightingAI;

	public Flyer(MapInstance map, Sprite sprite, Vector2 position) {
		super(map, sprite, position);

		health = 5;
		maxHealth = health;
	}

	@Override
	public void initializeAI() {
		//Order is important.
		aiBehaviour.add(0, fightingAI = new AIFight(this, 250));
		aiBehaviour.add(1, homingAI = new AIHoming(this, getPosition()));
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
	public boolean damage(DamageSource source) {
        health -= 1;

        if(source instanceof Entity) {
            fightingAI.setTarget((Entity) source);
            if(health < 0 && ((Entity) source).health < ((Entity) source).maxHealth) {
                ((Entity) source).health ++;
            }
        }

        return true;
	}

}
