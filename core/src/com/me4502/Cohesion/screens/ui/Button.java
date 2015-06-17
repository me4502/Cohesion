package com.me4502.Cohesion.screens.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me4502.Cohesion.Cohesion;

public abstract class Button extends BaseUI {

    String text;

    public Button(int x, int y, int width, int height, String text) {
        super(x, y, width, height);

        this.text = text;
    }

    @Override
    public void drawGuiElement(SpriteBatch batch) {
        Cohesion.instance.mainFont.draw(batch, text, x * Cohesion.AA_AMOUNT, y * Cohesion.AA_AMOUNT);
    }

    public abstract void clickAction();


    //Texture information
    public static Texture mainButtonTexture;

    public static void loadTextures() {

    }
}
