package org.firstinspires.ftc.teamcode.commands;


public class SequentialCommandGroup extends CommandGroup {
    private Command currentCommand;
    public SequentialCommandGroup (Command... commands) {
        super();
        addCommands(commands);
    }

    @Override
    public void initialize() {
        currentCommand = commands.get(0);
        currentCommand.initialize();
    }

    @Override
    public void update() {
        currentCommand.update();
        if (currentCommand.isFinished()){
            currentCommand.end(false);
            currentCommand = commands.get(commands.indexOf(currentCommand) + 1) ;
        }
    }
    @Override
    public boolean isFinished() {
        return commands.get(commands.size() - 1).isFinished();
    }
}
