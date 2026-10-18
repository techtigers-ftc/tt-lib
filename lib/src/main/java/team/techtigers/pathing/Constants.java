package team.techtigers.pathing;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.follower.Follower;

import team.techtigers.subsystems.DriveSubsystem;
import team.techtigers.utils.RobotState;

/**
 *
 */
public class Constants {
    /**
     * Constructs a new pedro pathing follower object
     *
     * @param drive the drive subsystem
     * @param robotState the robot state
     * @param foresightConfig the foresight config, defined from the auto tuners in the pedro pathing quickstart
     * @return the follower object created with the given parameters
     */
    public static Follower createFollower(DriveSubsystem drive, RobotState robotState, ForesightConfig foresightConfig) {
        return new Follower(new RobotStateLocalizer(robotState), new Mecanum(drive), new Foresight(foresightConfig));
    }
}