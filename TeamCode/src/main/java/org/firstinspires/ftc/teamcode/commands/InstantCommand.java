package org.firstinspires.ftc.teamcode.commands;

/**
 * Runs an action once when the command is initialized.
 */
public class InstantCommand extends CommandBase {

    private Runnable runnable;

    /**
     * Creates a command that runs the supplied action immediately.
     *
     * @param runnable the action to run
     */
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
