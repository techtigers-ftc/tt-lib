package team.techtigers.autostates;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PredictiveBrakingCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierPoint;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.paths.PathConstraints;

import team.techtigers.commands.CommandBase;
import team.techtigers.utils.RobotState;
import team.techtigers.utils.TTLogger;

/**
 * A class for autonomous drive commands that use PedroPathing.
 */
public class AutoDriveCommand extends CommandBase {
    private final RobotState robotState;
    public Follower follower;
    private PathChain pathChain;

    // Controllers/Constraints
    private PIDFCoefficients headingPIDF;
    private PredictiveBrakingCoefficients predictiveBrakingCoefficients;
    private final PathConstraints constraints;

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

        constraints = follower.getConstraints();
    }

    @Override
    public void initialize() {
        // Keeps default predictive braking and heading PIDF coefficients if not set

        if (headingPIDF != null) {
            follower.setHeadingPIDFCoefficients(headingPIDF);
        }
        if (predictiveBrakingCoefficients != null) {
            follower.setConstants(follower.getConstants().predictiveBrakingCoefficients(predictiveBrakingCoefficients));
        }

        // Sets constraints
        follower.setConstraints(constraints);

        // Makes sure that a path chain is set
        if (pathChain == null) {
            throw new IllegalArgumentException("Path chain not set");
        }

        // Finds the final waypoint in the path chain
        Path finalPath = pathChain.getPath(pathChain.size() - 1);
        Pose target =
                finalPath.endPose();

        // Sets the robot's final pose to the final waypoint found
        robotState.set("robotFinalPose", target);
        TTLogger.dd(tag, "Initial Target is: %s", pathChain.getPath(0).endPose().toString());
        TTLogger.dd(tag, "Final pose set to: %s", target.toString());
        follower.followPath(pathChain, true);

        TTLogger.dd(tag, "Follower initializing");
    }

    @Override
    public void update() {
        follower.update();
        TTLogger.dd(tag, "Follower driving towards point: %s", follower.getCurrentPath().endPose().toString());
        TTLogger.dd(tag, "Follower updating, robot pose: %s", robotState.get("robotPose").toString());
        TTLogger.dd(tag, "Follower running, T Value: %d", follower.getCurrentTValue());
        TTLogger.dd(tag, "Follower running, Distance Remaining: %d", follower.getDistanceRemaining());
    }

    @Override
    public void end(boolean interrupted) {
        TTLogger.dd(tag, "Follower done following");
        // Holds the robots current position and heading, and internally stops any concurrent following
        Pose robotPose = robotState.get("robotPose");
        follower.holdPoint(new BezierPoint(robotPose.getX(),
                robotPose.getY()),
                robotPose.getHeading(), true);
    }

    @Override
    public boolean isFinished() {
        TTLogger.dd(tag, "Follower busy: %b", follower.isBusy());
        return !follower.isBusy();
    }

    /**
     * Returns whether the robot is stuck. This is calculated by when the
     * robot isn't moving for a period of time
     *
     * @return whether the robot is stuck
     */
    public boolean isRobotStuck() {
        return follower.isRobotStuck();
    }

    /**
     * Sets the path chain for the command.
     *
     * @param pathChain the path chain to run
     */
    public void setPathChain(PathChain pathChain) {
        this.pathChain = pathChain;
    }

    /**
     * Sets the heading PIDF coefficients for the command.
     *
     * @param p the proportional coefficient
     * @param i the integral coefficient
     * @param d the derivative coefficient
     * @param f the feedforward coefficient
     */
    public void setHeadingPIDF(double p, double i, double d, double f) {
        headingPIDF = new PIDFCoefficients(p, i, d, f);
    }

    /**
     * Sets the predictive braking coefficients for the command.
     *
     * @param proportional      the proportional coefficient for predictive braking
     * @param linearBraking     the linear braking coefficient for predictive braking
     * @param quadraticFriction the quadratic friction coefficient for predictive braking
     */
    public void setPredictiveBrakingCoefficients(double proportional, double linearBraking, double quadraticFriction) {
        predictiveBrakingCoefficients = new PredictiveBrakingCoefficients(proportional, linearBraking, quadraticFriction);
    }

    /**
     * Sets the translational tolerance for the command.
     *
     * @param tolerance the translational tolerance in inches
     */
    public void setTolerance(double tolerance) {
        constraints.setTranslationalConstraint(tolerance);
    }

    /**
     * Sets the heading tolerance for the command.
     *
     * @param headingTolerance the heading tolerance in radians
     */
    public void setHeadingTolerance(double headingTolerance) {
        constraints.setHeadingConstraint(headingTolerance);
    }

    /**
     * Sets the timeout constraint for the command.
     *
     * @param timeout the amount of time in milliseconds before the command times out
     */
    public void setTimeoutConstraint(double timeout) {
        constraints.setTimeoutConstraint(timeout);
    }

    /**
     * Sets the velocity constraint for the command.
     *
     * @param velocity the velocity under which the command will be considered complete
     */
    public void setVelocityConstraint(double velocity) {
        constraints.setVelocityConstraint(velocity);
    }

    /**
     * Sets the t-value constraint for the command.
     *
     * @param tValue the t-value under which the command will be considered complete
     */
    public void setTValue(double tValue) {
        constraints.setTValueConstraint(tValue);
    }
}
