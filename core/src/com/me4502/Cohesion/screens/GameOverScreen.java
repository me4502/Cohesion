package com.me4502.Cohesion.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.me4502.Cohesion.Cohesion;

public class GameOverScreen extends Screen {

    private static final String TEXT = "Game Over\nClick to Play Again";

    GlyphLayout layout;

    @Override
    public void initialize() {
        super.initialize();

        layout = new GlyphLayout();
        layout.setText(Cohesion.instance.mainFont, TEXT, Color.WHITE, 400, Align.center, false);
    }

    public void render(SpriteBatch batch) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(Cohesion.instance.standardMatrix);
        batch.begin();

        Cohesion.instance.mainFont.draw(batch, layout, Cohesion.instance.camera.viewportWidth / 2 - (int)(layout.width*1.5), Cohesion.instance.camera.viewportHeight / 2 + layout.height);

        batch.end();
    }

    public void mouseClick(int x, int y, int button) {
        Cohesion.instance.switchScreen(new GameScreen());
    }
}
