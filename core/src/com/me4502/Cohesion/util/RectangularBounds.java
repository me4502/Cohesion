package com.me4502.Cohesion.util;

import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.entities.Entity;

public class RectangularBounds extends Bounds {

	int width;
	int height;
	
	public RectangularBounds(int width, int height) {
		
		this.width = width;
		this.height = height;
	}
	
	@Override
	public boolean doesIntersect(Vector2 position, Entity entity) {

		if(entity.getBoundingBox() instanceof RectangularBounds) {
			RectangularBounds otherBox = (RectangularBounds) entity.getBoundingBox();
			
			if (position.x + width <= entity.getPosition().x) return false;
			if (position.x >= entity.getPosition().x + otherBox.width) return false;
		    if (position.y - height >= entity.getPosition().y) return false;
		    if (position.y <= entity.getPosition().y - otherBox.height) return false;
		    return true;
		}
		return false;
	}

	@Override
	public boolean doesIntersect(Vector2 position, Vector2 intersectionpoint) {
		
		if(position.x <= intersectionpoint.x && position.x + width >= intersectionpoint.x)
			if(position.y <= intersectionpoint.y && position.y + height >= intersectionpoint.y)
				return true;
		
		return false;
	}
}
