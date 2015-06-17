package com.me4502.Cohesion.screens.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class BaseUI {
    public int x, y;
    public int width, height;

    public boolean clicked;
    public boolean focused;

    boolean removable;

    public BaseUI(int x, int y, int width, int height) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void drawGuiElement(SpriteBatch batch);

    public boolean mouseClick(int mouseX, int mouseY) {

        if (mouseX >= x && mouseX <= x + width) {
            if (mouseY >= y && mouseY <= y + height) {
                clicked = true;
                return true;
            }
        }

        System.out.println(mouseX + "," + mouseY);

        clicked = false;
        return false;
    }

    public boolean mouseReleased(int mouseX, int mouseY) {

        if (clicked) {
            // Still in bounds of the object?
            if (mouseX >= x && mouseX <= x + width) {
                if (mouseY >= y && mouseY <= y + height) {
                    clickAction();
                    return true;
                }
            }

            clicked = false;
        }

        return false;
    }

    public void mouseClickMove(int mouseX, int mouseY, int mouseButton) {

    }

    public abstract void clickAction();

    public void keyTyped(char character, int keyCode) {

    }

    public void update() {

    }

    public boolean isRemovable() {
        return removable;
    }

}
