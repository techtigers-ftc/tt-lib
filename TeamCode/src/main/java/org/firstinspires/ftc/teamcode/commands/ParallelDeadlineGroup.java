package org.firstinspires.ftc.teamcode.commands;

/**
 * Runs child commands concurrently until the deadline command finishes.
 */
public class ParallelDeadlineGroup extends CommandGroup {
    private final Command deadline;
    private boolean isFinished;

    /**
     * Creates a group that ends when its deadline command completes.
     *
     * @param deadline the command that determines when the group ends
     * @param commands the commands to run alongside the deadline
     */
    public ParallelDeadlineGroup(Command deadline, Command... commands) {
        this.deadline = deadline;
        addCommands(commands);
    }

    @Override
    public void initialize() {
        isFinished = false;
        CommandScheduler scheduler = CommandScheduler.getInstance();
        scheduler.schedule(deadline);
        scheduler.schedule(commands.toArray(new Command[0]));
    }

    @Override
    public void update() {
       isFinished = deadline.isFinished();
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
