package org.firstinspires.ftc.teamcode.commands;

/**
 * Represents an action that can be scheduled and run by the command scheduler.
 */
public interface Command {
    /**
     * Initializes the command before its first update.
     */
    default void initialize() {
    }

    /**
     * Runs one iteration of the command while it is scheduled.
     */
    default void update() {
    }

    /**
     * Determines whether the command has completed.
     *
     * @return true when the command should be ended and removed from the scheduler
     */
    default boolean isFinished() {
        return false;
    }

    /**
     * Cleans up the command after completion or interruption.
     *
     * @param interrupted true when the command was canceled before completing
     */
    default void end(boolean interrupted) {

    }
}
