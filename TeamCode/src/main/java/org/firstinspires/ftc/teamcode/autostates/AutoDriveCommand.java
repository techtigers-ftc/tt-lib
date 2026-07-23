package org.firstinspires.ftc.teamcode.autostates;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PredictiveBrakingCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierPoint;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.paths.PathConstraints;

import org.firstinspires.ftc.teamcode.commands.CommandBase;
import org.firstinspires.ftc.teamcode.utils.RobotState;

/**
 * A class for autonomous drive commands that use PedroPathing.
 */
public class AutoDriveCommand extends CommandBase {
    private static final String LOG_TAG =
            AutoDriveCommand.class.getSimpleName();

    private final RobotState robotState;
    public Follower follower;
    private PathChain pathChain;
    private PathChain previousPathChain;

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

        // Sets the previous path chain to the one set initially
        previousPathChain = pathChain;

        // Sets the robot's final pose to the final waypoint found
        robotState.setRobotFinalPose(target);
        follower.followPath(pathChain, true);
    }

    @Override
    public void update() {
//        if (pathChain != previousPathChain) {
//            follower = new Follower(localizer);
//            follower.setTranslationalPIDF(translationalPIDF.getCoefficients());
//            follower.setHeadingPIDF(headingPIDF.getCoefficients());
//            follower.setDrivePIDF(drivePIDF.getCoefficients());
//            follower.disableSecondaryPIDS();
//            follower.followPath(pathChain, true);
//            previousPathChain = pathChain;
//        }
        follower.update();
    }

    @Override
    public void end(boolean interrupted) {
        // Holds the robots current position and heading, and internally stops any concurrent following
        follower.holdPoint(new BezierPoint(robotState.getRobotPose().getX(),
                robotState.getRobotPose().getY()),
                robotState.getRobotPose().getHeading(), true);
    }

    @Override
    public boolean isFinished() {
        return follower.isBusy();
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
