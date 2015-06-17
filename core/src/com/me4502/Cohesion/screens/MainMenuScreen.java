package com.me4502.Cohesion.screens;

import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.screens.ui.Button;

public class MainMenuScreen extends Screen {

    @Override
    public void initialize() {

        uiComponents.clear();
        uiComponents.add(new Button(50,200,10,10,"Cake") {
            @Override
            public void clickAction() {
                Cohesion.instance.screen = new GameScreen();
                Cohesion.instance.screen.initialize();
            }
        });
    }
}
