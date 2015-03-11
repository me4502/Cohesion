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
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.me4502.Cohesion.map.Map;

public class Cohesion extends ApplicationAdapter {

	SpriteBatch batch;

	FrameBuffer buffer;

	OrthographicCamera camera;

	public static final Random RANDOM = new Random();

	/* Shaders */
	ShaderProgram simple;
	ShaderProgram postProcessing;
	
	/* Textures */
	Texture player;
	Texture platform;

	Map map;

	@Override
	public void create () {

		buffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, true);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(640, 640 * (h / w));
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();

		batch = new SpriteBatch();

		ShaderProgram.pedantic = false;

		simple = new ShaderProgram(Gdx.files.internal("data/shaders/simple.vrt"), Gdx.files.internal("data/shaders/simple.frg"));
		postProcessing = new ShaderProgram(Gdx.files.internal("data/shaders/post.vrt"), Gdx.files.internal("data/shaders/post.frg"));

		player = new Texture("data/entity/player.png");
		platform = new Texture("data/platforms/platform.png");
		
		map = new Map();
	}

	@Override
	public void render () {

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		buffer.begin();
		Gdx.gl.glClearColor(0, 0, 0, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(simple.isCompiled()) {
			batch.setShader(simple);
		}

		batch.begin();
		batch.draw(platform, 10, 10);
		batch.end();
		buffer.end();

		if(simple.isCompiled()) {
			batch.setShader(postProcessing);
		}

		batch.begin();
		batch.draw(buffer.getColorBufferTexture(), 0, 0, buffer.getWidth(), buffer.getHeight(), 0, 0, buffer.getWidth(), buffer.getHeight(), false, true);
		batch.end();
	}
}
