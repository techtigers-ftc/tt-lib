package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.ManualDriveCommand;
import org.firstinspires.ftc.teamcode.commands.dropper.DropperIntakeAction;
import org.firstinspires.ftc.teamcode.commands.dropper.DropperOuttakeAction;
import org.firstinspires.ftc.teamcode.commands.dropper.DropperSlidesRelativeCommand;
import org.firstinspires.ftc.teamcode.commands.dropper.UnsafeDropperSlidesCommand;
import org.firstinspires.ftc.teamcode.commands.intake.IntakeAction;
import org.firstinspires.ftc.teamcode.commands.intake.ManualIntakeCommand;
import org.firstinspires.ftc.teamcode.gamepad.GamepadKeys;
import org.firstinspires.ftc.teamcode.gamepad.Trigger;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DropperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.utils.TTLogger;

@TeleOp
public class TeleOpMode extends BaseOpMode {
    DropperSubsystem dropper;

    @Override
    protected void initialize() {
        DriveSubsystem drive = new DriveSubsystem(hardwareMap);
        dropper = new DropperSubsystem(hardwareMap);
        IntakeSubsystem intake = new IntakeSubsystem(hardwareMap);

        registerSubsystems(drive, dropper, intake);

        Trigger runSlides = new Trigger(() -> manipulatorGamepad.getLeftY() != 0);
        DropperSlidesRelativeCommand unsafeDropperSlidesCommand = new DropperSlidesRelativeCommand(dropper, () -> manipulatorGamepad.getLeftY() * 4);
        runSlides.whileActiveOnce(unsafeDropperSlidesCommand);

        DropperOuttakeAction dropperOuttakeAction = new DropperOuttakeAction(dropper);
        DropperIntakeAction dropperIntakeAction = new DropperIntakeAction(dropper);
        manipulatorGamepad.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenActive(dropperOuttakeAction);
        manipulatorGamepad.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenActive(dropperIntakeAction);

        Trigger manualIntake = new Trigger(() -> manipulatorGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) != 0 || manipulatorGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) != 0);
        ManualIntakeCommand intakeCommand = new ManualIntakeCommand(manipulatorGamepad, intake);
        manualIntake.whileActiveOnce(intakeCommand);
        IntakeAction intakeAction = new IntakeAction(intake, dropper);
        manipulatorGamepad.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER).toggleWhenActive(intakeAction);

        manipulatorGamepad.getGamepadButton(GamepadKeys.Button.DPAD_LEFT).toggleWhenActive(() -> dropper.setDropperServoPowers(1),
                () -> dropper.setDropperServoPowers(0));

        manipulatorGamepad.getGamepadButton(GamepadKeys.Button.DPAD_RIGHT).toggleWhenActive(() -> dropper.setDropperServoPowers(-1),
                () -> dropper.setDropperServoPowers(0));

        manipulatorGamepad.getGamepadButton(GamepadKeys.Button.A).whenActive(() -> dropper.setDropperServoPowers(-1));
        manipulatorGamepad.getGamepadButton(GamepadKeys.Button.B).whenActive(() -> dropper.setDropperServoPowers(0));

        Trigger runDrive = new Trigger(() -> driverGamepad.getLeftY() != 0 || driverGamepad.getLeftX() != 0 || driverGamepad.getRightX() != 0);
        ManualDriveCommand manualDriveCommand = new ManualDriveCommand(drive, robotState, driverGamepad);
        runDrive.whileActiveOnce(manualDriveCommand);

        TTLogger.setLoggingLevel(TTLogger.DISABLED);
    }

    @Override
    protected void update() {
        telemetry.addData("Gamepad", manipulatorGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER));
        telemetry.addData("Dropper Position", dropper.getCurrentSlidePositionInches());
        telemetry.addData("Current Slide Position", dropper.getCurrentSlidePositionInches());
    }

    @Override
    protected boolean isBlue() {
        return false;
    }
}
