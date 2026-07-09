package org.firstinspires.ftc.teamcode.commands;


import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utils.TTLogger;

public class SequentialCommandGroup extends CommandGroup {
    private Command currentCommand;
    private ElapsedTime timer;
    private boolean isFinished;

    public SequentialCommandGroup(Command... commands) {
        addCommands(commands);
        timer = new ElapsedTime();
    }

    @Override
    public void initialize() {
//        TTLogger.dd(tag, "Initialize Running on Sequential Command Group");
        currentCommand = commands.get(0);
        currentCommand.initialize();
        timer.reset();
    }

    @Override
    public void update() {
        if (currentCommand.isFinished()) {
            currentCommand.end(false);
            TTLogger.dd(tag, "End Ran on Previous Command");
            if ((commands.indexOf(currentCommand) + 1) < commands.size()) {
                currentCommand = commands.get(commands.indexOf(currentCommand) + 1);
                currentCommand.initialize();
                TTLogger.dd(tag, "Init Ran on Next Command");
            } else {
                isFinished = true;
            }
        } else {
            currentCommand.update();
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void end(boolean interrupted){
        isFinished = false;
    }
}
