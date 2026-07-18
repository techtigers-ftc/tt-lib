package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.utils.TTLogger;

public class SequentialCommandGroup extends CommandGroup {
    private int currentCommandIndex;
    private boolean isFinished;

    public SequentialCommandGroup(Command... commands) {
        addCommands(commands);
    }

    @Override
    public void initialize() {
        currentCommandIndex = 0;
        isFinished = false;
        CommandScheduler.getInstance().schedule(commands.get(currentCommandIndex));
    }

    @Override
    public void update() {
        if (isFinished) {
            return;
        }

        if (commands.get(currentCommandIndex).isFinished()) {
            currentCommandIndex++;
            if (currentCommandIndex == commands.size()) {
                isFinished = true;
            } else {
                TTLogger.dd(tag, "Scheduled next Command, Index: %d", currentCommandIndex);
                CommandScheduler.getInstance().schedule(commands.get(currentCommandIndex));
            }
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted && !isFinished) {
            CommandScheduler.getInstance().cancel(commands.get(currentCommandIndex));
        }
    }
}
