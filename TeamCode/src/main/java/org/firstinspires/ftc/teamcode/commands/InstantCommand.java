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

    /**
     * Overload constructor for InstantCommand that does not use a runnable.
     * The initialize should be overridden in this case to provide the desired behavior.
     */
    public InstantCommand() {
        runnable = () -> {};
    }

    @Override
    public void initialize() {
        runnable.run();
    }

    @Override
    public final boolean isFinished() {
        return true;
    }
}
