package com.me4502.Cohesion.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.me4502.Cohesion.Cohesion;
import com.me4502.Cohesion.screens.ui.Button;

public class OptionsScreen extends Screen {

    @Override
    public void initialize() {

        uiComponents.clear();

        Preferences graphicsPreferences = Gdx.app.getPreferences("graphics");

        uiComponents.add(new Button(320 - 128,300,256,32,"SSAA Amount " + Cohesion.AA_AMOUNT) {
            @Override
            public void clickAction(int button) {
                if(button == Input.Buttons.RIGHT)
                    Cohesion.AA_AMOUNT /= 2;
                else
                    Cohesion.AA_AMOUNT *= 2;

                if(Cohesion.AA_AMOUNT < 1)
                    Cohesion.AA_AMOUNT = 16;
                if(Cohesion.AA_AMOUNT > 16)
                    Cohesion.AA_AMOUNT = 1;

                Cohesion.instance.loadGraphics();
                Cohesion.instance.switchScreen(new OptionsScreen());

                graphicsPreferences.putInteger("AntiAliasing", Cohesion.AA_AMOUNT);
                graphicsPreferences.flush();
            }
        });

        uiComponents.add(new Button(320 - 128,250,256,32,"SHADER QUALITY " + Cohesion.SHADER_QUALITY_LEVEL) {
            @Override
            public void clickAction(int button) {
                if(button == Input.Buttons.RIGHT)
                    Cohesion.SHADER_QUALITY_LEVEL /= 2;
                else
                    Cohesion.SHADER_QUALITY_LEVEL *= 2;

                if(Cohesion.SHADER_QUALITY_LEVEL < 1)
                    Cohesion.SHADER_QUALITY_LEVEL = 64;
                if(Cohesion.SHADER_QUALITY_LEVEL > 64)
                    Cohesion.SHADER_QUALITY_LEVEL = 1;

                Cohesion.instance.loadGraphics();
                Cohesion.instance.switchScreen(new OptionsScreen());

                graphicsPreferences.putInteger("ShaderQuality", Cohesion.SHADER_QUALITY_LEVEL);
                graphicsPreferences.flush();
            }
        });

        uiComponents.add(new Button(320 - 128,200,256,32,"TEXTURE QUALITY " + Cohesion.TEXTURE_SIZE) {
            @Override
            public void clickAction(int button) {
                if(button == Input.Buttons.RIGHT)
                    Cohesion.TEXTURE_SIZE /= 2;
                else
                    Cohesion.TEXTURE_SIZE *= 2;

                if(Cohesion.TEXTURE_SIZE < 32)
                    Cohesion.TEXTURE_SIZE = 256;
                if(Cohesion.TEXTURE_SIZE > 256)
                    Cohesion.TEXTURE_SIZE = 32;

                Cohesion.instance.loadGraphics();
                Cohesion.instance.switchScreen(new OptionsScreen());

                graphicsPreferences.putInteger("TextureQuality", Cohesion.TEXTURE_SIZE);
                graphicsPreferences.flush();
            }
        });
    }
}
