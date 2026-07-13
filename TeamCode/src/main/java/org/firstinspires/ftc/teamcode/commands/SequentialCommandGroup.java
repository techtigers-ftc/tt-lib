package org.firstinspires.ftc.teamcode.commands;

import androidx.annotation.CallSuper;

public class SequentialCommandGroup extends CommandGroup {
    private Command currentCommand;
    private boolean isFinished;

    public SequentialCommandGroup(Command... commands) {
        addCommands(commands);
    }

    @Override
    @CallSuper
    public void initialize() {
        currentCommand = commands.get(0);
        currentCommand.initialize();
    }

    @Override
    @CallSuper
    public void update() {
        if (currentCommand.isFinished()) {
            currentCommand.end(false);
            if ((commands.indexOf(currentCommand) + 1) < commands.size()) {
                currentCommand = commands.get(commands.indexOf(currentCommand) + 1);
                currentCommand.initialize();
            } else {
                isFinished = true;
            }
        } else {
            currentCommand.update();
        }
    }

    @Override
    @CallSuper
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void end(boolean interrupted){
        isFinished = false;
    }
}
