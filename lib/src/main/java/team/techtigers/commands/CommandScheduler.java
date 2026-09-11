package team.techtigers.commands;

import java.util.ArrayList;

import team.techtigers.gamepad.Trigger;

/**
 * Schedules commands and runs their lifecycle methods each control loop.
 */
public class CommandScheduler {
    private static CommandScheduler instance;
    private final ArrayList<Command> commands = new ArrayList<>();
    private final ArrayList<Trigger> triggers = new ArrayList<>();

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
     * Returns whether a command is currently scheduled.
     */
    public boolean isScheduled(Command command) {
        return commands.contains(command);
    }

    /**
     * Registers a trigger so its bindings are evaluated once per scheduler loop.
     * Trigger constructors call this automatically.
     */
    public void registerTrigger(Trigger trigger) {
        triggers.add(trigger);
    }

    /**
     * Runs scheduled commands and ends commands that have finished.
     */
    public void update() {
        for (Trigger trigger : new ArrayList<>(triggers)) {
            trigger.update();
        }

        for (int i = 0; i < commands.size(); i++) {
            Command command = commands.get(i);
            command.update();
            if (command.isFinished()) {
                command.end(false);
                commands.remove(i);
                i--;
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
