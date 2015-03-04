package com.me4502.Cohesion;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class Cohesion extends ApplicationAdapter {

	SpriteBatch batch;
	Texture img;

	FrameBuffer buffer;
	OrthographicCamera camera;

	public static final Random RANDOM = new Random();

	@Override
	public void create () {

		buffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, true);

		camera = new OrthographicCamera(buffer.getWidth(), buffer.getHeight());

		batch = new SpriteBatch();

		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {

		buffer.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0.4f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.setProjectionMatrix(camera.combined);
		batch.draw(img, RANDOM.nextInt(300), RANDOM.nextInt(300));
		batch.end();
		buffer.end();

		batch.begin();
		batch.draw(buffer.getColorBufferTexture(), 0, 0);
		batch.end();
	}
}
