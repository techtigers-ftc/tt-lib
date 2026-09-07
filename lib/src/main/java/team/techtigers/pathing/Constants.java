package team.techtigers.pathing;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PredictiveBrakingCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.paths.PathConstraints;

import team.techtigers.subsystems.DriveSubsystem;
import team.techtigers.utils.RobotState;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .centripetalScaling(0)
            .mass(13.61)
            .headingPIDFCoefficients(new PIDFCoefficients(2,0,0,0))
            .predictiveBrakingCoefficients(new PredictiveBrakingCoefficients(0.1, 0.047, 0.0017))
            .centripetalScaling(0);;

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static DriveConstants driveConstants = new DriveConstants()
            .maxPower(1)
            .xVelocity(45.35)
            .yVelocity(65.18);

    public static Follower createFollower(DriveSubsystem drive, RobotState robotState) {
        return new Follower(followerConstants, new RobotStateLocalizer(robotState), new Mecanum(driveConstants, drive, robotState));
    }
}