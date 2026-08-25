package org.firstinspires.ftc.teamcode.opmodes;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.GoBodometrySubsystem;
import org.firstinspires.ftc.teamcode.utils.Point;
import org.firstinspires.ftc.teamcode.utils.TTLogger;
import org.firstinspires.ftc.teamcode.utils.Waypoint;

@TeleOp
public class DrivetrainTestingOpMode extends BaseOpMode {
    private double maxVelocity = 0;
    private double maxCurrentDraw = 0;
    private double totalCurrentDraw = 0;
    private double averageCurrentDraw = 0;
    private double distanceTraveled = 0;
    private double sixFeetTime;
    private boolean manualOverride = false;
    private boolean automaticRPMTesting = false;
    private boolean automaticFullFieldTesting = false;
    private boolean zeroToSixtyReached = false;
    private boolean sixFeetReached = false;
    private double[] averageWheelRPM = new double[4];
    private double[] rpmSum = new double[4];
    private double elapsed = 0;
    private Pose legStartPose = new Pose(0, 0);
    private ElapsedTime timer = new ElapsedTime();
    private int count = 0;
    private DriveSubsystem drive;
    private GoBodometrySubsystem odometry;
    private double accelerationTime = 0;
    JoinedTelemetry joinedTelemetry = new JoinedTelemetry(PanelsTelemetry.INSTANCE.getFtcTelemetry(), telemetry);
    private static double VELOCITY_THRESHOLD = 60; // in/s

    @Override
    protected void initialize() {
        drive = new DriveSubsystem(hardwareMap);
        odometry = new GoBodometrySubsystem(hardwareMap, new Pose(0, 0, Math.toRadians(0)));

        registerSubsystems(drive, odometry);
    }

    @Override
    protected void update() {
        boolean driving = gamepad1.left_stick_y != 0 ||  gamepad1.left_stick_x != 0 || gamepad1.right_stick_x != 0;
        manualOverride = gamepad1.dpadLeftWasPressed() != manualOverride;
        automaticRPMTesting = gamepad1.dpadRightWasPressed() != automaticRPMTesting;
        automaticFullFieldTesting = gamepad1.dpadDownWasPressed() != automaticFullFieldTesting;

        robotState.getRobotVelocity().getAsVector().getMagnitude();
        double velocity = robotState.getRobotVelocity().getAsVector().getMagnitude();
        double currentDraw = robotState.getDriveCurrent();

        if (!driving && !manualOverride && !automaticRPMTesting && !automaticFullFieldTesting) {
            // reset count and total if you stop driving
            totalCurrentDraw = 0;
            count = 0;
            rpmSum = new double[4];
            timer.reset();
            legStartPose = robotState.getRobotPose();
            drive.driveRobotCentric(0,0,0);
            zeroToSixtyReached = false;
            sixFeetReached = false;
        } else {
            count++;
            distanceTraveled = robotState.getRobotPose().distanceFrom(legStartPose);
            totalCurrentDraw += currentDraw;
            averageCurrentDraw = totalCurrentDraw / count;
//            rpmSum = addArrays(rpmSum, drive.getRRM());
            averageWheelRPM = divideArray(rpmSum, count);
            elapsed = timer.seconds();
        }

        if (manualOverride || driving) {
            drive.driveRobotCentric(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
            automaticRPMTesting = false;
            automaticFullFieldTesting = false;
            distanceTraveled = 0;
        } else if (automaticRPMTesting) {
            drive.setMotorPowers(1, 1, 1, 1);
            if (timer.seconds() > 10) {
                automaticRPMTesting = false;
                drive.setMotorPowers(0, 0, 0, 0);
            }
        } else if (automaticFullFieldTesting) {
            drive.driveRobotCentric(1, 0, 0);

            if (distanceTraveled > 144) {
                automaticFullFieldTesting = false;
                drive.driveRobotCentric(0, 0, 0);
            }

            if (velocity > VELOCITY_THRESHOLD && !zeroToSixtyReached) {
                accelerationTime = timer.seconds();
                zeroToSixtyReached = true;
            }

            if (distanceTraveled > 72 && !sixFeetReached) {
                sixFeetTime = timer.seconds();
                sixFeetReached = true;
            }
        }

        if (velocity > maxVelocity) {
            maxVelocity = velocity;
        }
        if (currentDraw > maxCurrentDraw) {
            maxCurrentDraw = currentDraw;
        }

        joinedTelemetry.addData("Manual override (DPAD LEFT)", manualOverride);
        joinedTelemetry.addData("Automatic RPM Testing (DPAD RIGHT)", automaticRPMTesting);
        joinedTelemetry.addData("Automatic Full Field Testing (DPAD DOWN)", automaticFullFieldTesting);
        joinedTelemetry.addLine();
        joinedTelemetry.addData("Time Elapsed", elapsed);
        joinedTelemetry.addData("Acceleration Time", accelerationTime);
        joinedTelemetry.addData("Six Feet Time", sixFeetTime);
        joinedTelemetry.addLine();
        joinedTelemetry.addData("Current Velocity", velocity);
        joinedTelemetry.addData("Max Velocity", maxVelocity);
        joinedTelemetry.addLine();
        joinedTelemetry.addData("Distance Traveled", distanceTraveled);
        joinedTelemetry.addLine();
        joinedTelemetry.addData("Average FL RPM", averageWheelRPM[0]);
        joinedTelemetry.addData("Average FR RPM", averageWheelRPM[1]);
        joinedTelemetry.addData("Average BL RPM", averageWheelRPM[2]);
        joinedTelemetry.addData("Average BR RPM", averageWheelRPM[3]);
        joinedTelemetry.addLine();
        joinedTelemetry.addData("Current Draw", currentDraw);
        joinedTelemetry.addData("Average Current Draw", averageCurrentDraw);
        joinedTelemetry.addData("Max Current Draw", maxCurrentDraw);
        joinedTelemetry.update();

        TTLogger.setLoggingLevel(TTLogger.DEBUG);
    }

    private double[] addArrays(double[] a, double[] b) {
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] + b[i];
        }
        return result;
    }

    private double[] divideArray(double[] a, double divisor) {
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] / divisor;
        }
        return result;
    }

    @Override
    protected boolean isBlue() {
        return false;
    }
}
