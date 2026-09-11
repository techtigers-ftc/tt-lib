package team.techtigers.subsystems.utils;

import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * A class to hold all the config for the drive subsystem
 */
public class DriveConfig {
    public String fl = "front_left";
    public String fr = "front_right";
    public String bl = "back_left";
    public String br = "back_right";
    public DcMotorSimple.Direction flDirection = DcMotorSimple.Direction.FORWARD;
    public DcMotorSimple.Direction frDirection = DcMotorSimple.Direction.FORWARD;
    public DcMotorSimple.Direction blDirection = DcMotorSimple.Direction.FORWARD;
    public DcMotorSimple.Direction brDirection = DcMotorSimple.Direction.FORWARD;
    public boolean readCurrent = false; // whether to read drivetrain current
    public double drivetrainCurrentReadInterval = 100; // time in ms between reads of drivetrain current

    /**
     * Method to set the name of the front left motor.
     *
     * @param motorName The name of the front left motor.
     * @return The current instance of DriveConfig for method chaining.
     */
    public DriveConfig fl(String motorName) {
        this.fl = motorName;
        return this;
    }

    /**
     * Method to set the name of the front right motor.
     *
     * @param motorName The name of the front right motor.
     * @return The current instance of DriveConfig for method chaining.
     */
    public DriveConfig fr(String motorName) {
        this.fr = motorName;
        return this;
    }

    /**
     * Method to set the name of the back left motor.
     *
     * @param motorName The name of the back left motor.
     * @return The current instance of DriveConfig for method chaining.
     */
    public DriveConfig bl(String motorName) {
        this.bl = motorName;
        return this;
    }

    /**
     * Method to set the name of the back right motor.
     *
     * @param motorName The name of the back right motor.
     * @return The current instance of DriveConfig for method chaining.
     */
    public DriveConfig br(String motorName) {
        this.br = motorName;
        return this;
    }

    /**
     * Method to set the direction of the front left motor.
     *
     * @param direction The direction of the front left motor.
     * @return The current instance of DriveConfig for method chaining.
     */
    public DriveConfig flDirection(DcMotorSimple.Direction direction) {
        this.flDirection = direction;
        return this;
    }

    /**
     * Method to set the direction of the front right motor.
     *
     * @param direction The direction of the front right motor.
     * @return The current instance of DriveConfig for method chaining.
     */
    public DriveConfig frDirection(DcMotorSimple.Direction direction) {
        this.frDirection = direction;
        return this;
    }

    /**
     * Method to set the direction of the back left motor.
     *
     * @param direction The direction of the back left motor.
     * @return The current instance of DriveConfig for method chaining.
     */
    public DriveConfig blDirection(DcMotorSimple.Direction direction) {
        this.blDirection = direction;
        return this;
    }

    /**
     * Method to set the direction of the back right motor.
     *
     * @param direction The direction of the back right motor.
     * @return The current instance of DriveConfig for method chaining.
     */
    public DriveConfig brDirection(DcMotorSimple.Direction direction) {
        this.brDirection = direction;
        return this;
    }

    /**
     * Method to set whether to read drivetrain current.
     *
     * @param readCurrent Whether to read drivetrain current.
     * @return The current instance of DriveConfig for method chaining.
     */
    public DriveConfig readCurrent(boolean readCurrent) {
        this.readCurrent = readCurrent;
        return this;
    }

    /**
     * Method to set the interval for reading drivetrain current.
     *
     * @param interval The interval in milliseconds for reading drivetrain current.
     * @return The current instance of DriveConfig for method chaining.
     */
    public DriveConfig drivetrainCurrentReadInterval(double interval) {
        this.drivetrainCurrentReadInterval = interval;
        return this;
    }
}
