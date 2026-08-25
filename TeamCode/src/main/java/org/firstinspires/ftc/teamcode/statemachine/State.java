package org.firstinspires.ftc.teamcode.statemachine;

import org.firstinspires.ftc.teamcode.commands.Command;

/**
 * Interface for all states, which are used to run a step of the state machine
 *
 * @param <T> The type of the condition, usually an enum
 */
public interface State<T> extends Command {
    /**
     * Returns the current condition of the state every update cycle
     */
    T getCurrentCondition();

    /**
     * Returns the name of the state
     */
    String getName();

    /**
     * Configures all the commands for the state
     */
    void configureCommands();
}