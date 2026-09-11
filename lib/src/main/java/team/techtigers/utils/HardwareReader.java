package team.techtigers.utils;

import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * A class which takes a lambda function which contains hardware reads. This class will then
 * stagger those reads to occur at a minimum interval in order to reduce cycle times
 */
public class HardwareReader {
    private final ElapsedTime intervalTimer;
    private final ElapsedTime operationTimer;
    private final double readInterval;

    /**
     * Constructs a HardwareReader with the specified read interval.
     *
     * @param intervalMilliseconds The interval in milliseconds between hardware reads.
     */
    public HardwareReader(double intervalMilliseconds) {
        this.intervalTimer = new ElapsedTime();
        this.operationTimer = new ElapsedTime();
        this.readInterval = intervalMilliseconds;
        intervalTimer.reset();
    }

    /**
     * Calls the provided readFunction if the specified interval has passed since the last call.
     *
     * @param readFunction A Runnable containing hardware read operations.
     * @return the time for the hardware read, in milliseconds
     */
    public double read(Runnable readFunction) {
        double initialCT = operationTimer.milliseconds();
        if (intervalTimer.milliseconds() >= readInterval) {
            readFunction.run();
            intervalTimer.reset();
        }
        double finalCT = operationTimer.milliseconds();
        return finalCT - initialCT;
    }
}
