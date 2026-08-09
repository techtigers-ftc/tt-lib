package org.firstinspires.ftc.teamcode.opmodes;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.GoBodometrySubsystem;
import org.firstinspires.ftc.teamcode.utils.RobotState;
import org.firstinspires.ftc.teamcode.utils.TTLogger;


@TeleOp(name = "Localization Tuning", group = "Tuning")
public class LocalizationTuningOpMode extends BaseOpMode {
    private double lastLoopTime;
    JoinedTelemetry joinedTelemetry = new JoinedTelemetry(PanelsTelemetry.INSTANCE.getFtcTelemetry(), telemetry);

    @Override
    protected void initialize() {
        GoBodometrySubsystem odometry = new GoBodometrySubsystem(hardwareMap, new Pose(8, -63, Math.toRadians(90)));

        registerSubsystems(odometry);

        TTLogger.setLoggingLevel(TTLogger.DISABLED);
    }

    @Override
    protected void update() {
        Pose robotPose = robotState.getRobotPose();
        Pose robotVelocity = robotState.getRobotVelocity();
        double loop = System.nanoTime();
        joinedTelemetry.addData("Cycle Time (ms)", (loop - lastLoopTime) / 1e6);
        lastLoopTime = loop;
        joinedTelemetry.addData("Robot Pos X: ", robotPose.getX());
        joinedTelemetry.addData("Robot Pos Y: ", robotPose.getY());
        joinedTelemetry.addData("Robot Pos Heading (deg): ",
                Math.toDegrees(robotPose.getHeading()));
        joinedTelemetry.addData("Robot Vel X: ", robotVelocity.getX());
        joinedTelemetry.addData("Robot Vel Y: ", robotVelocity.getY());
        joinedTelemetry.addData("Robot Vel Heading (deg): ",
                Math.toDegrees(robotVelocity.getHeading()));

        joinedTelemetry.update();
    }

    @Override
    protected boolean isBlue() {
        return false;
    }
}
