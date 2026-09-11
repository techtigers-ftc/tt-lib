package team.techtigers.statemachine;

import androidx.annotation.CallSuper;

import com.qualcomm.robotcore.util.ElapsedTime;

import team.techtigers.commands.SequentialCommandGroup;

/**
 * State that extends SequentialCommandGroup, to be used in a state machine
 *
 * @param <T> The type of the condition, usually an enum
 */
public abstract class SequentialCommandGroupState<T> extends SequentialCommandGroup implements State<T> {
    private final String name;
    private double timeout;
    private final ElapsedTime timer;
    /**
     * Constructor for the SequentialCommandGroupState
     *
     * @param name The name of the state
     * @param timeout the max time of the state in seconds
     */
    public SequentialCommandGroupState(String name, double timeout) {
        this.name = name;
        this.timeout = timeout;
        timer = new ElapsedTime();
    }

    /**
     * Overload Constructor without timeout
     *
     * @param name The name of the state
     */
    public SequentialCommandGroupState(String name){
        this(name, -1);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    @CallSuper
    public void initialize(){
        super.initialize();
        timer.reset();
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
