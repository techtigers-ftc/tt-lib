package team.techtigers.autostates;

import com.pedropathing.follower.Follower;

import team.techtigers.utils.RobotState;

/**
 * A basic drive state that drives the robot along a predefined path using the PedroPathing library.
 */
public class BasicDriveState extends DriveStateBase {
    public BasicDriveState(String name, Follower follower, RobotState robotState, double timeout) {
        super(name, follower, robotState, timeout);
    }

    @Override
    public void configureCommands() {
        addCommands(autoDriveCommand);
    }
}
