package org.firstinspires.ftc.teamcode.configuration;

import static org.firstinspires.ftc.teamcode.subsystems.AutoSubsystem.MEDIUM_ANGLE_TOLERANCE;
import static org.firstinspires.ftc.teamcode.subsystems.AutoSubsystem.MEDIUM_TOLERANCE;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.autostates.DriveStateBase;

public class AutoConfigurator {


    public static void configureBasicDriveState(DriveStateBase driveStateBase, Follower follower) {
        driveStateBase
                .setHeadingPIDF(1, 0.1)

                .setPathChain(
                        follower.pathBuilder().addPath(
                                        new BezierLine(
                                                new Pose(0, 0),
                                                new Pose(20, 20)
                                        )
                                )
                                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(90))
                                .build()
                )

                .setTolerance(MEDIUM_TOLERANCE)
                .setAngleTolerance(MEDIUM_ANGLE_TOLERANCE);
    }

    public static void configureOtherBasicDriveState(DriveStateBase driveStateBase, Follower follower) {
        driveStateBase
                .setHeadingPIDF(1, 0.1)

                .setPathChain(
                        follower.pathBuilder().addPath(
                                        new BezierLine(
                                                new Pose(20, 20),
                                                new Pose(0, 0)
                                        )
                                )
                                .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(0))
                                .build()
                )

                .setTolerance(MEDIUM_TOLERANCE)
                .setAngleTolerance(MEDIUM_ANGLE_TOLERANCE);
    }
}
