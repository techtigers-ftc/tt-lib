package team.techtigers.commands;

/**
 * Template class for creating commands
 * Necessary due to the structure of the state machine
 */
public abstract class CommandBase implements Command {
    protected String tag = this.getClass().getSimpleName();
}
