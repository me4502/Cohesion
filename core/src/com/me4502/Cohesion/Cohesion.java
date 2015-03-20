package com.me4502.Cohesion;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.me4502.Cohesion.map.Map;

public class Cohesion extends ApplicationAdapter {

	SpriteBatch batch;
	public ShapeRenderer shapes;

	FrameBuffer buffer;

	OrthographicCamera camera;

	public static Cohesion instance;

	public static final Random RANDOM = new Random();

	/* Shaders */
	public ShaderProgram simple;
	public ShaderProgram colorize;
	public ShaderProgram postProcessing;

	/* Textures */
	public Texture player;
	public Texture platform;

	Map map;
	
	private Vector2 lastCameraPosition;

	@Override
	public void create () {

		instance = this;

		buffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, true);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(640, 640 * (h / w));
		//camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();

		batch = new SpriteBatch();
		shapes = new ShapeRenderer();

		ShaderProgram.pedantic = false;

		simple = new ShaderProgram(Gdx.files.internal("data/shaders/simple.vrt"), Gdx.files.internal("data/shaders/simple.frg"));
		colorize = new ShaderProgram(Gdx.files.internal("data/shaders/colorize.vrt"), Gdx.files.internal("data/shaders/colorize.frg"));
		postProcessing = new ShaderProgram(Gdx.files.internal("data/shaders/post.vrt"), Gdx.files.internal("data/shaders/post.frg"));

		player = new Texture("data/entity/player.png");
		platform = new Texture("data/platforms/platform.png");

		map = new Map();

		Gdx.input.setInputProcessor(new InputProcessor() {
			@Override
			public boolean keyDown(int keycode) {
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}

			@Override
			public boolean scrolled(int amount) {
				return false;
			}
		});
	}

	@Override
	public void render () {

		camera.position.set(map.getCentrePoint().add(camera.viewportWidth/4, camera.viewportHeight/4), 0);
		//if(lastCameraPosition != null) {
		//	camera.translate((map.getCentrePoint().x - lastCameraPosition.x) /2f, map.getCentrePoint().y - lastCameraPosition.y);
		//}
		//lastCameraPosition = map.getCentrePoint();
		
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		buffer.begin();
		Gdx.gl.glClearColor(0, 0, 0, 1/5f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		map.update();

		if(simple.isCompiled()) {
			batch.setShader(simple);
		}

		batch.begin();
		map.render(batch);
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
