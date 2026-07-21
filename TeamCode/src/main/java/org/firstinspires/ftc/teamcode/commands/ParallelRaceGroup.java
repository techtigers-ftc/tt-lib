package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.utils.TTLogger;

/**
 * Runs child commands concurrently and completes when the first child command finishes.
 */
public class ParallelRaceGroup extends CommandGroup {
    private boolean isFinished;

    /**
     * Creates a group that races the supplied commands.
     *
     * @param commands the commands to run until one finishes
     */
    public ParallelRaceGroup(Command... commands) {
        addCommands(commands);
    }

    @Override
    public void initialize() {
        isFinished = false;
       CommandScheduler.getInstance().schedule(commands.toArray(new Command[0]));
    }

    @Override
    public void update() {
        for (Command command : commands) {
            if (command.isFinished()) {
                isFinished = true;
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
       for (Command command: commands) {
           if (!command.isFinished()) {
               CommandScheduler.getInstance().cancel(command);
           }
       }
    }
}
