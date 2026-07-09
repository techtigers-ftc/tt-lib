package org.firstinspires.ftc.teamcode.commands;

import org.firstinspires.ftc.teamcode.utils.TTLogger;

public class ParallelCommandGroup extends CommandGroup {

    public ParallelCommandGroup (Command... commands) {
        addCommands(commands);
    }

    @Override
    public void initialize() {
        for (Command command : commands) {
            command.initialize();
        }
    }

    @Override
    public void update() {
        for (Command command : commands) {
            TTLogger.dd(tag, "Update Running");
            if (command.isFinished()) {
                command.end(false);
            } else {
                command.update();
            }
        }
    }

    @Override
    public boolean isFinished() {
        TTLogger.dd(tag, "---------------------------------------------------");
        for (Command command : commands) {
            TTLogger.dd(tag, "Is Command Finished: %b", command.isFinished());
           if (!command.isFinished()) {
               return false;
           }
        }
        return true;
    }
}
