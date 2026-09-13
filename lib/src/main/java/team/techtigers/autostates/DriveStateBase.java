package team.techtigers.autostates;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.Path;

import team.techtigers.statemachine.ParallelCommandGroupState;
import team.techtigers.subsystems.AutoSubsystem;
import team.techtigers.utils.RobotState;
import team.techtigers.utils.enums.AutoStateCondition;

/**
 * A base class for autonomous drive team.techtigers.states, using a parallel command group.
 */
public abstract class DriveStateBase extends ParallelCommandGroupState<AutoStateCondition> {
    private static final String tag = DriveStateBase.class.getSimpleName();
    protected final AutoDriveCommand autoDriveCommand;
    protected final RobotState robotState;

    /**
     * Constructor for the SequentialCommandGroupState
     *
     * @param name       The name of the state
     * @param follower   The follower object
     * @param robotState The robot state
     * @param timeout    Time limit of the drive in seconds
     */
    public DriveStateBase(String name, Follower follower, RobotState robotState, double timeout) {
        super(name, timeout);
        this.robotState = robotState;
        autoDriveCommand = new AutoDriveCommand(follower, robotState);
        setTolerance(AutoSubsystem.MEDIUM_TOLERANCE);
        setAngleTolerance(AutoSubsystem.MEDIUM_ANGLE_TOLERANCE);
    }

    /**
     * Overload Constructor without Timeout
     *
     * @param name       The name of the state
     * @param follower   The follower object
     * @param robotState The robot state
     */
    public DriveStateBase(String name, Follower follower, RobotState robotState) {
        this(name, follower, robotState, -1);
    }

    /**
     * Sets the path that this state will follow. A {@link Path} may resolve to one or more
     * segments; the follower tracks those segments in order and uses the path's end pose as the
     * final position and heading target.
     *
     * @param path the path, including its curve, heading interpolation, and any modifiers
     * @return this state for method chaining
     */
    public DriveStateBase setPathChain(Path path) {
        autoDriveCommand.setPath(path);
        return this;
    }

    /**
     * Controls what the follower does after it reaches the path's parametric end. When enabled,
     * the follower enters hold mode and continues correcting toward the final pose. When disabled,
     * it enters idle mode and stops commanding the drivetrain.
     *
     * @param holdEnd {@code true} to hold the final pose; {@code false} to stop driving
     * @return this state for method chaining
     */
    public DriveStateBase setHoldEnd(boolean holdEnd) {
        autoDriveCommand.setHoldEnd(holdEnd);
        return this;
    }

    /**
     * Limits path speed to a fraction of the drivetrain's direction-dependent maximum achievable
     * velocity. For example, {@code 0.75} caps the target speed at 75% of the achievable speed.
     * If an absolute maximum velocity is also configured, Pedro uses the lower resulting limit.
     *
     * @param maxPathSpeed a positive speed scale, normally in the range {@code (0, 1]}
     * @return this state for method chaining
     */
    public DriveStateBase setMaxPathSpeed(double maxPathSpeed) {
        autoDriveCommand.setMaxPathSpeed(maxPathSpeed);
        return this;
    }

    /**
     * Sets an absolute cap on the target velocity while the follower is coasting along the path.
     * Pedro also respects the drivetrain's achievable velocity and the fractional path-speed cap,
     * using whichever limit produces the lowest target velocity.
     *
     * @param maxVelocity the maximum path velocity in distance units per second
     * @return this state for method chaining
     */
    public DriveStateBase setMaxVelocityConstraint(double maxVelocity) {
        autoDriveCommand.setMaxVelocityConstraint(maxVelocity);
        return this;
    }

    /**
     * Limits how quickly the target tangential velocity can increase while coasting. The limit is
     * applied each follower update before active braking begins.
     *
     * @param maxAcceleration the maximum target acceleration in distance units per second squared
     * @return this state for method chaining
     */
    public DriveStateBase setMaxAccelerationConstraint(double maxAcceleration) {
        autoDriveCommand.setMaxAccelerationConstraint(maxAcceleration);
        return this;
    }

    /**
     * Limits how quickly the target tangential velocity is allowed to decrease while coasting.
     * Pedro uses the remaining path distance to build a deceleration profile before its normal
     * active-braking phase. This value should not exceed the robot's natural deceleration.
     *
     * @param maxDeceleration the maximum coasting deceleration in distance units per second squared
     * @return this state for method chaining
     */
    public DriveStateBase setMaxDecelerationConstraint(double maxDeceleration) {
        autoDriveCommand.setMaxDecelerationConstraint(maxDeceleration);
        return this;
    }

    /**
     * Sets the speed that the maximum-deceleration profile approaches instead of slowing all the
     * way to zero. This setting has no effect unless a finite maximum deceleration constraint is
     * configured.
     *
     * @param coastDownToVelocity the terminal coasting speed in distance units per second
     * @return this state for method chaining
     */
    public DriveStateBase setCoastDownToVelocity(double coastDownToVelocity) {
        autoDriveCommand.setCoastDownToVelocity(coastDownToVelocity);
        return this;
    }

    /**
     * Biases the braking prediction at the end of a segment. {@code 1.0} is neutral, values above
     * {@code 1.0} permit more overshoot, and values below {@code 1.0} bias the robot to undershoot
     * and stop more conservatively. This changes when Pedro commits to braking, not the braking
     * power limit.
     *
     * @param brakeAggression a positive braking-bias value
     * @return this state for method chaining
     */
    public DriveStateBase setBrakeAggression(double brakeAggression) {
        autoDriveCommand.setBrakeAggression(brakeAggression);
        return this;
    }

    /**
     * Controls whether Pedro applies its active braking controller as the robot reaches the end of
     * a path. Disabling it leaves the follower in its coasting behavior; path-to-path continuation
     * is controlled separately by {@link #setPathSkip(boolean)}.
     *
     * @param brakeAtEnd {@code true} to actively brake at the path end; {@code false} to coast
     * @return this state for method chaining
     */
    public DriveStateBase setBrakeAtEnd(boolean brakeAtEnd) {
        autoDriveCommand.setBrakeAtEnd(brakeAtEnd);
        return this;
    }

    /**
     * Controls momentum-preserving transitions between segments of a compound path. When enabled,
     * Pedro advances to the next segment once the current segment reaches its predicted braking
     * point. When disabled, it remains on the current segment until the parametric end is reached.
     *
     * @param pathSkip {@code true} to continue early into the next segment; {@code false} to finish
     *                 the current segment first
     * @return this state for method chaining
     */
    public DriveStateBase setPathSkip(boolean pathSkip) {
        autoDriveCommand.setPathSkip(pathSkip);
        return this;
    }

    /**
     * Sets how Pedro prioritizes heading feedback relative to translational drive power when motor
     * commands must be clamped. {@code 1.0} gives heading correction priority before drive power,
     * while {@code 0.0} gives drive power priority before heading correction.
     *
     * @param headingDriveRatio the heading-priority ratio, normally from {@code 0.0} to {@code 1.0}
     * @return this state for method chaining
     */
    public DriveStateBase setHeadingDriveRatio(double headingDriveRatio) {
        autoDriveCommand.setHeadingDriveRatio(headingDriveRatio);
        return this;
    }

    /**
     * Sets the maximum translational error allowed during final-pose correction. After the path's
     * parametric end, this error, the heading error, and the tangential speed must all be below
     * their thresholds for the follower to report that it is no longer busy.
     *
     * @param tolerance the maximum position error in the path's distance units
     * @return this state for method chaining
     */
    public DriveStateBase setTolerance(double tolerance) {
        autoDriveCommand.setTolerance(tolerance);
        return this;
    }

    /**
     * Sets the maximum absolute heading error allowed during final-pose correction. The heading,
     * translational, and velocity constraints must be satisfied together unless the endpoint
     * correction timeout expires first.
     *
     * @param angleTolerance the maximum heading error in radians
     * @return this state for method chaining
     */
    public DriveStateBase setAngleTolerance(double angleTolerance) {
        autoDriveCommand.setHeadingTolerance(angleTolerance);
        return this;
    }

    /**
     * Sets how long Pedro may correct at the final pose after reaching the parametric end before it
     * reports completion even if the translational, heading, and velocity constraints have not all
     * been met. This endpoint timeout is separate from the state's overall timeout.
     *
     * @param timeout the endpoint correction timeout in milliseconds
     * @return this state for method chaining
     */
    public DriveStateBase setTimeoutConstraint(double timeout) {
        autoDriveCommand.setTimeoutConstraint(timeout);
        return this;
    }

    /**
     * Sets how close the closest point on the current curve must be to {@code t = 1} before the
     * segment is parametrically complete. Completion occurs when {@code t >= 1 - tValue}; for
     * example, {@code 0.025} permits completion at {@code t >= 0.975}. Larger values finish the
     * segment earlier, while smaller values require progress closer to its mathematical end.
     *
     * @param tValue the remaining parametric margin, normally in the range {@code (0.0, 1.0]}
     * @return this state for method chaining
     */
    public DriveStateBase setTValue(double tValue) {
        autoDriveCommand.setTValue(tValue);
        return this;
    }

    /**
     * Sets the maximum tangential speed allowed during final-pose correction. After the parametric
     * end, the robot must be moving below this speed and be within both pose-error tolerances for
     * the follower to report completion before the endpoint timeout.
     *
     * @param maxVelocity the endpoint speed threshold in distance units per second
     * @return this state for method chaining
     */
    public DriveStateBase setVelocityConstraint(double maxVelocity) {
        autoDriveCommand.setVelocityConstraint(maxVelocity);
        return this;
    }

    @Override
    public AutoStateCondition getCurrentCondition() {
        if (autoDriveCommand.isFinished()) {
            return AutoStateCondition.DRIVE_END;
        }

        if (isTimeoutReached()) {
            return AutoStateCondition.TIMEOUT;
        }

        return AutoStateCondition.RUNNING;
    }
}
