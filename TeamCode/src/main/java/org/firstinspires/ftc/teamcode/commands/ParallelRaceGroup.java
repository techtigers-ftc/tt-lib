package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.utils.TTLogger;

public class ParallelRaceGroup extends CommandGroup {
    private boolean isFinished;

    public ParallelRaceGroup(Command... commands) {
        addCommands(commands);
    }

    @Override
    public void initialize() {
        isFinished = false;

        for (Command command: commands) {
            CommandScheduler.getInstance().schedule(command);
        }
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
        TTLogger.dd(tag, "IsFinished %b", isFinished);
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
