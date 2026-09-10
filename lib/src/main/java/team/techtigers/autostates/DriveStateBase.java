package team.techtigers.autostates;

import androidx.annotation.CallSuper;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.util.ElapsedTime;

import team.techtigers.statemachine.ParallelCommandGroupState;
import team.techtigers.utils.RobotState;
import team.techtigers.utils.enums.AutoStateCondition;

/**
 * A base class for autonomous drive team.techtigers.states, using a parallel command group.
 */
public abstract class DriveStateBase extends ParallelCommandGroupState<AutoStateCondition> {
    private static final String LOG_TAG = DriveStateBase.class.getSimpleName();
    private static final double RECOVERY_TIMEOUT = 4000;
    protected final AutoDriveCommand autoDriveCommand;
    protected final RobotState robotState;
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
     * @param path the path chain to run
     * @return the current instance of DriveStateBase for method chaining
     */
    public DriveStateBase setPath(Path path) {
        autoDriveCommand.setPath(path);
        return this;
    }

    @Override
    @CallSuper
    public void initialize() {
        super.initialize();
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
