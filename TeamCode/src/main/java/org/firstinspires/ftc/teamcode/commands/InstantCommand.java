package org.firstinspires.ftc.teamcode.commands;

public class InstantCommand extends Command {

    private Runnable runnable;

    public InstantCommand(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void initialize() {
        runnable.run();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
