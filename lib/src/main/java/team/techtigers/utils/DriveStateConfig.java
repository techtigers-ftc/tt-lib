package team.techtigers.utils;

import com.pedropathing.follower.Follower;

import team.techtigers.subsystems.AutoSubsystem;

/**
 * A configuration class for the DriveStateBase that holds the running, finished, and timeout conditions, as well as the tolerance and angle tolerance values.
 * Serves as default configuration for DriveStateBase class
 *
 * @param <T> The type of the state condition enum
 */
public class DriveStateConfig<T> {
    private final T runningCondition;
    private final T finishedCondition;
    private final T timeoutCondition;
    private final Follower follower;
    private final double tolerance;
    private final double angleTolerance;

    /**
     * Constructs a DriveStateConfig with the specified running, finished, and timeout conditions, as well as the tolerance and angle tolerance values.
     *
     * @param runningCondition The condition that indicates the state is running
     * @param finishedCondition The condition that indicates the state has finished
     * @param timeoutCondition The condition that indicates the state has timed out
     * @param follower The follower object used for path following
     * @param tolerance The tolerance value for the drive state
     * @param angleTolerance The angle tolerance value for the drive state
     */
    public DriveStateConfig(T runningCondition, T finishedCondition, T timeoutCondition, Follower follower, double tolerance, double angleTolerance) {
        this.runningCondition = runningCondition;
        this.finishedCondition = finishedCondition;
        this.timeoutCondition = timeoutCondition;
        this.follower = follower;
        this.tolerance = tolerance;
        this.angleTolerance = angleTolerance;
    }

    /**
     * Constructs a DriveStateConfig with the specified running, finished, and timeout conditions, using default tolerance and angle tolerance values.
     *
     * @param runningCondition The condition that indicates the state is running
     * @param finishedCondition The condition that indicates the state has finished
     * @param timeoutCondition The condition that indicates the state has timed out
     * @param follower The follower object used for path following
     */
    public DriveStateConfig(T runningCondition, T finishedCondition, T timeoutCondition, Follower follower) {
        this(runningCondition, finishedCondition, timeoutCondition, follower, AutoSubsystem.MEDIUM_TOLERANCE, AutoSubsystem.MEDIUM_ANGLE_TOLERANCE);
    }

    /**
     * Returns the tolerance value for the drive state.
     *
     * @return The tolerance value
     */
    public T getRunningCondition() {
        return runningCondition;
    }

    /**
     * Returns the finished condition for the drive state.
     *
     * @return The finished condition
     */
    public T getFinishedCondition() {
        return finishedCondition;
    }

    /**
     * Returns the timeout condition for the drive state.
     *
     * @return The timeout condition
     */
    public T getTimeoutCondition() {
        return timeoutCondition;
    }

    /**
     * Returns the follower object used for path following.
     *
     * @return The follower object
     */
    public Follower getFollower() {
        return follower;
    }

    /**
     * Returns the tolerance value for the drive state.
     *
     * @return The tolerance value
     */
    public double getTolerance() {
        return tolerance;
    }

    /**
     * Returns the angle tolerance value for the drive state.
     *
     * @return The angle tolerance value
     */
    public double getAngleTolerance() {
        return angleTolerance;
    }
}
