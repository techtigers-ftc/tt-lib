package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.utils.TTLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class CommandScheduler {
    private static CommandScheduler instance;
    private ArrayList<Command> commands;
    private Iterator<Command> commandIterator;


    private CommandScheduler() {
        commands = new ArrayList<>();
    }

    public static CommandScheduler getInstance() {
        if (instance == null) {
            instance = new CommandScheduler();
        }
        return instance;
    }

    public void schedule(Command... commands) {
        for (Command command : commands) {
            this.commands.add(command);
            command.initialize();
        }
    }

    public void update() {
        commandIterator = commands.iterator();
        while (commandIterator.hasNext()) {
            Command command = commandIterator.next();
            if (!command.isFinished()) {
                command.update();
            } else {
                command.end(false);
                commandIterator.remove();
            }
        }
    }

    public void cancel() {
        while (commandIterator.hasNext()) {
            Command command = commandIterator.next();
            command.end(true);
            commandIterator.remove();
        }
    }

    public void reset() {
        instance = null;
    }
}
