package com.me4502.Cohesion.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me4502.Cohesion.Cohesion;

public class MainMenuScreen extends Screen {

    @Override
    public void initialize() {
    }

    @Override
    public void render(SpriteBatch batch) {

        batch.setProjectionMatrix(Cohesion.instance.standardMatrix);
        batch.begin();
        Cohesion.instance.mainFont.draw(batch, "Click to Play", 50, 200);
        batch.end();
    }

    @Override
    public void mouseClick(int x, int y) {

        Cohesion.instance.screen = new GameScreen();
        Cohesion.instance.screen.initialize();
    }
}
