package team.techtigers.utils;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

/**
 * A wrapper class for the DcMotorEx class that adds caching functionality to reduce the number of times the motor power is set.
 */
public class CachedMotor {
    private final DcMotorEx motor;
    private double motorCachingThreshold;
    private double previousMotorPower = 0;

    /**
     * Creates a new Motor with the given hardware map, device name, and motor caching threshold.
     *
     * @param hardwareMap           The hardware map, used to get hardware references
     * @param deviceName            The name of the motor in the hardware map
     * @param motorCachingThreshold The minimum change in power required to update the motor power, used to reduce the number of times the motor power is set
     */
    public CachedMotor(HardwareMap hardwareMap, String deviceName, double motorCachingThreshold) {
        motor = hardwareMap.get(DcMotorEx.class, deviceName);
        this.motorCachingThreshold = motorCachingThreshold;
    }

    /**
     * Overload constructor for Motor that uses a default motor caching threshold of 0.01.
     *
     * @param hardwareMap The hardware map, used to get hardware references
     * @param deviceName   The name of the motor in the hardware map
     */
    public CachedMotor(HardwareMap hardwareMap, String deviceName) {
        this(hardwareMap, deviceName, 0.01);
    }

    /**
     * Sets the motor caching threshold
     *
     * @param motorCachingThreshold The minimum change in power required to update the motor power
     * @return The Motor object, for chaining
     */
    public CachedMotor setMotorCachingThreshold(double motorCachingThreshold) {
        this.motorCachingThreshold = motorCachingThreshold;
        return this;
    }

    /**
     * Sets the run mode of the motor
     *
     * @param runMode The run mode to set the motor to
     * @return The Motor object, for chaining
     */
    public CachedMotor setRunMode(DcMotor.RunMode runMode) {
        motor.setMode(runMode);
        return this;
    }

    /**
     * Sets the zero power behavior of the motor
     *
     * @param zeroPowerBehavior The zero power behavior to set the motor to
     * @return The Motor object, for chaining
     */
    public CachedMotor setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        motor.setZeroPowerBehavior(zeroPowerBehavior);
        return this;
    }

    /**
     * Sets the direction of the motor
     *
     * @param direction The direction to set the motor to
     * @return The Motor object, for chaining
     */
    public CachedMotor setDirection(DcMotor.Direction direction) {
        motor.setDirection(direction);
        return this;
    }

    /**
     * Sets the team.techtigers.configuration type of the motor
     *
     * @param configurationType The team.techtigers.configuration type to set the motor to
     * @return The Motor object, for chaining
     */
    public CachedMotor setConfigurationType(MotorConfigurationType configurationType) {
        motor.setMotorType(configurationType);
        return this;
    }

    /**
     * @return the team.techtigers.configuration type of the motor
     */
    public MotorConfigurationType getConfigurationType() {
        return motor.getMotorType();
    }

    /**
     * Sets the power of the motor, using caching to reduce the number of times the motor power is set.
     *
     * @param power The power to set the motor to, between -1 and 1
     */
    public void setPower(double power) {
        power = Range.clip(power, -1, 1);
        if (Math.abs(previousMotorPower - power) > motorCachingThreshold) {
            motor.setPower(power);
            previousMotorPower = power;
        }
    }

    /**
     * @return the current position of the motor in ticks
     */
    public double getPosition() {
        return motor.getCurrentPosition();
    }

    /**
     * @return the current draw of the motor in amps
     */
    public double getCurrent() {
        return motor.getCurrent(CurrentUnit.AMPS);
    }

    /**
     * @return the current power of the motor
     */
    public double getPower() {
        return motor.getPower();
    }

    /**
     * @return the current velocity of the motor in ticks per second
     */
    public double getVelocity() {
        return motor.getVelocity();
    }

    /**
     * Stops the motor by setting its power to 0.
     */
    public void stop() {
        setPower(0);
    }
}
