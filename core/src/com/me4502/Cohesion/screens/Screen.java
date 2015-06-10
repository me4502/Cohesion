package com.me4502.Cohesion.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Screen {

    public void initialize() {

    }

    public abstract void render(SpriteBatch batch);

    public void mouseClick(int x, int y) {

    }
}
