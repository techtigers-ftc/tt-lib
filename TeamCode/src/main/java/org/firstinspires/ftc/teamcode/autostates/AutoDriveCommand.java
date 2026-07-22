package org.firstinspires.ftc.teamcode.autostates;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PredictiveBrakingCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;

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

    // Primary PIDF Controllers
    private PIDFCoefficients headingPIDF;
    private PredictiveBrakingCoefficients predictiveBrakingCoefficients;

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
    }

    @Override
    public void initialize() {
        // Makes sure that all primary PIDF coefficients are set
        if (headingPIDF == null) {
            throw new IllegalArgumentException("Heading PIDF coefficients not set");
        }
        if (predictiveBrakingCoefficients == null) {
            throw new IllegalArgumentException("Predictive Braking coefficients not set");
        }

        // Makes sure that a path chain is set
        if (pathChain == null) {
            throw new IllegalArgumentException("Path chain not set");
        }

        // Sets the primary PIDF coefficients
        follower.setHeadingPIDFCoefficients(headingPIDF);
        follower.setConstants(follower.getConstants().predictiveBrakingCoefficients(predictiveBrakingCoefficients));

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
        follower.breakFollowing();
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
}
