package com.me4502.Cohesion.util;

import com.flowpowered.math.vector.Vector2d;
import com.me4502.Cohesion.entities.Entity;

public abstract class Bounds {

	public abstract boolean doesIntersect(Vector2d position, Entity entity);

	public abstract boolean doesIntersect(Vector2d position, Vector2d intersectionpoint);

	public abstract void drawDebugBounds(Vector2d position);
}