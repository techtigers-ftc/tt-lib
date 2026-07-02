package org.firstinspires.ftc.teamcode.opmodes;

import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.GoBodometrySubsystem;
import org.firstinspires.ftc.teamcode.utils.Waypoint;

public class DrivetrainTestingOpMode extends BaseOpMode {
    private double maxVelocity = 0;
    private double maxAcceleration = 0;
    private double maxCurrentDraw = 0;
    private DriveSubsystem drive;

    @Override
    protected void initialize() {
        drive = new DriveSubsystem(hardwareMap);
        GoBodometrySubsystem odometry = new GoBodometrySubsystem(hardwareMap, new Waypoint(0, 0, 0));

        registerSubsystems(drive, odometry);
    }

    @Override
    protected void update() {
        drive.driveRobotCentric(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

        double velocity = robotState.getRobotVelocity().getPoint().magnitude();
        // TODO: Make sure that you are using the correct axis for acceleration based on the testing
        //  procedures document and the way the control hub is actually mounted
        double acceleration = robotState.getRobotAcceleration().xAccel;
        double currentDraw = robotState.getDriveCurrent();

        if (velocity > maxVelocity) {
            maxVelocity = velocity;
        }
        if (acceleration > maxAcceleration) {
            maxAcceleration = acceleration;
        }
        if (currentDraw > maxCurrentDraw) {
            maxCurrentDraw = currentDraw;
        }

        telemetry.addData("Current Velocity", velocity);
        telemetry.addData("Current Acceleration", acceleration);
        telemetry.addData("Current Draw", currentDraw);

        telemetry.addData("Max Velocity", maxVelocity);
        telemetry.addData("Max Acceleration", maxAcceleration);
        telemetry.addData("Max Current Draw", maxCurrentDraw);
    }

    @Override
    protected boolean isBlue() {
        return false;
    }
}
