package org.firstinspires.ftc.teamcode.examples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.SubsystemExecutor;
import org.firstinspires.ftc.teamcode.utils.RobotState;

@TeleOp(name = "Example Linear OpMode", group = "Examples")
public class ExampleLinearOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        ExampleSubsystem exampleSubsystem = new ExampleSubsystem(hardwareMap);
        RobotState robotState = new RobotState(true);

        SubsystemExecutor.reset();
        SubsystemExecutor.getInstance().registerSubsystem(telemetry, robotState, exampleSubsystem);
        SubsystemExecutor.getInstance().initLoop();
        waitForStart();

        SubsystemExecutor.getInstance().justAfterStart();

        while(opModeIsActive()) {
            SubsystemExecutor.getInstance().periodic();
            telemetry.update();
        }
    }
}
