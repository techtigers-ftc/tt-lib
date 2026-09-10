package team.techtigers.pathing;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.follower.Follower;

import team.techtigers.subsystems.DriveSubsystem;
import team.techtigers.utils.RobotState;

public class Constants {
    public static Follower createFollower(DriveSubsystem drive, RobotState robotState, ForesightConfig foresightConfig) {
        return new Follower(new RobotStateLocalizer(robotState), new Mecanum(drive), new Foresight(foresightConfig));
    }
}