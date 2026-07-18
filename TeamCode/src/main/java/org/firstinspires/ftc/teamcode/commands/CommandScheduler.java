package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.gamepad.Trigger;

import java.util.ArrayList;

public class CommandScheduler {
    private static CommandScheduler instance;
    private final ArrayList<Command> commands = new ArrayList<>();
    private final ArrayList<Trigger> triggers = new ArrayList<>();

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

    /**
     * Registers a trigger so its bindings are evaluated once per scheduler loop.
     * Trigger constructors call this automatically.
     */
    public void registerTrigger(Trigger trigger) {
        triggers.add(trigger);
    }

    public void update() {
        for (Trigger trigger : triggers) {
            trigger.update();
        }

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
