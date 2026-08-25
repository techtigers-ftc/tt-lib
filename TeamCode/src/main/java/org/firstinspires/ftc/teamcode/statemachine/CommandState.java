package org.firstinspires.ftc.teamcode.statemachine;

import org.firstinspires.ftc.teamcode.commands.Command;

/**
 * State that extends Command, to be used in a state machine
 *
 * @param <T> The type of the condition, usually an enum
 */
public abstract class CommandState<T> implements State<T> {
    private final String name;

    /**
     * Constructor for the CommandState
     *
     * @param name The name of the state
     */
    public CommandState(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
