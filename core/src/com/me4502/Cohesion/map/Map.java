package com.me4502.Cohesion.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.map.wgen.WorldGenTypes;
import com.me4502.Cohesion.screens.GameOverScreen;
import com.me4502.Cohesion.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;

public class Map {

	private final List<MapInstance> instances = new ArrayList<>();

	private int currentX = 150;

	private int lastScoreX = currentX;
	private int score = 0;

	private int updateTick = 0;

	private int mergeCount = 2;

	private final Vector2 centrePoint = new Vector2(0,0);

    private final GlyphLayout layout;

	public Map() {
        layout = new GlyphLayout();

		instances.add(new MapInstance(new Color(1.0f, 0f, 0f, 0.33f)));
		instances.add(new MapInstance(new Color(0f, 1.0f, 0f, 0.33f)));
		instances.add(new MapInstance(new Color(0f, 0f, 1.0f, 0.33f)));

		for(MapInstance instance : instances)
			instance.generateNext(0);

		centrePoint.set(0, 0);

		for(MapInstance instance : instances)
			centrePoint.add(instance.player.getPosition().x, instance.player.getPosition().y);

		centrePoint.scl(1f/instances.size());
	}

	public void render(SpriteBatch batch) {
		for(MapInstance instance : instances)
			instance.render(batch);
    }

    public void renderGui(SpriteBatch batch) {
        if(!(Cohesion.instance.screen instanceof GameScreen)) return;

        layout.setText(Cohesion.instance.mainFont, "Score " + score);

        Cohesion.instance.mainFont.draw(batch, layout, ((GameScreen)Cohesion.instance.screen).getWidth()/2 - layout.width/3, ((GameScreen)Cohesion.instance.screen).getHeight() - 32);

        for(int i = 0; i < mergeCount; i++) {
            batch.draw(GameScreen.mergeIcon, 16 * i + (16), ((GameScreen)Cohesion.instance.screen).getHeight() - (16 * 2), 16, 16);
        }
    }

	public boolean isSlowed() {
		return Gdx.input.isKeyPressed(Keys.SHIFT_LEFT);
	}

	public void update() {
		updateTick ++;

		if(isSlowed() && updateTick < 3)
			return;

		updateTick = 0;

		centrePoint.set(0, 0);

		for(MapInstance instance : instances)
			centrePoint.add(instance.player.getPosition().x, instance.player.getPosition().y);

		centrePoint.scl(1f/instances.size());

		if(Gdx.input.isKeyJustPressed(Keys.E) && mergeCount > 0) {
			boolean success = false;
			for(MapInstance instance : instances) {
                if (instance.player.tryMove(Cohesion.instance.getMap().getCentrePoint())) {
                    instance.player.velocity.set(0,0);
                    success = true;
                }
            }
			if(success)
				mergeCount --;
		}

		if(Gdx.input.isKeyJustPressed(Keys.R))
			gameOver();

		for(MapInstance instance : instances)
			instance.update();

		for(MapInstance instance : instances)
			if(instance.player.getPosition().y < -128 || instance.player.shouldRemove()) {
				instances.remove(instance);
				break;
			}

		if(instances.isEmpty() || instances.size() == 1) { //GameOver
			instances.clear();
			gameOver();
			return; //Do stuff.
	    }

		if(instances.get(0).getChunks(getCentrePoint().add(Chunk.CHUNK_WIDTH, 0)).isEmpty()) {

			int gen = Cohesion.RANDOM.nextInt(WorldGenTypes.values().length);

			for(MapInstance map : instances)
				map.generateNext(gen);

			currentX += 75;
		}

		Vector2 pos = instances.get(0).player.getPosition();

		if(pos != null && pos.dst2(getCentrePoint()) < 32*32 && (int)pos.x > lastScoreX) {
			giveScore(instances.size() * Math.abs(lastScoreX - (int)pos.x)/8);
			lastScoreX = (int)pos.x;
		}
	}

	public void giveScore(int amount) {
		for(int i = 0; i < amount; i++) {
			score ++;
			checkScore();
		}
	}

	public void checkScore() {
		if(score % 1000 == 0)
			//Give a reward.
			mergeCount ++;
	}

	public Vector2 getCentrePoint() {
		return centrePoint.cpy();
	}

	public void gameOver() {
		Cohesion.instance.switchScreen(new GameOverScreen());
	}
}
