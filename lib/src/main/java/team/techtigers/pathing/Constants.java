package team.techtigers.pathing;

import com.pedropathing.follower.Follower;

import team.techtigers.subsystems.DriveSubsystem;
import team.techtigers.utils.RobotState;

public class Constants {


    public static Follower createFollower(DriveSubsystem drive, RobotState robotState, PedroConfig pedroConfig) {


        return new Follower(new RobotStateLocalizer(robotState), new Mecanum(driveConstants, drive, robotState));
    }
}