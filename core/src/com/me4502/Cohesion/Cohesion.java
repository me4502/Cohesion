package com.me4502.Cohesion;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;
import com.me4502.Cohesion.map.Map;

public class Cohesion extends ApplicationAdapter {

	SpriteBatch batch;
	public ShapeRenderer shapes;

	FrameBuffer buffer;

	/* Blur Data */
	public static final int FBO_SIZE = 1024;
	FrameBuffer blurA, blurB;

	public OrthographicCamera camera;

	public static Cohesion instance;

	public static final Random RANDOM = new Random();

	/* Shaders */
	public ShaderProgram simple;
	public ShaderProgram colorize;
	public ShaderProgram blur;
	public ShaderProgram postProcessing;

	/* Textures */
	public Texture player;
	public Texture platform;
	public Texture projectile;
	public Texture blockade;
	public Texture ground;

	public Texture lastFrame;

	public Map map;

	private Matrix4 standardMatrix = new Matrix4();

	@Override
	public void create () {

		instance = this;

		RANDOM.setSeed(RANDOM.nextLong());

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(640, 640 * (h / w));
		//camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.update();

		buffer = new FrameBuffer(Format.RGBA8888, (int)camera.viewportWidth, (int)camera.viewportHeight, false, true); //Super Sampling

		blurA = new FrameBuffer(Format.RGBA8888, FBO_SIZE, FBO_SIZE, false);
		blurB = new FrameBuffer(Format.RGBA8888, FBO_SIZE, FBO_SIZE, false);

		batch = new SpriteBatch();
		shapes = new ShapeRenderer();

		ShaderProgram.pedantic = false;

		simple = new ShaderProgram(Gdx.files.internal("data/shaders/simple.vrt"), Gdx.files.internal("data/shaders/simple.frg"));
		colorize = new ShaderProgram(Gdx.files.internal("data/shaders/colorize.vrt"), Gdx.files.internal("data/shaders/colorize.frg"));
		postProcessing = new ShaderProgram(Gdx.files.internal("data/shaders/post.vrt"), Gdx.files.internal("data/shaders/post.frg"));
		blur = new ShaderProgram(Gdx.files.internal("data/shaders/blur.vrt"), Gdx.files.internal("data/shaders/blur.frg"));

		if(blur.getLog().length() > 0 && !blur.getLog().equals("No errors.\n"))
			System.out.println(blur.getLog());

		if(simple.getLog().length() > 0 && !simple.getLog().equals("No errors.\n"))
			System.out.println(simple.getLog());

		if(colorize.getLog().length() > 0 && !colorize.getLog().equals("No errors.\n"))
			System.out.println(colorize.getLog());

		if(postProcessing.getLog().length() > 0 && !postProcessing.getLog().equals("No errors.\n"))
			System.out.println(postProcessing.getLog());

		player = new Texture("data/entity/player.png");
		platform = new Texture("data/platforms/platform.png");
		projectile = new Texture("data/entity/projectile.png");
		blockade = new Texture("data/platforms/blockade.png");
		ground = new Texture("data/platforms/ground.png");

		map = new Map();

		standardMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

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

		//Blur. If possible.
		if(lastFrame != null && blur.isCompiled()) {
			//Blur it.
			blurA.begin();
			batch.setShader(simple);
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			batch.begin();

			batch.draw(lastFrame, 0, 0);

			batch.flush();

			blurA.end();
			blurB.begin();
			Gdx.gl.glClearColor(0, 0, 0,1f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			batch.setShader(blur);

			blur.setUniform2fv("dir", new float[]{1f, 0f}, 0, 2);
			blur.setUniformf("resolution", FBO_SIZE);
			blur.setUniformf("radius", 3);

			batch.draw(blurA.getColorBufferTexture(), 0, 0, camera.viewportWidth, camera.viewportHeight, 0, 0, blurA.getWidth(), blurA.getHeight(), false, false);
			batch.flush();

			blurB.end();
			batch.end();
		}

		camera.position.set(map.getCentrePoint(), 0);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		buffer.begin();
		Gdx.gl.glClearColor(0, 0, 0,1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		map.update();

		batch.begin();
		if(lastFrame != null) {
			batch.setProjectionMatrix(standardMatrix);
			batch.setShader(blur);
			blur.setUniform2fv("dir", new float[]{0f, 1f}, 0, 2);
			blur.setUniformf("resolution", FBO_SIZE);
			blur.setUniformf("radius", 3);
			batch.draw(blurB.getColorBufferTexture(), 0, 0, camera.viewportWidth, camera.viewportHeight, 0, 0, blurB.getWidth(), blurB.getHeight(), false, true);
						
			batch.setProjectionMatrix(camera.combined);
		}

		if(simple.isCompiled()) {
			batch.setShader(simple);
		} else {
			batch.setShader(null);
		}

		map.render(batch);
		batch.end();
		buffer.end();

		if(postProcessing.isCompiled()) {
			batch.setShader(postProcessing);
		}

		batch.setProjectionMatrix(standardMatrix);

		batch.begin();
		batch.draw(lastFrame = buffer.getColorBufferTexture(), 0, 0, camera.viewportWidth, camera.viewportHeight, 0, 0, buffer.getWidth(), buffer.getHeight(), false, true);
		batch.end();
	}
}
