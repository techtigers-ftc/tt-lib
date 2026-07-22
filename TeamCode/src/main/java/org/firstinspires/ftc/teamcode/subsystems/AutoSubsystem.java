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
