package com.me4502.Cohesion.util;

import com.badlogic.gdx.math.Vector2;

public abstract class Bounds {

	public abstract boolean doesIntersect(Vector2 position, Collidable entity);

	public abstract boolean doesIntersect(Vector2 position, Vector2 intersectionpoint);

	public abstract void drawDebugBounds(Vector2 position);

	public abstract Bounds padding();
}