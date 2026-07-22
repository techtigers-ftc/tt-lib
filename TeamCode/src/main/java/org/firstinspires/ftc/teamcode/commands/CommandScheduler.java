package org.firstinspires.ftc.teamcode.commands;

import java.util.ArrayList;

/**
 * Schedules commands and runs their lifecycle methods each control loop.
 */
public class CommandScheduler {
    private static CommandScheduler instance;
    private final ArrayList<Command> commands = new ArrayList<>();

    private CommandScheduler() {
    }

    /**
     * Returns the shared command scheduler instance.
     *
     * @return the active command scheduler
     */
    public static CommandScheduler getInstance() {
        if (instance == null) {
            instance = new CommandScheduler();
        }
        return instance;
    }

    /**
     * Adds commands to the scheduler and initializes them.
     *
     * @param commands the commands to schedule
     */
    public void schedule(Command... commands) {
        for (Command command : commands) {
            this.commands.add(command);
            command.initialize();
        }
    }

    /**
     * Runs scheduled commands and ends commands that have finished.
     */
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

    /**
     * Interrupts and removes the specified scheduled commands.
     *
     * @param commands the commands to cancel
     */
    public void cancel(Command... commands) {
        for (Command command : commands) {
            if (this.commands.remove(command)) {
                command.end(true);
            }
        }
    }

    /**
     * Clears the shared scheduler instance.
     */
    public void reset() {
        instance = null;
    }
}
