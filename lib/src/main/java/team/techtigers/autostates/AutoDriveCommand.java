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
    }

    @Override
    public boolean isFinished() {
        TTLogger.dd(tag, "Follower busy: %b", follower.isBusy());
        return !follower.isBusy();
    }

    /**
     * Sets the path for the command.
     *
     * @param path the path to run
     */
    public void setPath(Path path) {
        this.path = path;
    }

    /**
     * Sets whether the robot should hold its position at the end of the path.
     *
     * @param holdEnd true to hold position, false to not hold
     */
    public void setHoldEnd(boolean holdEnd) {
        follower.holdEnd.set(holdEnd);
    }

    /**
     * Sets the maximum path speed for the command.
     *
     * @param maxPathSpeed the maximum path speed in inches per second
     */
    public void setMaxPathSpeed(double maxPathSpeed) {
        config.maxPathSpeed.set(maxPathSpeed);
    }

    /**
     * Sets the maximum velocity constraint for the command.
     *
     * @param maxVelocityConstraint the maximum velocity constraint in inches per second
     */
    public void setMaxVelocityConstraint(double maxVelocityConstraint) {
        config.maxVelocityConstraint.set(maxVelocityConstraint);
    }

    /**
     * Sets the maximum acceleration constraint for the command.
     *
     * @param maxAccelerationConstraint the maximum acceleration constraint in inches per second squared
     */
    public void setMaxAccelerationConstraint(double maxAccelerationConstraint) {
        config.maxAccelerationConstraint.set(maxAccelerationConstraint);
    }

    /**
     * Sets the maximum deceleration constraint for the command.
     *
     * @param maxDecelerationConstraint the maximum deceleration constraint in inches per second squared
     */
    public void setMaxDecelerationConstraint(double maxDecelerationConstraint) {
        config.maxDecelerationConstraint.set(maxDecelerationConstraint);
    }

    /**
     * Sets the coast down to velocity for the command.
     *
     * @param coastDownToVelocity the velocity to coast down to
     */
    public void setCoastDownToVelocity(double coastDownToVelocity) {
        config.coastDownToVelocity.set(coastDownToVelocity);
    }

    /**
     * Sets the brake aggression for the command.
     *
     * @param brakeAggression the brake aggression value
     */
    public void setBrakeAggression(double brakeAggression) {
        config.brakeAggression.set(brakeAggression);
    }

    /**
     * Sets whether the robot should brake at the end of the path.
     *
     * @param brakeAtEnd true to brake at the end, false to coast
     */
    public void setBrakeAtEnd(boolean brakeAtEnd) {
        config.brakeAtEnd.set(brakeAtEnd);
    }

    /**
     * Sets whether the robot should stop fully before going to the next path in the chain.
     *
     * @param pathSkip true to skip the stop
     */
    public void setPathSkip(boolean pathSkip) {
        config.pathSkip.set(pathSkip);
    }

    /**
     * Sets the heading drive ratio for the command.
     *
     * @param headingDriveRatio the heading drive ratio
     */
    public void setHeadingDriveRatio(double headingDriveRatio) {
        config.headingDriveRatio.set(headingDriveRatio);
    }

    /**
     * Sets the translational tolerance for the command.
     *
     * @param tolerance the translational tolerance in inches
     */
    public void setTolerance(double tolerance) {
        config.translationalConstraint.set(tolerance);
    }

    /**
     * Sets the heading tolerance for the command.
     *
     * @param headingTolerance the heading tolerance in radians
     */
    public void setHeadingTolerance(double headingTolerance) {
        config.headingConstraint.set(headingTolerance);
    }

    /**
     * Sets the timeout constraint for the command.
     *
     * @param timeout the amount of time in milliseconds before the command times out
     */
    public void setTimeoutConstraint(double timeout) {
        config.timeoutConstraint.set(timeout);
    }

    /**
     * Sets the velocity constraint for the command.
     *
     * @param velocity the velocity under which the command will be considered complete
     */
    public void setVelocityConstraint(double velocity) {
        config.velocityConstraint.set(velocity);
    }

    /**
     * Sets the t-value constraint for the command.
     *
     * @param tValue the t-value under which the command will be considered complete
     */
    public void setTValue(double tValue) {
        config.parametricTConstraint.set(tValue);
    }
}
