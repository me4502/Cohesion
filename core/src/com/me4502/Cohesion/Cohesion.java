package com.me4502.Cohesion;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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

	//Settings Values
    public static int SHADER_QUALITY_LEVEL = 8; //Default is 8
	public static int TEXTURE_SIZE = 128; //Default is 128

	SpriteBatch batch;
	public ShapeRenderer shapes;

	/* Blur Data */
	public static final int BLUR_RADIUS = 5;

	public OrthographicCamera camera;

	public static Cohesion instance;

	public static final Random RANDOM = new Random();

	public BitmapFont mainFont;

	public Music music;

    public Screen screen;
	public Matrix4 standardMatrix = new Matrix4();

	public Map getMap() {
        if(screen instanceof GameScreen)
            return ((GameScreen)screen).map;
        return null;
    }

	@Override
	public void create () {
		instance = this;

		//Load the configuration.
        Preferences graphicsPreferences = Gdx.app.getPreferences("graphics");

        SHADER_QUALITY_LEVEL = graphicsPreferences.getInteger("ShaderQuality", 8);
        TEXTURE_SIZE = graphicsPreferences.getInteger("TextureQuality", 128);
        graphicsPreferences.flush();

        //Grab the width and height of the screen.
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

        //Setup the view camera.
		camera = new OrthographicCamera(640, 640 * (h/w));
		camera.update();

        //Setup the sprite and shape batching.
        batch = new SpriteBatch(32);
        if(DEBUG)
		    shapes = new ShapeRenderer();

        //Load basic graphics required for all screens.
        loadGraphics();

		music = Gdx.audio.newMusic(Gdx.files.internal("data/audio/flowershop.ogg"));
		music.setVolume(0.5f);
		music.setLooping(true);
		music.play();

		//Set the main menu as the default screen.
        screen = new MainMenuScreen();
        screen.initialize();

        //Setup an input processor to handle received inputs.
		Gdx.input.setInputProcessor(new InputProcessor() {
			@Override
			public boolean keyDown(int keycode) {
                if(keycode == Input.Keys.F11) {
                	if (!Gdx.graphics.isFullscreen()) {
                		Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
					} else {
                		Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
					}
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

				screen.mouseClick((int) (((int)mouse.x) + camera.viewportWidth / 2), (int) (((int)mouse.y) + camera.viewportHeight / 2), button);
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                Vector3 mouse = Cohesion.instance.camera.unproject(new Vector3(screenX, screenY, 1));

                screen.mouseRelease((int) (((int)mouse.x) + camera.viewportWidth / 2), (int) (((int) mouse.y) + camera.viewportHeight / 2), button);
                return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
                Vector3 mouse = Cohesion.instance.camera.unproject(new Vector3(screenX, screenY, 1));

                screen.mouseDragged((int) (((int) mouse.x) + camera.viewportWidth / 2), (int) (((int) mouse.y) + camera.viewportHeight / 2));
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

        standardMatrix.setToOrtho2D(0, 0, (int)Cohesion.instance.camera.viewportWidth, (int)Cohesion.instance.camera.viewportHeight);

        if(mainFont != null)
            mainFont.dispose();

		mainFont = new BitmapFont(Gdx.files.internal("data/fonts/crumbs.fnt"), Gdx.files.internal("data/fonts/crumbs.png"), false);

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
        screen.render(batch);
	}
}
