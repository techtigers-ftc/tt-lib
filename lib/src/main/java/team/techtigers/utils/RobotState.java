package team.techtigers.utils;

import com.pedropathing.geometry.Pose;

/**
 * A class to store information that is global to the entire robot. This is
 * intended to be extended from, and child classes can add in additional
 * attributes that are desired in the state.
 */
public class RobotState extends GlobalState{
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

    /**
     * @return the final pose of the robot in a trajectory
     */
    public Pose getRobotFinalPose() {
        return robotFinalPose;
    }

    /**
     * Sets the final pose of the robot in a trajectory
     *
     * @param robotFinalPose the final pose of the robot
     */
    public void setRobotFinalPose(Pose robotFinalPose) {
        this.robotFinalPose = robotFinalPose;
    }

    /**
     * @return the current voltage of the robot
     */
    public double getVoltage() {
        return voltage;
    }

    /**
     * Sets the current voltage of the robot
     *
     * @param voltage the current voltage of the robot
     */
    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    /**
     * @return the current state of the autonomous command
     */
    public String getCurrentAutoState() {
        return currentAutoState;
    }

    /**
     * Sets the current state of the autonomous command
     */
    public void setCurrentAutoState(String currentAutoState) {
        this.currentAutoState = currentAutoState;
    }

    /**
     * @return the previous state of the autonomous command
     */
    public String getPreviousAutoState() {
        return previousAutoState;
    }

    /**
     * Sets the previous state of the autonomous command
     */
    public void setPreviousAutoState(String previousAutoState) {
        this.previousAutoState = previousAutoState;
    }

    /**
     * @return the amount of time remaining in the autonomous
     */
    public double getAutoRemainingTime() {
        return (double) autoRemainingTime;
    }

    /**
     * Sets the amount of time remaining in the autonomous
     *
     * @param autoRemainingTime the amount of time remaining in the autonomous
     */
    public void setAutoRemainingTime(double autoRemainingTime) {
        this.autoRemainingTime = autoRemainingTime;
    }

    /**
     * @return the current drawn by the intake
     */
    public double getIntakeCurrent() {
        return intakeCurrent;
    }

    /**
     * Sets the current drawn by the intake
     *
     * @param intakeCurrent the current drawn by the intake
     */
    public void setIntakeCurrent(double intakeCurrent) {
        this.intakeCurrent = intakeCurrent;
    }
}