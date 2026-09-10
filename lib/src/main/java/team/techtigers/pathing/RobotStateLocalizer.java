package team.techtigers.pathing;

import com.pedropathing.localization.Localizer;
import com.pedropathing.localization.MotionState;
import com.pedropathing.math.Pose;

import team.techtigers.utils.RobotState;

/**
 * A localizer that uses the RobotState to localize the robot.
 */
public class RobotStateLocalizer implements Localizer {
    private RobotState robotState;
    private MotionState motionState;

    /**
     * Constructs a new RobotStateLocalizer.
     *
     * @param robotState The robot state
     */
    public RobotStateLocalizer(RobotState robotState) {
        super();
        this.robotState = robotState;
    }

    @Override
    public void update() {
        motionState = MotionState.ofVelocity(robotState.get("robotPose"), robotState.get("robotVelocity"));
    }

    @Override
    public void setPose(Pose setPose) {
        // Intentionally not doing this, pose shouldn't be overridden
    }

    @Override
    public MotionState state() {
        return motionState;
    }

    public void reset() {
        // intentionally not doing this, reset should be through odometry subsystem
    }
}
