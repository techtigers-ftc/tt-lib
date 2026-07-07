package org.firstinspires.ftc.teamcode.commands;


import org.firstinspires.ftc.teamcode.utils.TTLogger;

public class SequentialCommandGroup extends CommandGroup {
    private Command currentCommand;

    public SequentialCommandGroup(Command... commands) {
        addCommands(commands);
    }

    @Override
    public void initialize() {
        TTLogger.dd(tag, "Initalize Running on Sequential Command Greoup");
        currentCommand = commands.get(0);
        currentCommand.initialize();
    }

    @Override
    public void update() {
        currentCommand.update();
        if (currentCommand.isFinished()) {
            currentCommand.end(false);
            try {
                currentCommand = commands.get(commands.indexOf(currentCommand) + 1);
                currentCommand.initialize();
            } catch (IndexOutOfBoundsException e) {
                // No more commands to execute
            }
        }
    }

    @Override
    public boolean isFinished() {
        TTLogger.dd(tag, "Is Finished: %b", commands.get(commands.size() - 1).isFinished());
        for (Command command: commands){
            TTLogger.dd(tag, "Each Command Is Finished %b", command.isFinished());
        }
        return commands.get(commands.size() - 1).isFinished();
    }
}
