package team.techtigers.utils;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

/**
 * A wrapper class for the CRServo class that adds caching functionality to reduce the number of times the servo power is set.
 */
public class CachedCRServo {
    private final CRServo servo;
    private double servoCachingThreshold;
    private double previousServoPower = 0;

    /**
     * Creates a new CRServo with the given hardware map, device name, and servo caching threshold.
     *
     * @param hardwareMap           The hardware map, used to get hardware references
     * @param deviceName            The name of the servo in the hardware map
     * @param servoCachingThreshold The minimum change in power required to update the servo power, used to reduce the number of times the servo power is set
     */
    public CachedCRServo(HardwareMap hardwareMap, String deviceName, double servoCachingThreshold) {
        servo = hardwareMap.get(CRServo.class, deviceName);
        this.servoCachingThreshold = servoCachingThreshold;
    }

    /**
     * Overload constructor for CRServo that uses a default servo caching threshold of 0.01.
     *
     * @param hardwareMap The hardware map, used to get hardware references
     * @param deviceName The name of the servo in the hardware map
     */
    public CachedCRServo(HardwareMap hardwareMap, String deviceName) {
        this(hardwareMap, deviceName, 0.01);
    }

    /**
     * Sets the servo caching threshold
     *
     * @param servoCachingThreshold The minimum change in power required to update the servo power, used to reduce the number of times the servo power is set
     * @return The CRServo object, for chaining
     */
    public CachedCRServo setServoCachingThreshold(double servoCachingThreshold) {
        this.servoCachingThreshold = servoCachingThreshold;
        return this;
    }

    /**
     * Sets the direction of the servo
     *
     * @param direction The direction to set the servo to
     * @return The CRServo object, for chaining
     */
    public CachedCRServo setDirection(DcMotor.Direction direction) {
        servo.setDirection(direction);
        return this;
    }

    /**
     * Sets the power of the servo, using caching to reduce the number of times the servo power is set.
     *
     * @param power The power to set the servo to, between -1 and 1
     */
    public void setPower(double power) {
        power = Range.clip(power, -1, 1);
        if (Math.abs(previousServoPower - power) > servoCachingThreshold) {
            servo.setPower(power);
            previousServoPower = power;
        }
    }


    /**
     * Stops the servo by setting its power to 0.
     */
    public void stop() {
        setPower(0);
    }
}
