package org.firstinspires.ftc.teamcode.display;

import org.firstinspires.ftc.teamcode.utils.RobotState;

/**
 * A view for testing the LED driver
 */
public class TestView extends DisplayView {
    /**
     * Constructs a new TestView
     *
     * @param robotState the robot state object
     */
    public TestView(RobotState robotState) {
        super(new DisplayRegion[]{
                new RectangleRegion(0, 0, robotState)
        });
    }
}
