package org.firstinspires.ftc.teamcode.commands;

public class ParallelDeadlineGroup extends CommandGroup {
    private Command deadline;

    public ParallelDeadlineGroup(Command deadline, Command... commands) {
        this.deadline = deadline;
        addCommands(commands);
    }

    @Override
    public void initialize() {
        deadline.initialize();
        for (Command command : commands) {
            command.initialize();
        }
    }

    @Override
    public void update() {
       if (!deadline.isFinished()) {
           deadline.update();
           for (Command command : commands) {
               if (!command.isFinished()) {
                   command.update();
               } else {
                   command.end(false);
               }
           }
       } else {
           deadline.end(false);
           for (Command command : commands) {
                    command.end(true);
              }
       }
    }

    @Override
    public boolean isFinished() {
       return deadline.isFinished();
    }
}
