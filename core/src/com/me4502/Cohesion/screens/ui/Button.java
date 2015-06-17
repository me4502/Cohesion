package com.me4502.Cohesion.screens.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.me4502.Cohesion.Cohesion;

public abstract class Button extends BaseUI {

    String text;

    public Button(int x, int y, int width, int height, String text) {
        super(x, y, width, height);

        this.text = text;
    }

    @Override
    public void drawGuiElement(SpriteBatch batch) {

        int ax = x * Cohesion.AA_AMOUNT;
        int ay = y * Cohesion.AA_AMOUNT;

        int size = 4 * Cohesion.AA_AMOUNT;

        for(int xx = ax; xx < ax + width * Cohesion.AA_AMOUNT; xx += size) {
            for(int yy = ay; yy > ay - height * Cohesion.AA_AMOUNT; yy -= size) {
                if (xx == ax && yy == ay)
                    batch.draw(corner, xx, yy, size, size);
                else if (xx == ax && yy-size <= ay - height * Cohesion.AA_AMOUNT)
                    batch.draw(corner, size, size, new Affine2().setToTrnRotScl(xx + size, yy, 90, 1, 1));
                else if (xx+size >= ax + width * Cohesion.AA_AMOUNT && yy == ay)
                    batch.draw(corner, size, size, new Affine2().setToTrnRotScl(xx, yy + size, 270, 1, 1));
                else if (xx+size >= ax + width * Cohesion.AA_AMOUNT && yy-size <= ay - height * Cohesion.AA_AMOUNT)
                    batch.draw(corner, size, size, new Affine2().setToTrnRotScl(xx + size, yy + size, 180, 1, 1));
                else if(yy == ay)
                    batch.draw(top, xx, yy, size, size);
                else if(yy-size <= ay - height * Cohesion.AA_AMOUNT)
                    batch.draw(top, size, size, new Affine2().setToTrnRotScl(xx + size, yy + size, 180, 1, 1));
                else if(xx == ax)
                    batch.draw(side, xx, yy, size, size);
                else if(xx+size >= ax + width * Cohesion.AA_AMOUNT)
                    batch.draw(side, size, size, new Affine2().setToTrnRotScl(xx+size, yy+size, 180, 1, 1));
                else
                    batch.draw(centre, xx, yy, size, size);
            }
        }

        Cohesion.instance.mainFont.draw(batch, text, ax + 24, ay);
    }

    public abstract void clickAction();


    //Texture information
    public static Texture mainButtonTexture;
    public static TextureRegion corner;
    public static TextureRegion top;
    public static TextureRegion side;
    public static TextureRegion centre;

    public static void loadTextures() {

        mainButtonTexture = new Texture(Gdx.files.internal("data/ui/button." + Cohesion.TEXTURE_SIZE + ".png"), Pixmap.Format.RGBA8888, true);

        corner = new TextureRegion(mainButtonTexture, 0, 0, mainButtonTexture.getWidth()/2, mainButtonTexture.getHeight()/2);
        top = new TextureRegion(mainButtonTexture, mainButtonTexture.getWidth()/2, 0, mainButtonTexture.getWidth()/2, mainButtonTexture.getHeight()/2);
        side = new TextureRegion(mainButtonTexture, 0, mainButtonTexture.getHeight()/ 2, mainButtonTexture.getWidth()/2, mainButtonTexture.getHeight()/2);
        centre = new TextureRegion(mainButtonTexture, mainButtonTexture.getWidth()/2, mainButtonTexture.getHeight()/ 2, mainButtonTexture.getWidth()/2, mainButtonTexture.getHeight()/ 2);
    }
}
