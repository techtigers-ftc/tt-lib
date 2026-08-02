package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.GoBodometrySubsystem;
import org.firstinspires.ftc.teamcode.utils.SlidingAverageCalculator;
import org.firstinspires.ftc.teamcode.utils.Waypoint;

@TeleOp
public class MeasuredRPMTestOpMode extends BaseOpMode {
    private DriveSubsystem drive;
    private double maxVelocity = 0;
    private double maxCurrentDraw = 0;
    private double maxRPM = 0;

    @Override
    protected void initialize() {
        drive = new DriveSubsystem(hardwareMap);
        GoBodometrySubsystem odometry = new GoBodometrySubsystem(hardwareMap, new Waypoint(0, 0, Math.toRadians(0)));

        registerSubsystems(drive, odometry);
    }

    @Override
    protected void update() {
        double velocity = robotState.getRobotVelocity().getPoint().magnitude();
        double currentDraw = robotState.getDriveCurrent();
        double rpm = INSERT RPM CALCULATION HERE;

        if (gamepad1.dpad_up) {
            drive.driveRobotCentric(1, 0, 0);
        } else if (gamepad1.dpad_down) {
            drive.driveRobotCentric(-1, 0, 0);
        } else {
            drive.driveRobotCentric(0, 0, 0);
        }

        if (maxVelocity > velocity) {
            maxVelocity = velocity;
        }
        if (maxCurrentDraw > currentDraw) {
            maxCurrentDraw = currentDraw;
        }
        // add something for max rpm

        telemetry.addLine("Make sure to screen-record your two Panels graphs!");
        telemetry.addData("Max Velocity", )
        telemetry.addData("Current Velocity", velocity);
        telemetry.addData("Current Draw", currentDraw);
    }

    @Override
    protected boolean isBlue() {
        return false;
    }
}
