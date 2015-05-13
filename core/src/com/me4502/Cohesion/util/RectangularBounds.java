package com.me4502.Cohesion.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.Cohesion;

public class RectangularBounds extends Bounds {

	int width;
	int height;

	int padding = 0;

	public RectangularBounds(int width, int height) {

		this.width = width;
		this.height = height;
	}

	@Override
	public boolean doesIntersect(Vector2 position, Collidable entity) {

		if(entity.getBoundingBox() instanceof RectangularBounds) {
			RectangularBounds otherBox = (RectangularBounds) entity.getBoundingBox();

			if(position.x + padding < entity.getPosition().x + otherBox.width && position.x + width > entity.getPosition().x)
				if(position.y + padding < entity.getPosition().y + otherBox.height && position.y + height > entity.getPosition().y)
					return true;
		}

		return false;
	}

	@Override
	public boolean doesIntersect(Vector2 position, Vector2 intersectionpoint) {

		if(position.x + padding <= intersectionpoint.x && position.x + width >= intersectionpoint.x)
			if(position.y + padding <= intersectionpoint.y && position.y + height >= intersectionpoint.y)
				return true;

		return false;
	}

	@Override
	public void drawDebugBounds(Vector2 position) {

		Cohesion.instance.shapes.setColor(Color.WHITE);
		Cohesion.instance.shapes.begin(ShapeType.Filled);
		Cohesion.instance.shapes.rect(position.x + padding, position.y + padding, width, height);
		Cohesion.instance.shapes.end();
	}

	@Override
	public Bounds padding() {

		Bounds bounds = new RectangularBounds(width, height);
		((RectangularBounds)bounds).padding = 2;
		((RectangularBounds)bounds).width -= 2;
		((RectangularBounds)bounds).height -= 2;

		return bounds;
	}
}
