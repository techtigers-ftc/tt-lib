package org.firstinspires.ftc.teamcode.commands;

import androidx.annotation.CallSuper;

import org.firstinspires.ftc.teamcode.utils.TTLogger;

/**
 * Runs child commands one at a time in the order they were provided.
 */
public class SequentialCommandGroup extends CommandGroup {
    private int currentCommandIndex;
    private boolean isFinished;

    /**
     * Creates a group that runs the supplied commands in sequence.
     *
     * @param commands the commands to run in order
     */
    public SequentialCommandGroup(Command... commands) {
        addCommands(commands);
    }

    @Override
    @CallSuper
    public void initialize() {
        currentCommandIndex = 0;
        isFinished = false;
        CommandScheduler.getInstance().schedule(commands.get(currentCommandIndex));
    }

    @Override
    @CallSuper
    public void update() {
        if (commands.get(currentCommandIndex).isFinished()) {
            currentCommandIndex++;
            if (currentCommandIndex == commands.size()) {
                isFinished = true;
            } else {
                CommandScheduler.getInstance().schedule(commands.get(currentCommandIndex));
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
        if (interrupted && !isFinished) {
            CommandScheduler.getInstance().cancel(commands.get(currentCommandIndex));
        }
    }
}
