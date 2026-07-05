package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.utils.TTLogger;

import java.util.ArrayList;
import java.util.Iterator;

public class CommandScheduler {
    private static CommandScheduler instance;
    private ArrayList<Command> commands;
    private Iterator<Command> commandItterator;


    private CommandScheduler() {
        commands = new ArrayList<>();
        commandItterator = commands.iterator();
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
        while (commandItterator.hasNext()) {
            Command command = commandItterator.next();
            if (!command.isFinished()) {
                command.update();
            } else {
                command.end(false);
                commandItterator.remove();
            }
        }
    }

    public void cancel() {
        while (commandItterator.hasNext()) {
            Command command = commandItterator.next();
            command.end(true);
            commandItterator.remove();
        }
    }

    public void reset() {
        instance = null;
    }
}
