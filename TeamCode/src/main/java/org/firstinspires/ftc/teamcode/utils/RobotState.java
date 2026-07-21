package org.firstinspires.ftc.teamcode.utils;

/**
 * A class to store information that is global to the entire robot. This is
 * intended to be extended from, and child classes can add in additional
 * attributes that are desired in the state.
 */
public class RobotState extends GlobalState{

    private final boolean isBlue;

    /**
     * Creates a new RobotState with the specified alliance color.
     *
     * @param isBlue true if the robot is on the blue alliance, false if on the red alliance
     */
    public RobotState(boolean isBlue){
        this.isBlue = isBlue;
    }

    /**
     * Returns true if the robot is on the blue alliance, false if on the red alliance.
     *
     * @return true if the robot is on the blue alliance, false if on the red alliance
     */
    public boolean isBlue() {
        return isBlue;
    }
}
