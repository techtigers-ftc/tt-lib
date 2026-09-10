package team.techtigers.autostates;

import com.pedropathing.follower.Follower;


import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import team.techtigers.commands.CommandBase;
import team.techtigers.utils.RobotState;
import team.techtigers.utils.TTLogger;

/**
 * A class for autonomous drive team.techtigers.commands that use PedroPathing.
 */
public class AutoDriveCommand extends CommandBase {
    private static final String LOG_TAG =
            AutoDriveCommand.class.getSimpleName();

    private final RobotState robotState;
    public Follower follower;
    private Path path;

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
        // Makes sure that a path chain is set
        if (path == null) {
            throw new IllegalArgumentException("Path chain not set");
        }

        // Finds the final waypoint in the path chain
        Pose target =
                path.endPose();

        // Sets the robot's final pose to the final waypoint found
        robotState.set("robotFinalPose", target);
        follower.follow(path);

        TTLogger.dd(tag, "follower intiailizeing");
    }

    @Override
    public void update() {
        follower.update();
        TTLogger.dd(tag, "follwering updating, roboto pose: %s", robotState.getRobotPose().toString());
    }

    @Override
    public void end(boolean interrupted) {
        TTLogger.dd(tag, "follower done followuing");

        // Holds the robots current position and heading, and internally stops any concurrent following
        follower.hold((Pose) robotState.get("robotPose"));
    }

    @Override
    public boolean isFinished() {
        TTLogger.dd(tag, "Folllower busy: %b", follower.isBusy());
        return !follower.isBusy();
    }

    /**
     * Sets the path chain for the command.
     *
     * @param path the path chain to run
     */
    public void setPath(Path path) {
        this.path = path;
    }
}