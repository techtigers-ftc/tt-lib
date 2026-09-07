package team.techtigers.commands.drive;

import team.techtigers.commands.CommandBase;
import team.techtigers.gamepad.GamepadEx;
import team.techtigers.subsystems.DriveSubsystem;
import team.techtigers.utils.RobotState;

public class ManualDriveCommandRobotCentric extends CommandBase {
    private final DriveSubsystem drive;
    private final RobotState robotState;
    private final GamepadEx driverGamepad;

    /**
     * Constructs a new ManualDriveCommandRobotCentric
     *
     * @param drive         the drive subsystem
     * @param robotState    the robot state
     * @param driverGamepad the driver team.techtigers.gamepad
     */
    public ManualDriveCommandRobotCentric(DriveSubsystem drive,
                                          RobotState robotState, GamepadEx driverGamepad) {
        this.drive = drive;
        this.robotState = robotState;
        this.driverGamepad = driverGamepad;
    }

    @Override
    public void update() {
        drive.driveRobotCentric(driverGamepad.getLeftY(),
                -driverGamepad.getLeftX(), -driverGamepad.getRightX());
    }

    @Override
    public void end(boolean interrupted) {
        drive.driveRobotCentric(0,0,0);
    }
}
