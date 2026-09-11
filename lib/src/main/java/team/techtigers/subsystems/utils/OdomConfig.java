package team.techtigers.subsystems.utils;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;

/**
 * A class to hold the configuration of the odometry subsystem
 */
public class OdomConfig {
    public String name = "odo";
    public double xOffset = 0.0;
    public double yOffset = 0.0;
    public GoBildaPinpointDriver.EncoderDirection xDirection = GoBildaPinpointDriver.EncoderDirection.FORWARD;
    public GoBildaPinpointDriver.EncoderDirection yDirection = GoBildaPinpointDriver.EncoderDirection.FORWARD;

    /**
     * Method to set the name of the pinpoint in the hardware map.
     *
     * @param name The name of the pinpoint in the hardware map.
     * @return The current instance of OdomConfig for method chaining.
     */
    public OdomConfig name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the pinpoint's x offset.
     * The X pod offset refers to how far sideways from the tracking point the
     * X (forward) odometry pod is. Left of the center is a positive number,
     * right of center is a negative number
     *
     * @param xOffset the x offset of the pinpoint in inches
     * @return The current instance of OdomConfig for method chaining.
     */
    public OdomConfig xOffset(double xOffset) {
        this.xOffset = xOffset;
        return this;
    }

    /**
     * Sets the pinpoint's y offset.
     * The Y pod offset refers to how far forwards from the tracking point the
     * Y (strafe) odometry pod is. forward of center is a positive number,
     * backwards is a negative number.
     *
     * @param yOffset the y offset of the pinpoint in inches
     * @return The current instance of OdomConfig for method chaining.
     */
    public OdomConfig yOffset(double yOffset) {
        this.yOffset = yOffset;
        return this;
    }

    /**
     * Sets the direction of the x encoder.
     * The X (forward) pod should increase when you move the robot forward.
     *
     * @param xDirection the direction of the x encoder
     * @return The current instance of OdomConfig for method chaining.
     */
    public OdomConfig xDirection(GoBildaPinpointDriver.EncoderDirection xDirection) {
        this.xDirection = xDirection;
        return this;
    }

    /**
     * Sets the direction of the y encoder.
     * The Y (strafe) pod should increase when you move the robot to the left.
     *
     * @param yDirection the direction of the y encoder
     * @return The current instance of OdomConfig for method chaining.
     */
    public OdomConfig yDirection(GoBildaPinpointDriver.EncoderDirection yDirection) {
        this.yDirection = yDirection;
        return this;
    }
}
