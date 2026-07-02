package org.firstinspires.ftc.teamcode.commands;

public class ParallelCommandGroup extends CommandGroup {

    public ParallelCommandGroup (Command... commands) {
        super();
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
            if (!command.isFinished()){
                command.update();
            } else {
                command.end(false);
            }
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
}
