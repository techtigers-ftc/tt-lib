package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.subsystems.SubsystemController;
import org.firstinspires.ftc.teamcode.utils.RobotState;

public abstract class BaseOpMode extends LinearOpMode {
    protected RobotState robotState;
    @Override
    public void runOpMode() throws InterruptedException {
        robotState = new RobotState(isBlue());

        initialize();
        while (opModeInInit()) {
            SubsystemController.getInstance().initLoop();
        }
        waitForStart();

        SubsystemController.getInstance().justAfterStart();
        justAfterStart();

        while(opModeIsActive()) {
            SubsystemController.getInstance().periodic();
            update();
            telemetry.update();
        }
        SubsystemController.getInstance().close();
        close();
    }

    protected void registerSubsystems(Subsystem... subsystems) {
        SubsystemController.reset();
        SubsystemController.getInstance().registerSubsystem(telemetry, robotState, subsystems);
    }

    protected abstract void initialize();

    protected void justAfterStart() {
    }

    protected void update() {
    }

    protected void close() {

    }

    protected abstract boolean isBlue();
}
