package team.techtigers.subsystems;

import team.techtigers.statemachine.StateMachine;
import team.techtigers.utils.RobotState;
import team.techtigers.utils.enums.AutoStateCondition;

/**
 * A subsystem for autonomous team.techtigers.commands
 */
public class AutoSubsystem extends Subsystem {
    private final StateMachine<AutoStateCondition> stateMachine;
    private final RobotState robotState;
    public static final double SMALL_TOLERANCE = 0.75;
    public static final double MEDIUM_TOLERANCE = 1.5;
    public static final double LARGE_TOLERANCE = 3.0;
    public static final double SMALL_ANGLE_TOLERANCE = 1.0;
    public static final double MEDIUM_ANGLE_TOLERANCE = 2.0;
    public static final double LARGE_ANGLE_TOLERANCE = 4.0;

    /**
     * Constructor for the AutoSubsystem
     *
     * @param stateMachine The state machine for the autonomous command
     * @param robotState   Reference to the robot state - will be updated with the current and
     *                     previous state of the state machine.
     */
    public AutoSubsystem(StateMachine<AutoStateCondition> stateMachine, RobotState robotState) {
        this.stateMachine = stateMachine;
        this.robotState = robotState;
    }

    @Override
    public void justAfterStart() {
        stateMachine.start();
    }

    @Override
    public void periodic() {
        stateMachine.update();
        robotState.set("previousAutoState", stateMachine.getPreviousState());
        robotState.set("currentAutoState", stateMachine.getCurrentState());
        robotState.set("autoRemainingTime", 30 - robotState.getRunTime() / 1000.0);
    }
}
