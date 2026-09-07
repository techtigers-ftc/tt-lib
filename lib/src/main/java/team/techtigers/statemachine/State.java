package team.techtigers.statemachine;

import team.techtigers.commands.Command;

/**
 * Interface for all team.techtigers.states, which are used to run a step of the state machine
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
     * Configures all the team.techtigers.commands for the state
     */
    void configureCommands();
}