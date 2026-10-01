package org.firstinspires.ftc.teamcode.display;

import org.firstinspires.ftc.teamcode.display.sprites.RectangleSprite;
import org.firstinspires.ftc.teamcode.display.sprites.Sprite;
import org.firstinspires.ftc.teamcode.utils.RobotState;

/**
 * A region that shows a big blue rectangle
 */
public class RectangleRegion extends DisplayRegion {
    private final RectangleSprite rectangle;
    private final Sprite[] sprites;
    private RobotState robotState;

    /**
     * Constructs a new RectangleRegion
     *
     * @param x the x coordinate of the region
     * @param y the y coordinate of the region
     * @param robotState the robot state used to check whether the sprite should be showing or not
     */
    public RectangleRegion(int x, int y, RobotState robotState) {
        super(x, y, 12, 8);
        this.robotState = robotState;
        rectangle = new RectangleSprite(0, 0, 12, 8);
        rectangle.setColor(Color.BLUE);
        sprites = new Sprite[] {rectangle};
    }

    @Override
    public void update() {
        if (robotState.isLedShowing()) {
            rectangle.enable();
        } else {
            rectangle.disable();
        }
    }

    @Override
    protected Sprite[] getSprites() {
        return this.sprites;
    }
}
