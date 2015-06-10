package com.me4502.Cohesion.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.map.Map;

public class GameScreen extends Screen {

    public FrameBuffer buffer;
    public FrameBuffer blurA, blurB;

    public Texture lastFrame;

    public Map map;

    @Override
    public void initialize() {
        super.initialize();

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
        if(lastFrame != null && Cohesion.instance.blur.isCompiled()) {
            //Blur it.
            blurA.begin();
            batch.setShader(Cohesion.instance.background);
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.begin();

            batch.draw(lastFrame, 0, 0);

            batch.flush();

            blurA.end();
            blurB.begin();
            Gdx.gl.glClearColor(0, 0, 0,1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.setShader(Cohesion.instance.blur);

            Cohesion.instance.blur.setUniform2fv("dir", new float[]{1f, 0f}, 0, 2);
            Cohesion.instance.blur.setUniformf("resolution", Cohesion.FBO_SIZE);
            Cohesion.instance.blur.setUniformf("radius", Cohesion.BLUR_RADIUS);
            Cohesion.instance.blur.setUniformf("diffuse", Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 0.99f : 0.93f);

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
            batch.setShader(Cohesion.instance.blur);
            Cohesion.instance.blur.setUniform2fv("dir", new float[]{0f, 1f}, 0, 2);
            Cohesion.instance.blur.setUniformf("resolution", Cohesion.FBO_SIZE);
            Cohesion.instance.blur.setUniformf("radius", Cohesion.BLUR_RADIUS);
            Cohesion.instance.blur.setUniformf("diffuse", Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 0.99f : 0.93f);
            batch.draw(blurB.getColorBufferTexture(), 0, 0, Cohesion.instance.camera.viewportWidth* Cohesion.AA_AMOUNT, Cohesion.instance.camera.viewportHeight* Cohesion.AA_AMOUNT, 0, 0, blurB.getWidth(), blurB.getHeight(), false, true);

            batch.setProjectionMatrix(Cohesion.instance.camera.combined);
        }

        if(Cohesion.instance.simple.isCompiled())
            batch.setShader(Cohesion.instance.simple);
        else
            batch.setShader(null);

        map.render(batch);
        batch.end();
        buffer.end();

        if(Cohesion.instance.postProcessing.isCompiled())
            batch.setShader(Cohesion.instance.postProcessing);

        batch.setProjectionMatrix(Cohesion.instance.standardMatrix);

        batch.begin();
        batch.draw(lastFrame = buffer.getColorBufferTexture(), 0, 0, Cohesion.instance.camera.viewportWidth* Cohesion.AA_AMOUNT, Cohesion.instance.camera.viewportHeight* Cohesion.AA_AMOUNT, 0, 0, buffer.getWidth(), buffer.getHeight(), false, true);

        map.renderGui(batch);

        batch.end();
    }
}
