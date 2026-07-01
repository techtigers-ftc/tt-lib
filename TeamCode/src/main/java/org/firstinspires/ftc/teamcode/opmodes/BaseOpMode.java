package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.subsystems.SubsystemController;
import org.firstinspires.ftc.teamcode.utils.RobotState;

/**
 * BaseOpMode is an abstract class that extends LinearOpMode and provides a framework for creating
 * custom opmodes. It manages the subsystems controller and runs their appropriate methods.
 */
public abstract class BaseOpMode extends LinearOpMode {
    protected RobotState robotState;
    @Override
    public void runOpMode() throws InterruptedException {
        robotState = new RobotState(isBlue());

        initialize();
        while (opModeInInit()) {
            SubsystemController.getInstance().initLoop();
        }
        waitForStart();

        SubsystemController.getInstance().justAfterStart();
        justAfterStart();

        while(opModeIsActive()) {
            SubsystemController.getInstance().periodic();
            update();
            telemetry.update();
        }
        SubsystemController.getInstance().close();
        close();
    }

    /**
     * Registers the provided subsystems with the SubsystemController.
     *
     * @param subsystems the subsystems to be registered
     */
    protected void registerSubsystems(Subsystem... subsystems) {
        SubsystemController.reset();
        SubsystemController.getInstance().registerSubsystem(telemetry, robotState, subsystems);
    }

    /**
     * Initializes the opmode. This method is called once when the opmode is initialized.
     * Subclasses should override this method to perform any necessary initialization.
     */
    protected abstract void initialize();

    /**
     * Called after the start of the opmode. This method is called once after the start of the opmode.
     */
    protected void justAfterStart() {
    }

    /**
     * Called periodically during the opmode. This method is called repeatedly during the opmode.
     */
    protected void update() {
    }

    /**
     * Cleans up the opmode. This method is called once when the opmode is stopped.
     */
    protected void close() {

    }

    /**
     * Indicates whether the robot is on the blue alliance.
     * Subclasses should override this method to return true if the robot is on the blue alliance,
     * or false if it is on the red alliance.
     *
     * @return true if the robot is on the blue alliance, false otherwise
     */
    protected abstract boolean isBlue();
}
