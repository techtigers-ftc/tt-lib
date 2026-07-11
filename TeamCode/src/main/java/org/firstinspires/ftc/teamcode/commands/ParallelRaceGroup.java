package org.firstinspires.ftc.teamcode.commands;

public class ParallelRaceGroup extends CommandGroup {

    public ParallelRaceGroup(Command... commands) {
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
            if (command.isFinished()){
                command.end(false);
                commands.remove(command);
                commands.forEach(c -> c.end(true));
            } else {
                command.update();
            }
        }
    }

    @Override
    public boolean isFinished() {
        for (Command command : commands) {
            if (command.isFinished()) {
                return true;
            }
        }
        return false;
    }
}
