package team.techtigers.utils;

import com.pedropathing.math.Pose;

import java.util.HashMap;

/**
 * A class to store information that is global to the entire robot. This is
 * intended to be extended from, and child classes can add in additional
 * attributes that are desired in the state.
 */
public class RobotState extends GlobalState {
    private HashMap<String, Object> stateMap = new HashMap<>();
    private final boolean isBlue;
    private Pose robotPose;
    private Pose robotVelocity;
    private double driveCurrent;
    private double voltage;
    private Pose robotFinalPose;
    private String currentAutoState;
    private String previousAutoState;
    private double autoRemainingTime;
    private double intakeCurrent;

    /**
     * Creates a new RobotState with the specified alliance color.
     *
     * @param isBlue true if the robot is on the blue alliance, false if on the red alliance
     */
    public RobotState(boolean isBlue){
        this.isBlue = isBlue;
        robotPose = new Pose(0, 0, 0);
        robotVelocity = new Pose(0, 0, 0);
        driveCurrent = 0;
        voltage = 0;
        intakeCurrent = 0;
    }

    public <T> T get(String key) {
        if (stateMap.containsKey(key)) {
            return (T) stateMap.get(key);
        } else {
            TTLogger.ee("RobotState", "Key not found in stateMap: " + key);
            return null;
        }
    }

    public void set(String key, Object value) {
        stateMap.put(key, value);
    }

    /**
     * Returns true if the robot is on the blue alliance, false if on the red alliance.
     *
     * @return true if the robot is on the blue alliance, false if on the red alliance
     */
    public boolean isBlue() {
        return isBlue;
    }

    /**
     * @return the current pose of the robot (Inches and Radians)
     */
    public Pose getRobotPose() {
        return robotPose;
    }

    /**
     * @return the current velocity of the robot as a waypoint (x, y, heading) in Inches and Radians
     */
    public Pose getRobotVelocity() {
        return robotVelocity;
    }

    /**
     * Sets the current velocity of the robot as a waypoint (x, y, heading) in Inches and Radians
     *
     * @param robotVelocity the current velocity of the robot
     */
    public void setRobotVelocity(Pose robotVelocity) {
        this.robotVelocity = robotVelocity;
    }

    /**
     * Sets the current pose of the robot (Inches and Radians)
     *
     * @param robotPose the current pose of the robot
     */
    public void setRobotPose(Pose robotPose) {
        this.robotPose = robotPose;
    }

    /**
     * @return the total current drawn by the drive motors
     */
    public double getDriveCurrent() {
        return driveCurrent;
    }

    /**
     * Sets the total current drawn by the drive motors
     *
     * @param driveCurrent the total current drawn by the drive motors
     */
    public void setDriveCurrent(double driveCurrent) {
        this.driveCurrent = driveCurrent;
    }
}