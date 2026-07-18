package org.firstinspires.ftc.teamcode.commands;

import java.util.ArrayList;

public class CommandScheduler {
    private static CommandScheduler instance;
    private final ArrayList<Command> commands = new ArrayList<>();

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
        for (int i = 0; i < commands.size(); i++) {
            Command command = commands.get(i);
            if (command.isFinished()) {
                command.end(false);
                commands.remove(i);
                i--;
            } else {
                command.update();
            }
        }
    }

    public void cancel(Command... commands) {
        for (Command command : commands) {
            if (this.commands.remove(command)) {
                command.end(true);
            }
        }
    }

    public void reset() {
        instance = null;
    }
}
