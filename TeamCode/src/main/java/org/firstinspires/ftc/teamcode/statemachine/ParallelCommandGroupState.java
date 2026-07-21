package org.firstinspires.ftc.teamcode.statemachine;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.commands.ParallelCommandGroup;

/**
 * State that extends ParallelCommandGroup, to be used in a state machine
 *
 * @param <T> The type of the condition, usually an enum
 */
public abstract class ParallelCommandGroupState<T> extends ParallelCommandGroup implements State<T> {
    private final String name;
    private double timeout;
    private final ElapsedTime timer;

    /**
     * Constructor for the ParallelCommandGroupState
     *
     * @param name The name of the state
     * @param timeout The max time of the state in seconds
     */
    public ParallelCommandGroupState(String name, double timeout) {
        this.name = name;
        this.timeout = timeout;
        timer = new ElapsedTime();
    }

    /**
     * Overload Constructor without timeout
     *
     * @param name The name of the state
     */
    public ParallelCommandGroupState(String name){
        this(name, -1);
    }

    @Override
    public void initialize(){
        super.initialize();
        timer.reset();
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * @return if timeout is valid and is exceeded returns true
     */
    protected final boolean isTimeoutReached(){
        return timeout > 0 && timer.seconds() > timeout;
    }

    /**
     * @return the amount of time that the command has been running for
     */
    protected final double getRunningTime() {
        return timer.seconds();
    }

    /**
     * @return the amount of time that the command has remaining until the timeout is reached
     */
    protected final double getRemainingTime() {
        return timeout - timer.seconds();
    }

    /**
     * Sets the timeout for the state
     *
     * @param timeout the timeout in seconds
     */
    protected final void setTimeout(double timeout) {
        this.timeout = timeout;
    }
}
