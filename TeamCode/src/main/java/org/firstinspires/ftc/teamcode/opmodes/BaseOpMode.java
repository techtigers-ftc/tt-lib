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
        initialize();
        waitForStart();
        justAfterStart();
        while(opModeIsActive()) {
            update();
        }
        close();
    }

    protected void registerSubsystems(Subsystem... subsystems) {
        SubsystemExecutor.getInstance().registerSubsystem(hardwareMap, telemetry, robotState, subsystems);
    }

    protected void initialize() {
        robotState = new RobotState(isBlue());
        SubsystemExecutor.getInstance().initLoop();
    }

    protected void justAfterStart() {
        SubsystemExecutor.getInstance().justAfterStart();
    }

    protected void update() {
        SubsystemExecutor.getInstance().periodic();
        telemetry.update();
    }

    protected void close() {
        SubsystemExecutor.getInstance().close();
    }

    protected abstract boolean isBlue();
}
