package team.techtigers.utils;

import com.pedropathing.geometry.Pose;

/**
 * Class to set the default values of RobotState
 */
public class RobotStateInitializer {
    /**
     * Initialize values in robot state to defaults
     *
     * @param robotState the robot state to set the values to
     */
    public static void initialize(RobotState robotState) {
        robotState.set("robotPose", new Pose(0,0,0));
        robotState.set("finalRobotPose", new Pose(0,0,0));
        robotState.set("robotVoltage", 12.0);
        robotState.set("currentAutoState", "");
        robotState.set("previousAutoState", "");
        robotState.set("autoRemainingTime", 30.0);
        robotState.set("driveCurrent", 0.0);
        robotState.set("robotVelocity", new Pose(0,0,0));
    }
}
