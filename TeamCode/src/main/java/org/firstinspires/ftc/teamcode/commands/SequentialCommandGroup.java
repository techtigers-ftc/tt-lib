package org.firstinspires.ftc.teamcode.commands;


import org.firstinspires.ftc.teamcode.utils.TTLogger;

public class SequentialCommandGroup extends CommandGroup {
    private Command currentCommand;

    public SequentialCommandGroup(Command... commands) {
        addCommands(commands);
    }

    @Override
    public void initialize() {
//        TTLogger.dd(tag, "Initialize Running on Sequential Command Group");
        currentCommand = commands.get(0);
        currentCommand.initialize();
    }

    @Override
    public void update() {
        if (currentCommand.isFinished()) {
            currentCommand.end(false);
            if ((commands.indexOf(currentCommand) + 1) < commands.size()) {
                currentCommand = commands.get(commands.indexOf(currentCommand) + 1);
                currentCommand.initialize();
            }
        } else {
            currentCommand.update();
        }
    }

    @Override
    public boolean isFinished() {
        for (Command command : commands) {
            if (!command.isFinished()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void end(boolean interrupted){
       for (Command command: commands){
           command.initialize();
       }
    }
}
