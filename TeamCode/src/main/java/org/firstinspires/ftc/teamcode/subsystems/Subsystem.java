package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.RobotState;

/**
 * Subsystem is an abstract class that represents a subsystem of the robot.
 * It provides methods for initialization, periodic updates, and cleanup.
 */
public abstract class Subsystem {
    protected String tag;
    protected Telemetry telemetry;
    protected RobotState robotState;

    /**
     * Constructs a Subsystem with the specified tag.
     *
     * @param tag the tag for the subsystem
     */
    public Subsystem(String tag){
        this.tag = tag;
    }
    /**
     * Initializes the subsystem. This method is called once when the subsystem is registered.
     */
    public void initLoop() {}
    /**
     * Called after the start of the opmode. This method is called once after the start of the opmode.
     */
    public void justAfterStart() {}
    /**
     * Called periodically during the opmode. This method is called repeatedly during the opmode.
     */
    public void periodic() {}
    /**
     * Cleans up the subsystem. This method is called once when the opmode is stopped.
     */
    public void close() {}

    /**
     * Sets the parameters for the subsystem. This method is called by the SubsystemController when
     * the subsystem is registered.
     *
     * @param telemetry the telemetry object to be used by the subsystem
     * @param robotState the robotState object to be used by the subsystem
     */
    public void setParameters(Telemetry telemetry, RobotState robotState){
        this.telemetry = telemetry;
        this.robotState = robotState;
    };
}
