package org.firstinspires.ftc.teamcode.commands;

public class ParallelCommandGroup extends CommandGroup {
    private boolean isFinished;

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
