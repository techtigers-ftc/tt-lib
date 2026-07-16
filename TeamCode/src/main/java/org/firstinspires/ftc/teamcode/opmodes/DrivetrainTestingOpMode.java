package org.firstinspires.ftc.teamcode.opmodes;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.GoBodometrySubsystem;
import org.firstinspires.ftc.teamcode.utils.TTLogger;
import org.firstinspires.ftc.teamcode.utils.Waypoint;

@TeleOp
public class DrivetrainTestingOpMode extends BaseOpMode {
    private double maxVelocity = 0;
    private double maxCurrentDraw = 0;
    private DriveSubsystem drive;
    JoinedTelemetry joinedTelemetry = new JoinedTelemetry(PanelsTelemetry.INSTANCE.getFtcTelemetry(), telemetry);;

    @Override
    protected void initialize() {
        drive = new DriveSubsystem(hardwareMap);
        GoBodometrySubsystem odometry = new GoBodometrySubsystem(hardwareMap, new Waypoint(0, 0, Math.toRadians(0)));

        registerSubsystems(drive, odometry);
    }

    @Override
    protected void update() {
        drive.driveRobotCentric(gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);

        double velocity = robotState.getRobotVelocity().getPoint().magnitude();
        double currentDraw = robotState.getDriveCurrent();

        if (velocity > maxVelocity) {
            maxVelocity = velocity;
        }
        if (currentDraw > maxCurrentDraw) {
            maxCurrentDraw = currentDraw;
        }

        if (gamepad1.a) {
           drive.setMotorPowers(1, 0, 0, 0);
        } else if (gamepad1.b) {
            drive.setMotorPowers(0, 1, 0, 0);
        } else if (gamepad1.x) {
            drive.setMotorPowers(0, 0, 1, 0);
        } else if (gamepad1.y) {
            drive.setMotorPowers(0, 0, 0, 1);
        }

        joinedTelemetry.addData("Current Velocity", velocity);
        joinedTelemetry.addData("Current Draw", currentDraw);

        joinedTelemetry.addData("Max Velocity", maxVelocity);
        joinedTelemetry.addData("Max Current Draw", maxCurrentDraw);
        joinedTelemetry.update();

        TTLogger.setLoggingLevel(TTLogger.DEBUG);
    }

    @Override
    protected boolean isBlue() {
        return false;
    }
}
