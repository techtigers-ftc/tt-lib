package org.firstinspires.ftc.teamcode.commands;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class CommandGroup extends Command {

    protected ArrayList<Command> commands;

    public CommandGroup() {
        commands = new ArrayList<>();
    }

    protected void addCommands(Command... commands) {
        this.commands.addAll(Arrays.asList(commands));
    }
}
