package com.me4502.Cohesion.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.map.Map;

public class GameScreen extends Screen {

    /* Textures */
    public static Texture player;
    public static Texture flyer;
    public static Texture platform;
    public static Texture projectile;
    public static Texture blockade;
    public static Texture ground;
    public static Texture mergeIcon;

    /* Shaders */
    public static ShaderProgram simple;
    public static ShaderProgram colorize;
    public static ShaderProgram blur;
    public static ShaderProgram postProcessing;
    public static ShaderProgram background;

    public FrameBuffer buffer;
    public FrameBuffer blurA, blurB;

    public Texture lastFrame;

    public Map map;

    @Override
    public void initialize() {
        super.initialize();

        player = new Texture(Gdx.files.internal("data/entity/player." + Cohesion.TEXTURE_SIZE + ".png"), Pixmap.Format.RGBA8888, true);
        flyer = new Texture(Gdx.files.internal("data/entity/flyer." + Cohesion.TEXTURE_SIZE + ".png"), Pixmap.Format.RGBA8888, true);
        platform = new Texture(Gdx.files.internal("data/platforms/platform." + Cohesion.TEXTURE_SIZE + ".png"), Pixmap.Format.RGBA8888, true);
        projectile = new Texture(Gdx.files.internal("data/entity/projectile." + Cohesion.TEXTURE_SIZE + ".png"), Pixmap.Format.RGBA8888, true);
        blockade = new Texture(Gdx.files.internal("data/platforms/blockade." + Cohesion.TEXTURE_SIZE + ".png"), Pixmap.Format.RGBA8888, true);
        ground = new Texture(Gdx.files.internal("data/platforms/ground." + Cohesion.TEXTURE_SIZE + ".png"), Pixmap.Format.RGBA8888, true);
        mergeIcon = new Texture(Gdx.files.internal("data/icons/merge_icon." + Cohesion.TEXTURE_SIZE + ".png"), Pixmap.Format.RGBA8888, true);

        //Load Shaders
        if(simple == null) {
            ShaderProgram.pedantic = true;

            simple = new ShaderProgram(Gdx.files.internal("data/shaders/simple.vrt"), Gdx.files.internal("data/shaders/simple.frg"));
            colorize = new ShaderProgram(Gdx.files.internal("data/shaders/colorize.vrt"), Gdx.files.internal("data/shaders/colorize.frg"));
            postProcessing = new ShaderProgram(Gdx.files.internal("data/shaders/post.vrt"), Gdx.files.internal("data/shaders/post.frg"));
            blur = new ShaderProgram(Gdx.files.internal("data/shaders/blur.vrt"), Gdx.files.internal("data/shaders/blur.frg"));
            background = new ShaderProgram(Gdx.files.internal("data/shaders/background.vrt"), Gdx.files.internal("data/shaders/background.frg"));

            if (blur.getLog().length() > 0 && !blur.getLog().equals("No errors.\n"))
                System.out.println(blur.getLog());

            if (simple.getLog().length() > 0 && !simple.getLog().equals("No errors.\n"))
                System.out.println(simple.getLog());

            if (colorize.getLog().length() > 0 && !colorize.getLog().equals("No errors.\n"))
                System.out.println(colorize.getLog());

            if (postProcessing.getLog().length() > 0 && !postProcessing.getLog().equals("No errors.\n"))
                System.out.println(postProcessing.getLog());

            if (background.getLog().length() > 0 && !background.getLog().equals("No errors.\n"))
                System.out.println(background.getLog());
        }

        blurA = new FrameBuffer(Pixmap.Format.RGB888, Cohesion.FBO_SIZE, Cohesion.FBO_SIZE, false);
        blurB = new FrameBuffer(Pixmap.Format.RGB888, Cohesion.FBO_SIZE, Cohesion.FBO_SIZE, false);

        buffer = new FrameBuffer(Pixmap.Format.RGBA8888, (int)Cohesion.instance.camera.viewportWidth * Cohesion.AA_AMOUNT, (int)Cohesion.instance.camera.viewportHeight * Cohesion.AA_AMOUNT, false, true); //Super Sampling

        map = new Map();
    }

    public int getWidth() {
        return buffer.getWidth();
    }

    public int getHeight() {
        return buffer.getHeight();
    }

    @Override
    public void render(SpriteBatch batch) {
        //Blur. If possible.
        if(lastFrame != null && blur.isCompiled()) {
            //Blur it.
            blurA.begin();
            batch.setShader(background);
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
            blur.setUniformf("resolution", Cohesion.FBO_SIZE);
            blur.setUniformf("radius", Cohesion.BLUR_RADIUS);
            blur.setUniformf("diffuse", Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 0.99f : 0.93f);

            batch.draw(blurA.getColorBufferTexture(), 0, 0, Cohesion.instance.camera.viewportWidth* Cohesion.AA_AMOUNT, Cohesion.instance.camera.viewportHeight* Cohesion.AA_AMOUNT, 0, 0, blurA.getWidth(), blurA.getHeight(), false, false);
            batch.flush();

            blurB.end();
            batch.end();
        }

        Cohesion.instance.camera.position.set(map.getCentrePoint(), 0);

        Cohesion.instance.camera.update();
        batch.setProjectionMatrix(Cohesion.instance.camera.combined);

        buffer.begin();
        Gdx.gl.glClearColor(0, 0, 0,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        map.update();

        batch.begin();
        if(lastFrame != null) {
            batch.setProjectionMatrix(Cohesion.instance.standardMatrix);
            batch.setShader(blur);
            blur.setUniform2fv("dir", new float[]{0f, 1f}, 0, 2);
            blur.setUniformf("resolution", Cohesion.FBO_SIZE);
            blur.setUniformf("radius", Cohesion.BLUR_RADIUS);
            blur.setUniformf("diffuse", Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 0.99f : 0.93f);
            batch.draw(blurB.getColorBufferTexture(), 0, 0, Cohesion.instance.camera.viewportWidth* Cohesion.AA_AMOUNT, Cohesion.instance.camera.viewportHeight* Cohesion.AA_AMOUNT, 0, 0, blurB.getWidth(), blurB.getHeight(), false, true);

            batch.setProjectionMatrix(Cohesion.instance.camera.combined);
        }

        if(simple.isCompiled())
            batch.setShader(simple);
        else
            batch.setShader(null);

        map.render(batch);
        batch.end();
        buffer.end();

        if(postProcessing.isCompiled())
            batch.setShader(postProcessing);

        batch.setProjectionMatrix(Cohesion.instance.standardMatrix);

        batch.begin();
        batch.draw(lastFrame = buffer.getColorBufferTexture(), 0, 0, Cohesion.instance.camera.viewportWidth* Cohesion.AA_AMOUNT, Cohesion.instance.camera.viewportHeight* Cohesion.AA_AMOUNT, 0, 0, buffer.getWidth(), buffer.getHeight(), false, true);

        map.renderGui(batch);

        batch.end();
    }
}
