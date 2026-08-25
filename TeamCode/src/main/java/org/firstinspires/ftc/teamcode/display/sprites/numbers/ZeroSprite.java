package org.firstinspires.ftc.teamcode.display.sprites.numbers;


import org.firstinspires.ftc.teamcode.display.Color;
import org.firstinspires.ftc.teamcode.display.sprites.Sprite;

/**
 * A class which represents a sprite in the shape of a 0
 */
public class ZeroSprite extends Sprite {

    /**
     * Creates a new 0 sprite
     *
     * @param x the x coordinate of the bottom left corner of the sprite within the region
     * @param y the y coordinate of the bottom left corner of the sprite within the region
     */
    public ZeroSprite(int x, int y) {
        super(x, y, 3, 5);
    }

    @Override
    protected void showSprite(Color[][] leds) {
        for (int i = 0; i < getHeight(); i++) {
            leds[getX()][getY() + i] = getColor();
            leds[getX() + 2][getY() + i] = getColor();
        }
        leds[getX() + 1][getY()] = getColor();
        leds[getX() + 1][getY() + 4] = getColor();
    }
}
