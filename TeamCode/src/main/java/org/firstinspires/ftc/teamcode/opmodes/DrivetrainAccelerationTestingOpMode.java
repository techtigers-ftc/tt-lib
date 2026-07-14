package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.GoBodometrySubsystem;
import org.firstinspires.ftc.teamcode.utils.TTLogger;
import org.firstinspires.ftc.teamcode.utils.Waypoint;

@TeleOp
public class DrivetrainAccelerationTestingOpMode extends BaseOpMode {
    private DriveSubsystem drive;
    private double accelerationTime;
    private ElapsedTime time;

    @Override
    protected void initialize() {
        drive = new DriveSubsystem(hardwareMap);
        GoBodometrySubsystem odometry = new GoBodometrySubsystem(hardwareMap, new Waypoint(0, 0, Math.toRadians(0)));

        registerSubsystems(drive, odometry);
    }

    @Override
    protected void justAfterStart() {
        time.reset();
    }

    @Override
    protected void update() {
        double velocity = robotState.getRobotVelocity().getPoint().magnitude();
        double currentDraw = robotState.getDriveCurrent();

        if (velocity < 60) {
            drive.driveRobotCentric(1.0, 0, 0);
        } else {
            drive.driveRobotCentric(0, 0, 0);
            accelerationTime = time.seconds();
        }

        telemetry.addData("Acceleration Time", accelerationTime);
        telemetry.addData("Current Velocity", velocity);
        telemetry.addData("Current Draw", currentDraw);


        TTLogger.setLoggingLevel(TTLogger.DEBUG);
    }

    @Override
    protected boolean isBlue() {
        return false;
    }
}
