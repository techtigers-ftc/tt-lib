package org.firstinspires.ftc.teamcode.commands;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

/**
 * A command that waits until a condition is met or a timeout is reached.
 */
public class TimeoutWaitUntilCommand extends WaitCommand {
    private BooleanSupplier condition;

    /**
     * Constructor for the TimeoutWaitUntilCommand
     *
     * @param condition The condition to wait for
     * @param timeout   The timeout
     * @param timeUnit the time unit to use
     */
    public TimeoutWaitUntilCommand(BooleanSupplier condition, double timeout, TimeUnit timeUnit) {
        super(timeout, timeUnit);
        this.condition = condition;
    }

    /**
     * Overload constructor for the TimeoutWaitUntilCommand, defaulting to milliseconds
     *
     * @param condition The condition to wait for
     * @param timeout   The timeout
     */
    public TimeoutWaitUntilCommand(BooleanSupplier condition, double timeout) {
        this(condition, timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isFinished() {
        return condition.getAsBoolean() || isTimeoutReached();
    }
}
