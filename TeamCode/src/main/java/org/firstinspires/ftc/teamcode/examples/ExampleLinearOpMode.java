package org.firstinspires.ftc.teamcode.examples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.SubsystemExecutor;
import org.firstinspires.ftc.teamcode.utils.RobotState;

public class ExampleLinearOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
        RobotState robotState = new RobotState(true);

        SubsystemExecutor.getInstance().registerSubsystem(hardwareMap, telemetry, robotState, exampleSubsystem);
        SubsystemExecutor.getInstance().initLoop();
        waitForStart();

        SubsystemExecutor.getInstance().justAfterStart();

        while(opModeIsActive()) {
            SubsystemExecutor.getInstance().periodic();
            telemetry.update();
        }
    }
}
