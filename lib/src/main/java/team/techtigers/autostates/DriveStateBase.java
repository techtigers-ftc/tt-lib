package team.techtigers.autostates;

import androidx.annotation.CallSuper;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.util.ElapsedTime;

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
    private double tolerance;
    private double angleTolerance;

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
        tolerance = AutoSubsystem.MEDIUM_TOLERANCE;
        setTolerance(tolerance);
        angleTolerance = AutoSubsystem.MEDIUM_ANGLE_TOLERANCE;
        setAngleTolerance(angleTolerance);
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
     * Sets the path chain for the drive command.
     *
     * @param pathChain the path chain to run
     * @return the current instance of DriveStateBase for method chaining
     */
    public DriveStateBase setPathChain(PathChain pathChain) {
        autoDriveCommand.setPathChain(pathChain);
        return this;
    }

    /**
     * Sets the heading PIDF coefficients for the drive command.
     *
     * @param p the proportional coefficient
     * @param d the derivative coefficient
     * @return the current instance of DriveStateBase for method chaining
     */
    public DriveStateBase setHeadingPIDF(double p, double d) {
        autoDriveCommand.setHeadingPIDF(p, 0, d, 0);
        return this;
    }

    /**
     * Sets the predictive braking coefficients for the drive command.
     *
     * @param proportional      the proportional coefficient for predictive braking
     * @param linearBraking     the linear braking coefficient for predictive braking
     * @param quadraticFriction the quadratic friction coefficient for predictive braking
     * @return the current instance of DriveStateBase for method chaining
     */
    public DriveStateBase setPredictiveBreakingCoefficients(double proportional, double linearBraking, double quadraticFriction) {
        autoDriveCommand.setPredictiveBrakingCoefficients(proportional, linearBraking, quadraticFriction);
        return this;
    }

    /**
     * Sets the tolerance for the drive state
     *
     * @param tolerance the tolerance for the drive state
     * @return the current instance of DriveStateBase for method chaining
     */
    public DriveStateBase setTolerance(double tolerance) {
        autoDriveCommand.setTolerance(tolerance);
        return this;
    }

    /**
     * Sets the angle tolerance for the drive state
     *
     * @param angleTolerance the angle tolerance for the drive state
     * @return the current instance of DriveStateBase for method chaining
     */
    public DriveStateBase setAngleTolerance(double angleTolerance) {
        autoDriveCommand.setHeadingTolerance(angleTolerance);
        return this;
    }

    /**
     * Sets the timeout constraint for the drive command.
     *
     * @param timeout the amount of time in seconds before the command times out
     * @return the current instance of DriveStateBase for method chaining
     */
    public DriveStateBase setTimeoutConstraint(double timeout) {
        autoDriveCommand.setTimeoutConstraint(timeout);
        return this;
    }

    /**
     * Sets the t-value for the drive command.
     *
     * @param tValue the t-value to set
     * @return the current instance of DriveStateBase for method chaining
     */
    public DriveStateBase setTValue(double tValue) {
        autoDriveCommand.setTValue(tValue);
        return this;
    }

    /**
     * Sets the velocity constraint for the drive command.
     *
     * @param maxVelocity the velocity in inches per second
     * @return the current instance of DriveStateBase for method chaining
     */
    public DriveStateBase setVelocityConstraint(double maxVelocity) {
        autoDriveCommand.setVelocityConstraint(maxVelocity);
        return this;
    }

    @Override
    public AutoStateCondition getCurrentCondition() {
        if (tolerance < 0 || angleTolerance < 0) {
            throw new IllegalStateException("Tolerance and angle tolerance must be set");
        }

        if (autoDriveCommand.isFinished()) {
            return AutoStateCondition.DRIVE_END;
        }

        if (isTimeoutReached()) {
            return AutoStateCondition.TIMEOUT;
        }

        return AutoStateCondition.RUNNING;
    }
}
