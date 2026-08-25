package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.gamepad.GamepadEx;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.utils.RobotState;

public class ManualDriveCommand extends CommandBase {
    private final DriveSubsystem drive;
    private final RobotState robotState;
    private final GamepadEx driverGamepad;

    /**
     * Constructs a new PedroManualDriveCommand
     *
     * @param drive         the drive subsystem
     * @param robotState    the robot state
     * @param driverGamepad the driver gamepad
     */
    public ManualDriveCommand(DriveSubsystem drive,
                              RobotState robotState, GamepadEx driverGamepad) {
        this.drive = drive;
        this.robotState = robotState;
        this.driverGamepad = driverGamepad;
    }

    @Override
    public void update() {
        drive.driveRobotCentric(driverGamepad.getLeftY(),
                driverGamepad.getLeftX(), driverGamepad.getRightX());
    }
}
