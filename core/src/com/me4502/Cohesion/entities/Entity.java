package com.me4502.Cohesion.entities;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.entities.player.Player;
import com.me4502.Cohesion.entities.projectile.Projectile;
import com.me4502.Cohesion.map.Chunk;
import com.me4502.Cohesion.map.MapInstance;
import com.me4502.Cohesion.tile.Tile;
import com.me4502.Cohesion.util.Bounds;
import com.me4502.Cohesion.util.Collidable;
import com.me4502.Cohesion.util.DamageSource;
import com.me4502.Cohesion.util.RectangularBounds;

public abstract class Entity implements Collidable, DamageSource {

	public static final Vector2 GRAVITY = new Vector2(0, 1.4f);

	public static float COLLISION_ACCURACY = 100f; //Collision Accuracy. Higher = Less Wall Clipping & More Lag

	Vector2 position;
	public Vector2 velocity;

	public Sprite sprite;

	Bounds bounds;

	public MapInstance map;

	public boolean onGround;
	boolean remove;

	public int timeSinceHit = 1000;

	/* Movement Modifiers */
	public float collisionDrag = 0.01f;
	public float airDrag = 0.99f;
	public float groundDrag = 0.8f;

	protected int age;
    public int timeSinceMove;

    public double maxHealth;
    public double health;

    public Entity(MapInstance map, Sprite sprite, Vector2 position) {

		this.map = map;
		this.position = position;
		this.sprite = sprite;
		bounds = new RectangularBounds(sprite.getTexture().getWidth(), sprite.getTexture().getHeight());

		velocity = new Vector2(0,0);

        try {
            if (!move(position)) {
                remove = true;
                return;
            }
        } catch(Exception e){}

		sprite.setPosition(position.x, position.y);
	}

    public boolean damage(DamageSource source){return false;}

    public MapInstance getMap() {
		return map;
	}

	public boolean shouldRemove() {
		return remove || position.y < -200 || (maxHealth > 0 && health < 0);
	}

	@Override
	public Bounds getBoundingBox() {
		return bounds;
	}

	public void setPosition(Vector2 position) {
		move(position);
	}

	@Override
	public Vector2 getPosition() {
		return position.cpy();
	}

	public void render(SpriteBatch batch) {
		if(!shouldRemove()) {
            if(maxHealth > 0)
                sprite.setAlpha((float) Math.min(health / maxHealth + 0.05f, 1.0f));
            sprite.draw(batch);
        }
	}

	public void update() {

		age ++;
        timeSinceMove ++;

		if(shouldRemove()) return;

		timeSinceHit ++;

		onGround = doesIntersect(getPosition().sub(0, 2));

		if(hasGravity() && !onGround) //Only apply gravity if we aren't on the ground
			velocity.sub(GRAVITY);

		if(velocity.len2() > 0.05f) {
			boolean moved = false;
			for(float i = 1f; i > 1f/COLLISION_ACCURACY; i-=1f/COLLISION_ACCURACY) {
				Vector2 tmp = velocity.cpy().scl(i);
				Vector2 cpy = tmp.cpy();
				if(!move(getPosition().add(tmp))) {
					tmp = tmp.scl(collisionDrag,1.0f);
					if(!move(getPosition().add(tmp))) {
						tmp = cpy.cpy();
						tmp = tmp.scl(1.0f,collisionDrag);
						if(!move(getPosition().add(tmp))) {
							tmp = cpy.cpy();
							tmp = tmp.scl(collisionDrag,collisionDrag);
							if(!move(getPosition().add(tmp)))
								continue;
							else {
                                moved = true;
								velocity = tmp;
								break;
							}
						} else {
                            moved = true;
							velocity = tmp;
							break;
						}
					} else {
                        moved = true;
						velocity = tmp;
						break;
					}
				} else {
					moved = true;
					velocity = tmp;
					break;
				}
			}

			if(onGround && !moved)
				velocity.y = 0f;

            if(moved) timeSinceMove = 0;
		}

		velocity.scl(new Vector2(onGround ? groundDrag : airDrag, onGround ? groundDrag : airDrag));
		sprite.setPosition(position.x, position.y);

		if(Cohesion.DEBUG) bounds.drawDebugBounds(position);
	}

    public boolean onCollision(Entity ent) {
        if(ent instanceof Projectile && ent.timeSinceHit > 5 && ((Projectile) ent).timeSinceMove < 5) {
            if(((Projectile) ent).getShooter().equals(this)) return false;
            velocity.sub(ent.velocity.cpy().scl(ent.collisionDrag));
            ent.timeSinceHit = 0;
            timeSinceHit = 0;
            if(damage(((Projectile) ent).getShooter()))
                ((Projectile) ent).age += 5000;
            return true;
        }

        return true;
    }

	protected boolean doesIntersect(Vector2 position) {

		List<Chunk> chunks = map.getChunks(position);
		for(Chunk chunk : chunks)
			for(Tile tile : chunk.getTiles())
				if(getBoundingBox().doesIntersect(position, tile))
					return true;

		for(Entity ent : map.entities) {
			if(ent == this || !ent.doesHardCollide()) continue;
			if(getBoundingBox().doesIntersect(position, ent)) {
				if(ent.onCollision(this))
					return true;
			}
		}

		return false;
	}

	public boolean move(Vector2 position) {

		if(doesIntersect(position)) return false;
		this.position = position;
		return true;
	}

	public abstract boolean hasGravity();

	public boolean doesHardCollide() {
		return true;
	}
}
