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
        for (Command command : commands) {
            command.initialize();
        }
    }

    @Override
    public void update() {
        TTLogger.dd(tag, "------------------------------------");
        for (Command command : commands) {
            TTLogger.dd(tag, "IsFinished %b", command.isFinished());
            if (command.isFinished()) {
                command.end(false);
                commands.forEach(c -> c.end(true));
                isFinished = true;
            } else if (!isFinished) {
                command.update();
            }
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

//    @Override
//    public void end(boolean interruptible) {
//       isFinished = false;
//    }
}
