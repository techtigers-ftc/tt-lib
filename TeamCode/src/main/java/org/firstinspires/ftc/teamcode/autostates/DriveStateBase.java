package org.firstinspires.ftc.teamcode.autostates;

import androidx.annotation.CallSuper;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.statemachine.ParallelCommandGroupState;
import org.firstinspires.ftc.teamcode.utils.RobotState;
import org.firstinspires.ftc.teamcode.utils.enums.AutoStateCondition;

/**
 * A base class for autonomous drive states, using a parallel command group.
 */
public abstract class DriveStateBase extends ParallelCommandGroupState<AutoStateCondition> {
    private static final String LOG_TAG = DriveStateBase.class.getSimpleName();
    private static final double RECOVERY_TIMEOUT = 4000;
    protected final AutoDriveCommand autoDriveCommand;
    protected final RobotState robotState;
    private double tolerance;
    private double angleTolerance;
    private boolean hasRecovered;
    private ElapsedTime recoveryTimer;

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
        tolerance = -1;
        angleTolerance = -1;
        hasRecovered = false;
        recoveryTimer = new ElapsedTime();
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
    @CallSuper
    public void initialize() {
        super.initialize();
        hasRecovered = false;
        recoveryTimer.reset();
    }

    @Override
    @CallSuper
    public void update() {
        super.update();
        // If the robot is stuck or the timeout is reached for the first time, we need to recover
//        if (isTimeoutReached() && !hasRecovered) {
//            // Generate a new path chain using the robot's current and final poses
//            PathChain pathChain = new PathBuilder().addBezierLine(
//                    new Point(robotState.getRobotCurrentPose().getX(), robotState.getRobotCurrentPose().getY()),
//                    new Point(robotState.getRobotFinalPose().getX(), robotState.getRobotFinalPose().getY())
//            ).setLinearHeadingInterpolation(
//                    robotState.getRobotCurrentPose().getHeading(),
//                    robotState.getRobotFinalPose().getHeading()
//            ).build();
//            autoDriveCommand.setPathChain(pathChain);
//            hasRecovered = true;
//            recoveryTimer.reset();
//        }
    }

    @Override
    public AutoStateCondition getCurrentCondition() {
        if (tolerance < 0 || angleTolerance < 0) {
            throw new IllegalStateException("Tolerance and angle tolerance must be set");
        }

//        RobotLog.dd(LOG_TAG, "Current State: %s", robotState.getCurrentAutoState());
//        RobotLog.dd(LOG_TAG, "Distance: %f", distToTarget(current, target));
//        RobotLog.dd(LOG_TAG, "Angular Distance: %f", Math.toDegrees(angleDistance(current.getHeading(), target.getHeading())));

        if (autoDriveCommand.isFinished()) {
            return AutoStateCondition.DRIVE_END;
        }

//        if (recoveryTimer.milliseconds() > RECOVERY_TIMEOUT && hasRecovered) {
//            return AutoState.TIMEOUT;
//        }
        if (isTimeoutReached()) {
            return AutoStateCondition.TIMEOUT;
        }

        return AutoStateCondition.RUNNING;
    }
}
