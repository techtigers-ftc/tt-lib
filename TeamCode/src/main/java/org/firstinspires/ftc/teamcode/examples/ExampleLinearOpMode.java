package org.firstinspires.ftc.teamcode.examples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.SubsystemController;
import org.firstinspires.ftc.teamcode.utils.RobotState;

@TeleOp(name = "Example Linear OpMode", group = "Examples")
public class ExampleLinearOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        ExampleSubsystem exampleSubsystem = new ExampleSubsystem(hardwareMap);
        RobotState robotState = new RobotState(true);

        SubsystemController.reset();
        SubsystemController.getInstance().registerSubsystem(telemetry, robotState, exampleSubsystem);
        SubsystemController.getInstance().initLoop();
        waitForStart();

        SubsystemController.getInstance().justAfterStart();

        while(opModeIsActive()) {
            SubsystemController.getInstance().periodic();
            telemetry.update();
        }
    }
}
