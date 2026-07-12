package org.firstinspires.ftc.teamcode.examples;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.commands.CommandScheduler;
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
        TTLogger.setLoggingLevel(TTLogger.DEBUG);
        timeBeforeNextCommand.reset();
    }

    @Override
    public void update() {
        if (gamepad1.a && timeBeforeNextCommand.seconds() > 0.5){
            timeBeforeNextCommand.reset();
            CommandScheduler.getInstance().schedule(exampleCommand);
        } else if (gamepad1.b) {
            CommandScheduler.getInstance().schedule(exampleSequentialCommand);
        } else if (gamepad1.x){
            CommandScheduler.getInstance().schedule(exampleParallelCommandGroup);
        } else if (gamepad1.y){
            CommandScheduler.getInstance().schedule(exampleParallelDeadlineGroup);
        } else if (gamepad1.dpad_up) {
            CommandScheduler.getInstance().schedule(exampleParallelRaceGroup);
        } else if (gamepad1.dpad_down) {
            CommandScheduler.getInstance().schedule(exampleServoAction);
        }
        telemetry.addLine("Update Loop Running");
    }


    @Override
    protected boolean isBlue() {
        return false;
    }
}
