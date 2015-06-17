package com.me4502.Cohesion;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.me4502.Cohesion.map.Map;
import com.me4502.Cohesion.screens.GameScreen;
import com.me4502.Cohesion.screens.MainMenuScreen;
import com.me4502.Cohesion.screens.Screen;
import com.me4502.Cohesion.screens.ui.Button;

import java.util.Random;

public class Cohesion extends ApplicationAdapter {

	public static final boolean DEBUG = false;
    public static final boolean PROFILING = false;

	public static final int AA_AMOUNT = 8; //Default is 1
    public static final int SHADER_QUALITY_LEVEL = 32; //Default is 8
	public static final int TEXTURE_SIZE = 256; //Default is 32

	SpriteBatch batch;
	public ShapeRenderer shapes;

	/* Blur Data */
	public static final int FBO_SIZE = 128 * SHADER_QUALITY_LEVEL;
	public static final int BLUR_RADIUS = 5;

	public OrthographicCamera camera;

	public static Cohesion instance;

	public static final Random RANDOM = new Random();

	public BitmapFont mainFont;

    public Screen screen;
	public Matrix4 standardMatrix = new Matrix4();

	public Map getMap() {
        if(screen instanceof GameScreen)
            return ((GameScreen)screen).map;
        return null;
    }

	@Override
	public void create () {

        if(PROFILING) GLProfiler.enable();

		instance = this;

		RANDOM.setSeed(RANDOM.nextLong());

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera(640, 640 * (h/w));
		camera.update();

        standardMatrix.setToOrtho2D(0, 0, (int)Cohesion.instance.camera.viewportWidth * Cohesion.AA_AMOUNT, (int)Cohesion.instance.camera.viewportHeight * Cohesion.AA_AMOUNT);

        batch = new SpriteBatch(32);
        if(DEBUG)
		    shapes = new ShapeRenderer();

        loadGraphics();

        screen = new MainMenuScreen();
        screen.initialize();

		Gdx.input.setInputProcessor(new InputProcessor() {
			@Override
			public boolean keyDown(int keycode) {
                if(keycode == Input.Keys.F11) {
                    Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode().width, Gdx.graphics.getDesktopDisplayMode().height, !Gdx.graphics.isFullscreen());
                }

                screen.onKeyPress(keycode);
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

				Vector3 mouse = Cohesion.instance.camera.unproject(new Vector3(screenX, screenY, 1));

				screen.mouseClick((int) (((int)mouse.x) + camera.viewportWidth / 2), (int) (((int)mouse.y) + camera.viewportHeight / 2));
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                Vector3 mouse = Cohesion.instance.camera.unproject(new Vector3(screenX, screenY, 1));

                screen.mouseRelease((int) (((int)mouse.x) + camera.viewportWidth / 2), (int) (((int) mouse.y) + camera.viewportHeight / 2));
                return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
                Vector3 mouse = Cohesion.instance.camera.unproject(new Vector3(screenX, screenY, 1));

                screen.mouseDragged((int) (((int)mouse.x) + camera.viewportWidth / 2), (int) (((int) mouse.y) + camera.viewportHeight / 2));
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

    public void loadGraphics() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/fonts/crumbs.ttf"));
		FreeTypeFontGenerator.setMaxTextureSize(256 * AA_AMOUNT);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24*AA_AMOUNT;
        parameter.magFilter = Texture.TextureFilter.MipMapLinearLinear;
        parameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;
        parameter.genMipMaps = true;
        mainFont = generator.generateFont(parameter);
        generator.dispose();

        Button.loadTextures();
    }

    public void switchScreen(Screen newScreen) {
        if(this.screen != null) {
            this.screen.dispose();
            this.screen = null;
        }

        camera.position.set(0,0,0);
        camera.update();

        batch.setShader(null);
        screen = newScreen;
        screen.initialize();
    }

	@Override
	public void render () {
        if(PROFILING) GLProfiler.reset();
        screen.render(batch);
        if(PROFILING) System.out.println(GLProfiler.calls);
	}
}
