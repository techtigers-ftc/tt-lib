package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utils.RobotState;

public class SubsystemExecutor {
    private static SubsystemExecutor instance;
    private Subsystem[] subsystems;

    private SubsystemExecutor() {
    }

    public static SubsystemExecutor getInstance() {
        if (instance == null) {
            instance = new SubsystemExecutor();
        }
        return instance;
    }

    public void registerSubsystem(HardwareMap hardwareMap, Telemetry telemetry, RobotState robotState, Subsystem... subsystems) {
       this.subsystems = subsystems;
       for (Subsystem subsystem : subsystems) {
           subsystem.setParameters(hardwareMap, telemetry, robotState);
       }
    }

    public void initLoop() {
        for (Subsystem subsystem : subsystems) {
            subsystem.initLoop();
        }
    }

    public void justAfterStart() {
        for (Subsystem subsystem : subsystems) {
            subsystem.justAfterStart();
        }
    }

    public void periodic() {
        for (Subsystem subsystem : subsystems) {
            subsystem.periodic();
        }
    }

    public void close() {
        for (Subsystem subsystem : subsystems) {
            subsystem.close();
        }
    }
}
