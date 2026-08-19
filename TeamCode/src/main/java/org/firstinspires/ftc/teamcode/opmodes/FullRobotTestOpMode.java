package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.ManualDriveCommand;
import org.firstinspires.ftc.teamcode.commands.dropper.UnsafeDropperSlidesCommand;
import org.firstinspires.ftc.teamcode.gamepad.Trigger;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DropperSubsystem;

@TeleOp
public class FullRobotTestOpMode extends BaseOpMode {
    DropperSubsystem dropper;

    @Override
    protected void initialize() {
        DriveSubsystem drive = new DriveSubsystem(hardwareMap);
        dropper = new DropperSubsystem(hardwareMap);

        registerSubsystems(drive, dropper);

        Trigger runSlides = new Trigger(() -> manipulatorGamepad.getLeftY() != 0);
        UnsafeDropperSlidesCommand unsafeDropperSlidesCommand = new UnsafeDropperSlidesCommand(dropper, manipulatorGamepad);
        runSlides.whileActiveOnce(unsafeDropperSlidesCommand);

        Trigger runDrive = new Trigger(() -> driverGamepad.getLeftY() != 0 || driverGamepad.getLeftX() != 0 || driverGamepad.getRightX() != 0);
        ManualDriveCommand manualDriveCommand = new ManualDriveCommand(drive, robotState, driverGamepad);
        runDrive.whileActiveOnce(manualDriveCommand);
    }

    @Override
    protected void update() {
        telemetry.addData("Current Slide Position", dropper.getCurrentSlidePositionInches());
    }

    @Override
    protected boolean isBlue() {
        return false;
    }
}
