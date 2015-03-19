package com.me4502.Cohesion.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.flowpowered.math.vector.Vector2d;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.entities.Entity;

public class RectangularBounds extends Bounds {

	int width;
	int height;

	public RectangularBounds(int width, int height) {

		this.width = width;
		this.height = height;
	}

	@Override
	public boolean doesIntersect(Vector2d position, Entity entity) {

		if(entity.getBoundingBox() instanceof RectangularBounds) {
			RectangularBounds otherBox = (RectangularBounds) entity.getBoundingBox();

			if(position.getX() < entity.getPosition().getX() + otherBox.width && position.getX() + width > entity.getPosition().getX())
				if(position.getY() < entity.getPosition().getY() + otherBox.height && position.getY() + height > entity.getPosition().getY())
					return true;
		}

		return false;
	}

	@Override
	public boolean doesIntersect(Vector2d position, Vector2d intersectionpoint) {

		if(position.getX() <= intersectionpoint.getX() && position.getX() + width >= intersectionpoint.getX())
			if(position.getY() <= intersectionpoint.getY() && position.getY() + height >= intersectionpoint.getY())
				return true;

		return false;
	}

	@Override
	public void drawDebugBounds(Vector2d position) {

		Cohesion.instance.shapes.setColor(Color.WHITE);
		Cohesion.instance.shapes.begin(ShapeType.Filled);
		Cohesion.instance.shapes.rect(position.getFloorX(), position.getFloorY(), width, height);
		Cohesion.instance.shapes.end();
	}
}
