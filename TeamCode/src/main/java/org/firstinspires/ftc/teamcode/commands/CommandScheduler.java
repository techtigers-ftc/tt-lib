package org.firstinspires.ftc.teamcode.commands;

import java.util.ArrayList;

public class CommandScheduler {
    private static CommandScheduler instance;
    private ArrayList<Command> commands;

    private CommandScheduler() {
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
        for (Command command : commands) {
            if (!command.isFinished()) {
                command.update();
            } else {
                command.end(false);
                commands.remove(command);
            }
        }
    }

    public void cancel() {
        for (Command command : commands) {
            command.end(true);
            commands.remove(command);
        }
    }

    public void reset() {
        instance = null;
    }
}
