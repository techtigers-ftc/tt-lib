package org.firstinspires.ftc.teamcode.commands;

import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

/**
 * Wait command that waits for a specified duration
 */
public class WaitCommand extends CommandBase {
    private final ElapsedTime timer;
    private final TimeUnit timeUnit;
    private double duration;

    /**
     * Constructor for WaitCommand
     *
     * @param duration the duration to wait
     * @param timeUnit the unit of time for the duration
     */
    public WaitCommand(double duration, TimeUnit timeUnit) {
        timer = new ElapsedTime();
        this.duration = duration;
        this.timeUnit = timeUnit;
    }

    /**
     * Overloaded Constructor for WaitCommand with milliseconds as default time unit
     *
     * @param duration the duration to wait in milliseconds
     */
    public WaitCommand(double duration) {
        this(duration, TimeUnit.MILLISECONDS);
    }

    @Override
    public void initialize() {
        timer.reset();
    }

    @Override
    public boolean isFinished() {
        return getRunningTime() >= duration;
    }

    /**
     * Sets the duration of the wait command
     *
     * @param duration the new duration to wait
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * @return the amount of time that the command has been running for
     */
    protected final double getRunningTime() {
        if (timeUnit.equals(TimeUnit.MILLISECONDS)) {
            return timer.milliseconds();
        } else {
            return timer.seconds();
        }
    }

    /**
     * @return the amount of time that the command has remaining until the timeout is reached
     */
    protected final double getRemainingTime() {
        return duration - getRunningTime();
    }

    /**
     * @return if timeout is valid and is exceeded returns true
     */
    protected final boolean isTimeoutReached(){
        return duration > 0 && getRunningTime() > duration;
    }
}
