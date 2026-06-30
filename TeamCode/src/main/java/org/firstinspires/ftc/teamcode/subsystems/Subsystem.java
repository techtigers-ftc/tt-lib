package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.RobotState;

public abstract class Subsystem {
    protected HardwareMap hardwareMap;
    protected Telemetry telemetry;
    protected RobotState robotState;
    public abstract void initLoop();
    public abstract void justAfterStart();
    public abstract void periodic();
    public abstract void close();

    public void getParameters(HardwareMap hardwareMap, Telemetry telemetry, RobotState robotState){
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        this.robotState = robotState;
    };
}
