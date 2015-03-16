package com.me4502.Cohesion.map;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.entities.Entity;

public class MapInstance {

	List<Entity> entities = new ArrayList<Entity>();

	Color color;
	
	public MapInstance(Color color) {
		this.color = color;
	}

	public void render(SpriteBatch batch) {
	
		batch.setShader(Cohesion.instance.colorize);
		Cohesion.instance.colorize.setUniformf("v_color", color.r, color.g, color.b, color.a);
		
		for(Entity ent : entities)
			ent.render(batch);
	}

	public void update() {

		for(Entity ent : entities)
			ent.update();
	}
}
