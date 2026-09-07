package team.techtigers.commands;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Base class for command group which coordinate a collection of team.techtigers.commands.
 */
public abstract class CommandGroup extends CommandBase {

    protected ArrayList<Command> commands;

    /**
     * Sets the child team.techtigers.commands managed by this command group.
     *
     * @param commands the team.techtigers.commands managed by the group
     */
    protected void addCommands(Command... commands) {
        this.commands = new ArrayList<>();
        this.commands.addAll(Arrays.asList(commands));
    }
}
