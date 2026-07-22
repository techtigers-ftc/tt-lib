package org.firstinspires.ftc.teamcode.autostates;

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
     * Calculate the distance to the target
     *
     * @param current current waypoint
     * @param target  target waypoint
     * @return the distance to the target
     */
    protected double distToTarget(Pose current, Pose target) {
        return Math.hypot(target.getX() - current.getX(),
                target.getY() - current.getY());
    }

    /**
     * Calculate the angle distance to the target
     *
     * @param currentHeading current heading
     * @param targetHeading  target heading
     * @return the angle distance to the target
     */
    protected double angleDistance(double currentHeading, double targetHeading) {
        return Math.abs(currentHeading - targetHeading);
    }

    /**
     * Sets the path chain for the drive command.
     *
     * @param pathChain the path chain to run
     */
    public void setPathChain(PathChain pathChain) {
        autoDriveCommand.setPathChain(pathChain);
    }

    /**
     * Sets the heading PIDF coefficients for the drive command.
     *
     * @param p the proportional coefficient
     * @param d the derivative coefficient
     */
    public void setHeadingPIDF(double p, double d) {
        autoDriveCommand.setHeadingPIDF(p, 0, d, 0);
    }

    public void setPredictiveBreakingCoefficients(double proportional, double linearBraking, double quadraticFriction) {
        autoDriveCommand.setPredictiveBrakingCoefficients(proportional, linearBraking, quadraticFriction);
    }

    /**
     * Sets the tolerance for the drive state
     *
     * @param tolerance the tolerance for the drive state
     */
    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    /**
     * Sets the angle tolerance for the drive state
     *
     * @param angleTolerance the angle tolerance for the drive state
     */
    public void setAngleTolerance(double angleTolerance) {
        this.angleTolerance = angleTolerance;
    }

    @Override
    public void initialize() {
        super.initialize();
        hasRecovered = false;
        recoveryTimer.reset();
    }

    @Override
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

        Pose current = robotState.getRobotPose();
        Pose target = robotState.getRobotFinalPose();

//        RobotLog.dd(LOG_TAG, "Current State: %s", robotState.getCurrentAutoState());
//        RobotLog.dd(LOG_TAG, "Distance: %f", distToTarget(current, target));
//        RobotLog.dd(LOG_TAG, "Angular Distance: %f", Math.toDegrees(angleDistance(current.getHeading(), target.getHeading())));

        if (distToTarget(current, target) < tolerance
                && angleDistance(current.getHeading(), target.getHeading()) < angleTolerance) {
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
