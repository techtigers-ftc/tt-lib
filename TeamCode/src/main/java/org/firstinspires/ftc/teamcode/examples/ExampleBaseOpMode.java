package org.firstinspires.ftc.teamcode.examples;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.gamepad.GamepadKeys;
import org.firstinspires.ftc.teamcode.gamepad.Trigger;
import org.firstinspires.ftc.teamcode.opmodes.BaseOpMode;
import org.firstinspires.ftc.teamcode.utils.TTLogger;

/**
 * Demonstrates commands and command groups in a BaseOpMode.
 */
@TeleOp(name = "Example Base OpMode", group = "Examples")
public class ExampleBaseOpMode extends BaseOpMode {
    private ExampleCommand exampleCommand;
    private ExampleSequentialCommand exampleSequentialCommand;
    private ExampleParallelCommandGroup exampleParallelCommandGroup;
    private ExampleParallelDeadlineGroup exampleParallelDeadlineGroup;
    private ExampleParallelRaceGroup exampleParallelRaceGroup;
    private ExampleServoAction exampleServoAction;

    @Override
    public void initialize() {
        ExampleSubsystem exampleSubsystem = new ExampleSubsystem(hardwareMap);
        registerSubsystems(exampleSubsystem);

        exampleCommand = new ExampleCommand(exampleSubsystem, 1.0);
        exampleSequentialCommand = new ExampleSequentialCommand(exampleSubsystem);
        exampleParallelCommandGroup = new ExampleParallelCommandGroup(exampleSubsystem);
        exampleParallelDeadlineGroup = new ExampleParallelDeadlineGroup(exampleSubsystem);
        exampleParallelRaceGroup = new ExampleParallelRaceGroup(exampleSubsystem);
        exampleServoAction = new ExampleServoAction(exampleSubsystem, () -> -1.0, 1000);
        driverGamepad.getGamepadButton(GamepadKeys.Button.A).whenActive(exampleCommand);
        driverGamepad.getGamepadButton(GamepadKeys.Button.B).whenActive(exampleSequentialCommand);
        driverGamepad.getGamepadButton(GamepadKeys.Button.X).whenActive(exampleParallelCommandGroup);
        driverGamepad.getGamepadButton(GamepadKeys.Button.Y).whenActive(exampleParallelDeadlineGroup);
        driverGamepad.getGamepadButton(GamepadKeys.Button.DPAD_UP).whenActive(exampleParallelRaceGroup);
        driverGamepad.getGamepadButton(GamepadKeys.Button.DPAD_DOWN).whenActive(exampleServoAction);

        Trigger leftTrigger = new Trigger(() -> driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) != 0.0);
        leftTrigger.whenActive(new ExampleCommand(exampleSubsystem, 0.5));

        TTLogger.setLoggingLevel(TTLogger.DEBUG);
    }

    @Override
    public void update() {
        telemetry.addData("Left Stick", "x: %.2f, y: %.2f",
                driverGamepad.getLeftX(), driverGamepad.getLeftY());
        telemetry.addData("Right Stick", "x: %.2f, y: %.2f",
                driverGamepad.getRightX(), driverGamepad.getRightY());
        telemetry.addData("Left Trigger", "%.2f",
                driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER));
        telemetry.addData("Right Trigger", "%.2f",
                driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER));
    }


    @Override
    protected boolean isBlue() {
        return false;
    }
}
