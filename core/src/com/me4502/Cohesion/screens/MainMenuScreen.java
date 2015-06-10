package com.me4502.Cohesion.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me4502.Cohesion.Cohesion;

public class MainMenuScreen extends Screen {

    @Override
    public void render(SpriteBatch batch) {

    }

    @Override
    public void mouseClick(int x, int y) {

        Cohesion.instance.screen = new GameScreen();
        Cohesion.instance.screen.initialize();
    }
}
