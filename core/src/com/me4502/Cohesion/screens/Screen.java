package com.me4502.Cohesion.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.screens.ui.BaseUI;

import java.util.ArrayList;
import java.util.List;

public abstract class Screen {

    List<BaseUI> uiComponents = new ArrayList<>();

    public void initialize() {

    }

    public void render(SpriteBatch batch) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(Cohesion.instance.standardMatrix);
        batch.begin();
        for(BaseUI ui : uiComponents) {
            ui.drawGuiElement(batch);
        }
        batch.end();
    }

    public void mouseClick(int x, int y, int button) {

        for(BaseUI ui : uiComponents) {
            ui.mouseClick(x, y, button);
        }
    }

    public void mouseRelease(int x, int y, int button) {

        for(BaseUI ui : uiComponents) {
            ui.mouseReleased(x, y, button);
        }
    }

    public void mouseDragged(int x, int y) {
        for(BaseUI ui : uiComponents) {
            ui.mouseDragged(x, y);
        }
    }

    public void onKeyPress(int keyCode) {
        if(keyCode == Input.Keys.ESCAPE) {
            Cohesion.instance.switchScreen(new MainMenuScreen());
        }
    }

    public void dispose() {
        //uiComponents.clear();
    }
}
