package org.firstinspires.ftc.teamcode.statemachine;

import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

import team.techtigers.BaseCommand;

/**
 * Wait command that waits for a specified duration
 */
public class WaitCommand extends BaseCommand {
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
    public void init() {
        timer.reset();
    }

    @Override
    public boolean isFinished() {
        double currentTime = timeUnit == TimeUnit.SECONDS ? timer.seconds() : timer.milliseconds();
        return currentTime >= duration;
    }

    /**
     * Sets the duration of the wait command
     *
     * @param duration the new duration to wait
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }
}
