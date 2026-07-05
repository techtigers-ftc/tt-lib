package org.firstinspires.ftc.teamcode.examples;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.commands.CommandScheduler;
import org.firstinspires.ftc.teamcode.opmodes.BaseOpMode;
import org.firstinspires.ftc.teamcode.utils.TTLogger;

@TeleOp(name = "Example Base OpMode", group = "Examples")
public class ExampleBaseOpMode extends BaseOpMode {
    private ExampleCommand exampleCommand;
    private ExampleSequentialCommand exampleSequentialCommand;

    @Override
    public void initialize() {
        ExampleSubsystem exampleSubsystem = new ExampleSubsystem(hardwareMap);
        registerSubsystems(exampleSubsystem);

        exampleCommand = new ExampleCommand(exampleSubsystem, 1.0);
        exampleSequentialCommand = new ExampleSequentialCommand(exampleSubsystem);
        TTLogger.setLoggingLevel(TTLogger.DEBUG);
    }

    @Override
    public void update() {
        if (gamepad1.a){
            CommandScheduler.getInstance().schedule(exampleCommand);
        }

//        else if (gamepad1.b) {
//            CommandScheduler.getInstance().schedule(exampleSequentialCommand);
//        }
        telemetry.addLine("Update Loop Running");
    }


    @Override
    protected boolean isBlue() {
        return false;
    }
}
