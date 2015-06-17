package com.me4502.Cohesion.screens;

import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.screens.ui.Button;

public class MainMenuScreen extends Screen {

    @Override
    public void initialize() {

        uiComponents.clear();
        uiComponents.add(new Button(320 - 32,300,64,32,"Play") {
            @Override
            public void clickAction(int button) {
                Cohesion.instance.switchScreen(new GameScreen());
            }
        });

        uiComponents.add(new Button(320 - 32,250,64,32,"Options") {
            @Override
            public void clickAction(int button) {
                Cohesion.instance.switchScreen(new OptionsScreen());
            }
        });
    }
}
