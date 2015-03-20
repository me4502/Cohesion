package com.me4502.Cohesion.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.entities.Entity;
import com.me4502.Cohesion.entities.Platform;
import com.me4502.Cohesion.entities.Player;

public class MapInstance {

	public List<Entity> entities = new ArrayList<Entity>();
	
	Player player;

	Color color;
	
	Random random = new Random();
	
	int lastTileHeight;
	
	public MapInstance(Color color) {
		this.color = color;
		
		entities.add(player = new Player(this, new Sprite(Cohesion.instance.player), new Vector2(50,100)));
		
		entities.add(new Platform(this, new Sprite(Cohesion.instance.platform), new Vector2(50,50)));
		lastTileHeight = 50;
				
		for(int x = 100; x < 1000; x += 75)
			entities.add(new Platform(this, new Sprite(Cohesion.instance.platform), new Vector2(x,lastTileHeight=lastTileHeight+MathUtils.random(-400, 75))));
	}

	public void render(SpriteBatch batch) {
	
		batch.setShader(Cohesion.instance.colorize);
		Cohesion.instance.colorize.setUniformf("color", color.r, color.g, color.b, color.a);
		
		for(Entity ent : entities)
			ent.render(batch);
		
		batch.setShader(Cohesion.instance.simple);
	}

	public void update() {

		for(Entity ent : entities)
			ent.update();
	}
}