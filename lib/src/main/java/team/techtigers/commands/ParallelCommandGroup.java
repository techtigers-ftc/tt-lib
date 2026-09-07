package team.techtigers.commands;

import androidx.annotation.CallSuper;

/**
 * Runs child team.techtigers.commands concurrently and completes when every child command finishes.
 */
public class ParallelCommandGroup extends CommandGroup {
    private boolean isFinished;

    /**
     * Creates a group that runs all supplied team.techtigers.commands in parallel.
     *
     * @param commands the team.techtigers.commands to run concurrently
     */
    public ParallelCommandGroup(Command... commands) {
        addCommands(commands);
    }

    @Override
    @CallSuper
    public void initialize() {
        isFinished = false;

        for (Command command : commands) {
            CommandScheduler.getInstance().schedule(command);
        }
    }

    @Override
    @CallSuper
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
    @CallSuper
    public void end(boolean interrupted) {
        if (interrupted) {
            for (Command command : commands) {
                if (!command.isFinished()) {
                    CommandScheduler.getInstance().cancel(command);
                }
            }
        }
    }
}
