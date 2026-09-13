package team.techtigers.autostates;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import team.techtigers.commands.CommandBase;
import team.techtigers.utils.RobotState;
import team.techtigers.utils.TTLogger;

/**
 * A class for autonomous drive commands that use PedroPathing.
 */
public class AutoDriveCommand extends CommandBase {
    private final RobotState robotState;
    public Follower follower;
    private Path path;

    // Controllers/Constraints
    private final ForesightConfig config;
    private final ForesightConfig originalConfig;

    /**
     * Constructs a new AutoDriveCommand.
     *
     * @param follower   the follower object
     * @param robotState The robot state
     */
    public AutoDriveCommand(Follower follower,
                            RobotState robotState) {
        this.robotState = robotState;
        this.follower = follower;
        follower.update();

        Foresight algorithm = (Foresight) follower.algorithm();
        config = algorithm.config;
        originalConfig = algorithm.config;
    }

    @Override
    public void initialize() {
        // Sets constraints
        follower.setAlgorithm(new Foresight(config));

        // Makes sure that a path chain is set
        if (path == null) {
            throw new IllegalArgumentException("Path chain not set");
        }

        // Finds the final waypoint in the path chain
        Pose target =
                path.endPose();

        // Sets the robot's final pose to the final waypoint found
        robotState.set("robotFinalPose", target);
        TTLogger.dd(tag, "Initial Target: %s", path.endPose().toString());
        TTLogger.dd(tag, "Final Pose: %s", target.toString());
        follower.follow(path);

        TTLogger.dd(tag, "Follower Initializing");
    }

    @Override
    public void update() {
        follower.update();
        TTLogger.dd(tag, "Follower updating, robot pose: %s", robotState.get("robotPose").toString());
        TTLogger.dd(tag, "Follower running, T Value: %f", follower.parametricCompletion());
        TTLogger.dd(tag, "Follower running, Distance Remaining: %f", follower.remainingDistance());
    }

    @Override
    public void end(boolean interrupted) {
        TTLogger.dd(tag, "Path Following Completed");
        follower.setAlgorithm(new Foresight(originalConfig));
    }

    @Override
    public boolean isFinished() {
        TTLogger.dd(tag, "Follower busy: %b", follower.isBusy());
        return !follower.isBusy();
    }

    /**
     * Sets the path that the follower will track when this command initializes. A {@link Path} may
     * resolve to one or more segments; its end pose becomes the command's final pose target.
     *
     * @param path the path, including its curve, heading interpolation, and any modifiers
     */
    public void setPath(Path path) {
        this.path = path;
    }

    /**
     * Controls what the follower does after it reaches the path's parametric end. When enabled,
     * the follower enters hold mode and continues correcting toward the final pose. When disabled,
     * it enters idle mode and stops commanding the drivetrain.
     *
     * @param holdEnd {@code true} to hold the final pose; {@code false} to stop driving
     */
    public void setHoldEnd(boolean holdEnd) {
        follower.holdEnd.set(holdEnd);
    }

    /**
     * Limits path speed to a fraction of the drivetrain's direction-dependent maximum achievable
     * velocity. For example, {@code 0.75} caps the target speed at 75% of the achievable speed.
     * If an absolute maximum velocity is also configured, Pedro uses the lower resulting limit.
     *
     * @param maxPathSpeed a positive speed scale, normally in the range {@code (0, 1]}
     */
    public void setMaxPathSpeed(double maxPathSpeed) {
        config.maxPathSpeed.set(maxPathSpeed);
    }

    /**
     * Sets an absolute cap on the target velocity while the follower is coasting along the path.
     * Pedro also respects the drivetrain's achievable velocity and the fractional path-speed cap,
     * using whichever limit produces the lowest target velocity.
     *
     * @param maxVelocityConstraint the maximum path velocity in distance units per second
     */
    public void setMaxVelocityConstraint(double maxVelocityConstraint) {
        config.maxVelocityConstraint.set(maxVelocityConstraint);
    }

    /**
     * Limits how quickly the target tangential velocity can increase while coasting. The limit is
     * applied each follower update before active braking begins.
     *
     * @param maxAccelerationConstraint the maximum target acceleration in distance units per second
     *                                  squared
     */
    public void setMaxAccelerationConstraint(double maxAccelerationConstraint) {
        config.maxAccelerationConstraint.set(maxAccelerationConstraint);
    }

    /**
     * Limits how quickly the target tangential velocity is allowed to decrease while coasting.
     * Pedro uses the remaining path distance to build a deceleration profile before its normal
     * active-braking phase. This value should not exceed the robot's natural deceleration.
     *
     * @param maxDecelerationConstraint the maximum coasting deceleration in distance units per
     *                                  second squared
     */
    public void setMaxDecelerationConstraint(double maxDecelerationConstraint) {
        config.maxDecelerationConstraint.set(maxDecelerationConstraint);
    }

    /**
     * Sets the speed that the maximum-deceleration profile approaches instead of slowing all the
     * way to zero. This setting has no effect unless a finite maximum deceleration constraint is
     * configured.
     *
     * @param coastDownToVelocity the terminal coasting speed in distance units per second
     */
    public void setCoastDownToVelocity(double coastDownToVelocity) {
        config.coastDownToVelocity.set(coastDownToVelocity);
    }

    /**
     * Biases the braking prediction at the end of a segment. {@code 1.0} is neutral, values above
     * {@code 1.0} permit more overshoot, and values below {@code 1.0} bias the robot to undershoot
     * and stop more conservatively. This changes when Pedro commits to braking, not the braking
     * power limit.
     *
     * @param brakeAggression a positive braking-bias value
     */
    public void setBrakeAggression(double brakeAggression) {
        config.brakeAggression.set(brakeAggression);
    }

    /**
     * Controls whether Pedro applies its active braking controller as the robot reaches the end of
     * a path. Disabling it leaves the follower in its coasting behavior; path-to-path continuation
     * is controlled separately by {@link #setPathSkip(boolean)}.
     *
     * @param brakeAtEnd {@code true} to actively brake at the path end; {@code false} to coast
     */
    public void setBrakeAtEnd(boolean brakeAtEnd) {
        config.brakeAtEnd.set(brakeAtEnd);
    }

    /**
     * Controls momentum-preserving transitions between segments of a compound path. When enabled,
     * Pedro advances to the next segment once the current segment reaches its predicted braking
     * point. When disabled, it remains on the current segment until the parametric end is reached.
     *
     * @param pathSkip {@code true} to continue early into the next segment; {@code false} to finish
     *                 the current segment first
     */
    public void setPathSkip( boolean pathSkip) {
        config.pathSkip.set(pathSkip);
    }

    /**
     * Sets how Pedro prioritizes heading feedback relative to translational drive power when motor
     * commands must be clamped. {@code 1.0} gives heading correction priority before drive power,
     * while {@code 0.0} gives drive power priority before heading correction.
     *
     * @param headingDriveRatio the heading-priority ratio, normally from {@code 0.0} to {@code 1.0}
     */
    public void setHeadingDriveRatio(double headingDriveRatio) {
        config.headingDriveRatio.set(headingDriveRatio);
    }

    /**
     * Sets the maximum translational error allowed during final-pose correction. After the path's
     * parametric end, this error, the heading error, and the tangential speed must all be below
     * their thresholds for the follower to report that it is no longer busy.
     *
     * @param tolerance the maximum position error in the path's distance units
     */
    public void setTolerance(double tolerance) {
        config.translationalConstraint.set(tolerance);
    }

    /**
     * Sets the maximum absolute heading error allowed during final-pose correction. The heading,
     * translational, and velocity constraints must be satisfied together unless the endpoint
     * correction timeout expires first.
     *
     * @param headingTolerance the maximum heading error in radians
     */
    public void setHeadingTolerance(double headingTolerance) {
        config.headingConstraint.set(headingTolerance);
    }

    /**
     * Sets how long Pedro may correct at the final pose after reaching the parametric end before it
     * reports completion even if the translational, heading, and velocity constraints have not all
     * been met. This is an endpoint-settling timeout, not a timeout for the entire command.
     *
     * @param timeout the endpoint correction timeout in milliseconds
     */
    public void setTimeoutConstraint(double timeout) {
        config.timeoutConstraint.set(timeout);
    }

    /**
     * Sets the maximum tangential speed allowed during final-pose correction. After the parametric
     * end, the robot must be moving below this speed and be within both pose-error tolerances for
     * the follower to report completion before the endpoint timeout.
     *
     * @param velocity the endpoint speed threshold in distance units per second
     */
    public void setVelocityConstraint(double velocity) {
        config.velocityConstraint.set(velocity);
    }

    /**
     * Sets how close the closest point on the current curve must be to {@code t = 1} before the
     * segment is parametrically complete. Completion occurs when {@code t >= 1 - tValue}; for
     * example, {@code 0.025} permits completion at {@code t >= 0.975}. Larger values finish the
     * segment earlier, while smaller values require progress closer to its mathematical end.
     *
     * @param tValue the remaining parametric margin, normally in the range {@code (0.0, 1.0]}
     */
    public void setTValue(double tValue) {
        config.parametricTConstraint.set(tValue);
    }
}
