package org.firstinspires.ftc.teamcode.commands;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Base class for command group which coordinate a collection of commands.
 */
public abstract class CommandGroup extends CommandBase {

    protected ArrayList<Command> commands;

    /**
     * Sets the child commands managed by this command group.
     *
     * @param commands the commands managed by the group
     */
    protected void addCommands(Command... commands) {
        this.commands = new ArrayList<>();
        this.commands.addAll(Arrays.asList(commands));
    }
}
