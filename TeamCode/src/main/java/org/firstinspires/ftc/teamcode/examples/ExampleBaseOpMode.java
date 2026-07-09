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
    private ElapsedTime timeBeforeNextCommand;

    @Override
    public void initialize() {
        ExampleSubsystem exampleSubsystem = new ExampleSubsystem(hardwareMap);
        timeBeforeNextCommand = new ElapsedTime();
        registerSubsystems(exampleSubsystem);

        exampleCommand = new ExampleCommand(exampleSubsystem, 1.0);
        exampleSequentialCommand = new ExampleSequentialCommand(exampleSubsystem);
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
        }
        telemetry.addLine("Update Loop Running");
    }


    @Override
    protected boolean isBlue() {
        return false;
    }
}
