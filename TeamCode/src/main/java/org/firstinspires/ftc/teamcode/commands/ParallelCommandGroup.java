package org.firstinspires.ftc.teamcode.commands;

/**
 * Runs child commands concurrently and completes when every child command finishes.
 */
public class ParallelCommandGroup extends CommandGroup {
    private boolean isFinished;

    /**
     * Creates a group that runs all supplied commands in parallel.
     *
     * @param commands the commands to run concurrently
     */
    public ParallelCommandGroup(Command... commands) {
        addCommands(commands);
    }

    @Override
    public void initialize() {
        isFinished = false;

        for (Command command : commands) {
            CommandScheduler.getInstance().schedule(command);
        }
    }

    @Override
    public void update() {
        isFinished = true;
        for (Command command : commands) {
            if (!command.isFinished()) {
                isFinished = false;
                return;
            }
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void end(boolean interrupted) {
        for (Command command : commands) {
            if (!command.isFinished()) {
                CommandScheduler.getInstance().cancel(command);
            }
        }
    }
}
