package org.firstinspires.ftc.teamcode.examples;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.commands.CommandScheduler;
import org.firstinspires.ftc.teamcode.gamepad.GamepadButtons;
import org.firstinspires.ftc.teamcode.opmodes.BaseOpMode;
import org.firstinspires.ftc.teamcode.utils.TTLogger;

@TeleOp(name = "Example Base OpMode", group = "Examples")
public class ExampleBaseOpMode extends BaseOpMode {
    private ExampleCommand exampleCommand;
    private ExampleSequentialCommand exampleSequentialCommand;
    private ExampleParallelCommandGroup exampleParallelCommandGroup;
    private ExampleParallelDeadlineGroup exampleParallelDeadlineGroup;
    private ExampleParallelRaceGroup exampleParallelRaceGroup;
    private ExampleServoAction exampleServoAction;
    private ElapsedTime timeBeforeNextCommand;

    @Override
    public void initialize() {
        ExampleSubsystem exampleSubsystem = new ExampleSubsystem(hardwareMap);
        timeBeforeNextCommand = new ElapsedTime();
        registerSubsystems(exampleSubsystem);

        exampleCommand = new ExampleCommand(exampleSubsystem, 1.0);
        exampleSequentialCommand = new ExampleSequentialCommand(exampleSubsystem);
        exampleParallelCommandGroup = new ExampleParallelCommandGroup(exampleSubsystem);
        exampleParallelDeadlineGroup = new ExampleParallelDeadlineGroup(exampleSubsystem);
        exampleParallelRaceGroup = new ExampleParallelRaceGroup(exampleSubsystem);
        exampleServoAction = new ExampleServoAction(exampleSubsystem, () -> -1.0, 1000);
        timeBeforeNextCommand.reset();

        driverGamepad.getGamepadButton(GamepadButtons.A).whenActive(exampleCommand);
        driverGamepad.getGamepadButton(GamepadButtons.B).whenActive(exampleSequentialCommand);
        driverGamepad.getGamepadButton(GamepadButtons.X).whenActive(exampleParallelCommandGroup);
        driverGamepad.getGamepadButton(GamepadButtons.Y).whenActive(exampleParallelDeadlineGroup);
        driverGamepad.getGamepadButton(GamepadButtons.DPAD_UP).whenActive(exampleParallelRaceGroup);
        driverGamepad.getGamepadButton(GamepadButtons.DPAD_DOWN).whenActive(exampleServoAction);

        TTLogger.setLoggingLevel(TTLogger.DEBUG);
    }

    @Override
    public void update() {
        telemetry.addLine("Update Loop Running");
    }


    @Override
    protected boolean isBlue() {
        return false;
    }
}
