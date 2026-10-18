package team.techtigers.autostates;

import com.pedropathing.follower.Follower;

import team.techtigers.utils.DriveStateConfig;
import team.techtigers.utils.RobotState;

/**
 * A basic drive state that drives the robot along a predefined path using the PedroPathing library.
 */
public class BasicDriveState<T> extends DriveStateBase<T> {
    /**
     * Constructs a BasicDriveState with the specified name, configuration, robot state, and timeout.
     *
     * @param name the name of the state
     * @param config the configuration for the drive state
     * @param robotState the robot state object
     * @param timeout the timeout for the state in seconds
     */
    public BasicDriveState(String name, DriveStateConfig<T> config, RobotState robotState, double timeout) {
        super(name, config, robotState, timeout);
    }

    @Override
    public void configureCommands() {
        addCommands(autoDriveCommand);
    }
}
