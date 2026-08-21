package org.firstinspires.ftc.teamcode.commands;

/**
 * Represents an action that can be scheduled and run by the command scheduler.
 */
public abstract class Command {
    protected String tag = this.getClass().getSimpleName();

    /**
     * Initializes the command before its first update.
     */
    protected void initialize(){

    }

    /**
     * Runs one iteration of the command while it is scheduled.
     */
    protected void update(){

    }

    /**
     * Determines whether the command has completed.
     *
     * @return true when the command should be ended and removed from the scheduler
     */
    public abstract boolean isFinished();

    /**
     * Cleans up the command after completion or interruption.
     *
     * @param interrupted true when the command was canceled before completing
     */
    protected void end(boolean interrupted){

    }
}
