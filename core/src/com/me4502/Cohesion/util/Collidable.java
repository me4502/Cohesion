package com.me4502.Cohesion.util;

import com.badlogic.gdx.math.Vector2;

public interface Collidable {

	Bounds getBoundingBox();

	Vector2 getPosition();
}
