package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.ManualDriveCommand;
import org.firstinspires.ftc.teamcode.commands.dropper.UnsafeDropperSlidesCommand;
import org.firstinspires.ftc.teamcode.commands.intake.ManualIntakeCommand;
import org.firstinspires.ftc.teamcode.gamepad.GamepadEx;
import org.firstinspires.ftc.teamcode.gamepad.GamepadKeys;
import org.firstinspires.ftc.teamcode.gamepad.Trigger;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DropperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.utils.TTLogger;

@TeleOp
public class FullRobotTestOpMode extends BaseOpMode {
    DropperSubsystem dropper;

    @Override
    protected void initialize() {
        DriveSubsystem drive = new DriveSubsystem(hardwareMap);
        dropper = new DropperSubsystem(hardwareMap);
        IntakeSubsystem intake = new IntakeSubsystem(hardwareMap);

        registerSubsystems(drive, dropper, intake);

        Trigger runSlides = new Trigger(() -> manipulatorGamepad.getLeftY() != 0);
        UnsafeDropperSlidesCommand unsafeDropperSlidesCommand = new UnsafeDropperSlidesCommand(dropper, manipulatorGamepad);
        runSlides.whileActiveOnce(unsafeDropperSlidesCommand);

        manipulatorGamepad.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenActive(() -> dropper.setDropperPosition(DropperSubsystem.PITCH_DROP_POSITION));
        manipulatorGamepad.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenActive(() -> dropper.setDropperPosition(DropperSubsystem.PITCH_INTAKE_POSITION));

        Trigger manualIntake = new Trigger(() -> manipulatorGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) != 0 || manipulatorGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) != 0);
        ManualIntakeCommand manualIntakeCommand = new ManualIntakeCommand(manipulatorGamepad, intake);
        manualIntake.whenActive(manualIntakeCommand);

        Trigger runDrive = new Trigger(() -> driverGamepad.getLeftY() != 0 || driverGamepad.getLeftX() != 0 || driverGamepad.getRightX() != 0);
        ManualDriveCommand manualDriveCommand = new ManualDriveCommand(drive, robotState, driverGamepad);
        runDrive.whileActiveOnce(manualDriveCommand);

        TTLogger.setLoggingLevel(TTLogger.DISABLED);
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
