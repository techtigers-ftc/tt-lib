package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.subsystems.SubsystemExecutor;
import org.firstinspires.ftc.teamcode.utils.RobotState;
import org.firstinspires.ftc.teamcode.utils.TTLogger;

public abstract class BaseOpMode extends LinearOpMode {
    protected RobotState robotState;
    @Override
    public void runOpMode() throws InterruptedException {
        robotState = new RobotState(isBlue());
        SubsystemExecutor.getInstance().initLoop();
        initialize();
        waitForStart();
        SubsystemExecutor.getInstance().justAfterStart();
        justAfterStart();
        while(opModeIsActive()) {
            SubsystemExecutor.getInstance().periodic();
            telemetry.update();
            update();
        }
        SubsystemExecutor.getInstance().close();
        close();
    }

    protected void registerSubsystems(Subsystem... subsystems) {
        SubsystemExecutor.reset();
        SubsystemExecutor.getInstance().registerSubsystem(telemetry, robotState, subsystems);
    }

    protected void initialize() {

    }

    protected void justAfterStart() {
    }

    protected void update() {

    }

    protected void close() {

    }

    protected abstract boolean isBlue();
}
