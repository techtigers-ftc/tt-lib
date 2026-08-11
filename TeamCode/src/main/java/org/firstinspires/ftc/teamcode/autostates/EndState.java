package org.firstinspires.ftc.teamcode.autostates;

import org.firstinspires.ftc.teamcode.statemachine.ParallelCommandGroupState;
import org.firstinspires.ftc.teamcode.utils.enums.AutoStateCondition;

/**
 * An end state for the state machine to end the autonomous
 */
public class EndState extends ParallelCommandGroupState<AutoStateCondition> {
    private static final String LOG_TAG =
            EndState.class.getSimpleName();

    /**
     * Constructor for the EndState
     *
     * @param name The name of the state
     */
    public EndState(String name) {
        super(name);
    }

    @Override
    public void configureCommands() {
        // No commands to add, this state is just a placeholder to end the autonomous
    }

    @Override
    public AutoStateCondition getCurrentCondition() {
        return AutoStateCondition.RUNNING;
    }
}
