package team.techtigers.pathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;

import team.techtigers.subsystems.DriveSubsystem;
import team.techtigers.utils.RobotState;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants();

    public static DriveConstants driveConstants = new DriveConstants();

    public static Follower createFollower(DriveSubsystem drive, RobotState robotState, PedroConstants pedroConstants) {
        followerConstants
                .centripetalScaling(pedroConstants.centripetalScaling)
                .mass(pedroConstants.mass)
                .headingPIDFCoefficients(pedroConstants.headingPIDFCoefficients)
                .predictiveBrakingCoefficients(pedroConstants.predictiveBrakingCoefficients);

        driveConstants
                .maxPower(pedroConstants.maxPower)
                .xVelocity(pedroConstants.xVelocity)
                .yVelocity(pedroConstants.yVelocity);

        return new Follower(followerConstants, new RobotStateLocalizer(robotState), new Mecanum(driveConstants, drive, robotState));
    }
}