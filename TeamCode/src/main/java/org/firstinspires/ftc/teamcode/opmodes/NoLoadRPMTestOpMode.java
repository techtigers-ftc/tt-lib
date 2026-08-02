package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.GoBodometrySubsystem;
import org.firstinspires.ftc.teamcode.utils.SlidingAverageCalculator;
import org.firstinspires.ftc.teamcode.utils.Waypoint;

@TeleOp
public class NoLoadRPMTestOpMode extends BaseOpMode {
    private DriveSubsystem drive;
    private SlidingAverageCalculator rpmSlidingAverage;
    private SlidingAverageCalculator currentSlidingAverage;
    private ElapsedTime timer;

    @Override
    protected void initialize() {
        drive = new DriveSubsystem(hardwareMap);
        rpmSlidingAverage = new SlidingAverageCalculator(100);
        currentSlidingAverage = new SlidingAverageCalculator(100);
        timer = new ElapsedTime();
        GoBodometrySubsystem odometry = new GoBodometrySubsystem(hardwareMap, new Waypoint(0, 0, Math.toRadians(0)));

        registerSubsystems(drive, odometry);
    }

    @Override
    protected void justAfterStart() {
        timer.reset();
    }

    @Override
    protected void update() {
        double currentDraw = robotState.getDriveCurrent();

        drive.driveRobotCentric(1, 0, 0);
        currentSlidingAverage.add(currentDraw);
        rpmSlidingAverage.add(INSERT RPM CALCULATION HERE);

        if (timer.seconds() < 10) {
            telemetry.addLine("Collecting data, please wait...");
        } else {
            telemetry.addData("Average RPM",
                    rpmSlidingAverage.getAverage());
            telemetry.addData("Average Current Draw",
                    currentSlidingAverage.getAverage());
        }
    }

    @Override
    protected boolean isBlue() {
        return false;
    }
}
