package team.techtigers.pathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;

import team.techtigers.subsystems.DriveSubsystem;
import team.techtigers.utils.RobotState;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants();

    public static DriveConstants driveConstants = new DriveConstants();

    public static Follower createFollower(DriveSubsystem drive, RobotState robotState, PedroConfig pedroConfig) {
        followerConstants
                .centripetalScaling(pedroConfig.centripetalScaling)
                .mass(pedroConfig.mass)
                .headingPIDFCoefficients(pedroConfig.headingPIDFCoefficients)
                .predictiveBrakingCoefficients(pedroConfig.predictiveBrakingCoefficients);

        driveConstants
                .maxPower(pedroConfig.maxPower)
                .xVelocity(pedroConfig.xVelocity)
                .yVelocity(pedroConfig.yVelocity);

        return new Follower(followerConstants, new RobotStateLocalizer(robotState), new Mecanum(driveConstants, drive, robotState));
    }
}