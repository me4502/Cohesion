package com.me4502.Cohesion;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.me4502.Cohesion.map.Map;
import com.me4502.Cohesion.screens.GameScreen;
import com.me4502.Cohesion.screens.MainMenuScreen;
import com.me4502.Cohesion.screens.Screen;

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

	/* Shaders */
	public ShaderProgram simple;
	public ShaderProgram colorize;
	public ShaderProgram blur;
	public ShaderProgram postProcessing;
	public ShaderProgram background;

	/* Textures */
	public Texture player;
    public Texture flyer;
	public Texture platform;
	public Texture projectile;
	public Texture blockade;
	public Texture ground;
	public Texture mergeIcon;

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

				screen.mouseClick(((int)mouse.x) + Gdx.graphics.getWidth() / 2, ((int)mouse.y) + Gdx.graphics.getHeight() / 2);
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                Vector3 mouse = Cohesion.instance.camera.unproject(new Vector3(screenX, screenY, 1));

                screen.mouseRelease(((int)mouse.x) + Gdx.graphics.getWidth() / 2, ((int)mouse.y) + Gdx.graphics.getHeight() / 2);
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

    public void loadGraphics() {

        ShaderProgram.pedantic = true;

        simple = new ShaderProgram(Gdx.files.internal("data/shaders/simple.vrt"), Gdx.files.internal("data/shaders/simple.frg"));
        colorize = new ShaderProgram(Gdx.files.internal("data/shaders/colorize.vrt"), Gdx.files.internal("data/shaders/colorize.frg"));
        postProcessing = new ShaderProgram(Gdx.files.internal("data/shaders/post.vrt"), Gdx.files.internal("data/shaders/post.frg"));
        blur = new ShaderProgram(Gdx.files.internal("data/shaders/blur.vrt"), Gdx.files.internal("data/shaders/blur.frg"));
        background = new ShaderProgram(Gdx.files.internal("data/shaders/background.vrt"), Gdx.files.internal("data/shaders/background.frg"));

        if(blur.getLog().length() > 0 && !blur.getLog().equals("No errors.\n"))
            System.out.println(blur.getLog());

        if(simple.getLog().length() > 0 && !simple.getLog().equals("No errors.\n"))
            System.out.println(simple.getLog());

        if(colorize.getLog().length() > 0 && !colorize.getLog().equals("No errors.\n"))
            System.out.println(colorize.getLog());

        if(postProcessing.getLog().length() > 0 && !postProcessing.getLog().equals("No errors.\n"))
            System.out.println(postProcessing.getLog());

        if(background.getLog().length() > 0 && !background.getLog().equals("No errors.\n"))
            System.out.println(background.getLog());

        player = new Texture(Gdx.files.internal("data/entity/player." + TEXTURE_SIZE + ".png"), Pixmap.Format.RGBA8888, true);
        flyer = new Texture(Gdx.files.internal("data/entity/flyer." + TEXTURE_SIZE + ".png"), Pixmap.Format.RGBA8888, true);
        platform = new Texture(Gdx.files.internal("data/platforms/platform." + TEXTURE_SIZE + ".png"), Pixmap.Format.RGBA8888, true);
        projectile = new Texture(Gdx.files.internal("data/entity/projectile." + TEXTURE_SIZE + ".png"), Pixmap.Format.RGBA8888, true);
        blockade = new Texture(Gdx.files.internal("data/platforms/blockade." + TEXTURE_SIZE + ".png"), Pixmap.Format.RGBA8888, true);
        ground = new Texture(Gdx.files.internal("data/platforms/ground." + TEXTURE_SIZE + ".png"), Pixmap.Format.RGBA8888, true);
        mergeIcon = new Texture(Gdx.files.internal("data/icons/merge_icon." + TEXTURE_SIZE + ".png"), Pixmap.Format.RGBA8888, true);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("data/fonts/crumbs.ttf"));
		FreeTypeFontGenerator.setMaxTextureSize(256 * AA_AMOUNT);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (24 * (TEXTURE_SIZE/32))*AA_AMOUNT;
        parameter.magFilter = Texture.TextureFilter.MipMapLinearLinear;
        parameter.minFilter = Texture.TextureFilter.MipMapLinearLinear;
        parameter.genMipMaps = true;
        mainFont = generator.generateFont(parameter);
        mainFont.getData().setScale(32f/TEXTURE_SIZE);
        generator.dispose();
    }

	@Override
	public void render () {
        if(PROFILING) GLProfiler.reset();
        screen.render(batch);
        if(PROFILING) System.out.println(GLProfiler.calls);
	}
}
