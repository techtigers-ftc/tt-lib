package org.firstinspires.ftc.teamcode.subsystems;

import org.firstinspires.ftc.teamcode.statemachine.StateMachine;
import org.firstinspires.ftc.teamcode.utils.enums.AutoStateCondition;
import org.firstinspires.ftc.teamcode.utils.RobotState;

/**
 * A subsystem for autonomous commands
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
        robotState.setPreviousAutoState(stateMachine.getPreviousState());
        robotState.setCurrentAutoState(stateMachine.getCurrentState());
        robotState.setAutoRemainingTime(30 - robotState.getRunTime() / 1000.0);
    }
}
