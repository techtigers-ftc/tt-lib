package org.firstinspires.ftc.teamcode.utils;


public class RobotState extends GlobalState{

    private final boolean isBlue;

    public RobotState(boolean isBlue){
        this.isBlue = isBlue;
    }

    public boolean isBlue() {
        return isBlue;
    }
}
