package com.me4502.Cohesion.util;

import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.entities.Entity;

public abstract class Bounds {

	public abstract boolean doesIntersect(Vector2 position, Entity entity);

	public abstract boolean doesIntersect(Vector2 position, Vector2 intersectionpoint);

	public abstract void drawDebugBounds(Vector2 position);
}