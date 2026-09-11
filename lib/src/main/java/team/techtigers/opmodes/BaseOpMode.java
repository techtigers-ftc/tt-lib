package team.techtigers.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import team.techtigers.commands.CommandScheduler;
import team.techtigers.gamepad.GamepadEx;
import team.techtigers.subsystems.Subsystem;
import team.techtigers.subsystems.SubsystemController;
import team.techtigers.utils.RobotState;
import team.techtigers.utils.TTLogger;

/**
 * BaseOpMode is an abstract class that extends LinearOpMode and provides a framework for creating
 * custom opmodes. It manages the subsystem controller and runs their appropriate methods.
 */
public abstract class BaseOpMode extends LinearOpMode {
    protected RobotState robotState;
    protected GamepadEx driverGamepad;
    protected GamepadEx manipulatorGamepad;
    private ElapsedTime debounceTimer;
    protected boolean isDebounceOn;

    @Override
    public void runOpMode() throws InterruptedException {
        robotState = new RobotState(isBlue());
        driverGamepad = new GamepadEx(gamepad1);
        manipulatorGamepad = new GamepadEx(gamepad2);

        initialize();
        TTLogger.dd("Base Op Mode", "Is debounce on: %b", isDebounceOn);
        while (opModeInInit()) {
            SubsystemController.getInstance().initLoop();
            if (isDebounceOn) {
                debounceTimer = new ElapsedTime();
                debounceTimer.reset();
            }
        }
        waitForStart();

        SubsystemController.getInstance().justAfterStart();
        justAfterStart();

        while (opModeIsActive()) {
            SubsystemController.getInstance().periodic();
            CommandScheduler.getInstance().update();

            if (isDebounceOn) {
                if (debounceTimer.milliseconds() > 200) {
                    update();
                    telemetry.update();
                    debounceTimer.reset();
                }
            } else {
                update();
                telemetry.update();
            }
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
        CommandScheduler.getInstance().reset();
        SubsystemController.reset();
        SubsystemController.getInstance().registerSubsystems(telemetry, robotState, subsystems);
    }

    /**
     * Initializes the opmode. This method is called once when the opmode is initialized.
     * Subclasses should override this method to perform any necessary initialization.
     */
    protected abstract void initialize();

    protected void initLoop() {
    }

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
