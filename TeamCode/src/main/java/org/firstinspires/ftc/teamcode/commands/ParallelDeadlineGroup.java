package org.firstinspires.ftc.teamcode.commands;

public class ParallelDeadlineGroup extends CommandGroup {
    private final Command deadline;
    private boolean isFinished;

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
