package team.techtigers.commands.drive;

import team.techtigers.commands.CommandBase;
import team.techtigers.gamepad.GamepadEx;
import team.techtigers.subsystems.DriveSubsystem;
import team.techtigers.utils.RobotState;

/**
 * Command for manual driving of the robot in field-centric mode.
 */
public class ManualDriveCommandFieldCentric extends CommandBase {
    private final DriveSubsystem drive;
    private final RobotState robotState;
    private final GamepadEx driverGamepad;

    /**
     * Constructs a new ManualDriveCommandFieldCentric
     *
     * @param drive         the drive subsystem
     * @param robotState    the robot state
     * @param driverGamepad the driver gamepad
     */
    public ManualDriveCommandFieldCentric(DriveSubsystem drive,
                                          RobotState robotState, GamepadEx driverGamepad) {
        this.drive = drive;
        this.robotState = robotState;
        this.driverGamepad = driverGamepad;
    }

    @Override
    public void update() {
        drive.driveFieldCentric(driverGamepad.getLeftY(),
                -driverGamepad.getLeftX(), -driverGamepad.getRightX(), robotState.get("robotPose"));
    }

    @Override
    public void end(boolean interrupted) {
        drive.driveRobotCentric(0,0,0);
    }
}
