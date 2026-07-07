package org.firstinspires.ftc.teamcode.commands;


import java.util.ArrayList;
import java.util.Iterator;

public class CommandScheduler {
    private static CommandScheduler instance;
    private ArrayList<Command> commands;
    private Iterator<Command> commandItterator;


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
        commandItterator = commands.iterator();
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
