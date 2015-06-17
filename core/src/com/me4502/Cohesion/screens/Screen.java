package com.me4502.Cohesion.screens;

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

        batch.setProjectionMatrix(Cohesion.instance.standardMatrix);
        batch.begin();
        for(BaseUI ui : uiComponents) {
            ui.drawGuiElement(batch);
        }
        batch.end();
    }

    public void mouseClick(int x, int y) {

        for(BaseUI ui : uiComponents) {
            ui.mouseClick(x, y);
        }
    }

    public void mouseRelease(int x, int y) {

        for(BaseUI ui : uiComponents) {
            ui.mouseReleased(x, y);
        }
    }
}
