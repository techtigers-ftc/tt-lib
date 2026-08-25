package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.utils.HardwareReader;
import org.firstinspires.ftc.teamcode.utils.RobotState;
import org.firstinspires.ftc.teamcode.utils.SlidingAverageCalculator;

import java.util.ArrayList;

/**
 * A subsystem that controls the robot's sensors.
 */
public class SensorSubsystem extends Subsystem {
    private final RobotState robotState;
    private final VoltageSensor voltage;
    private final SlidingAverageCalculator voltageAverage;
    private final HardwareReader sensorReader;
    private final double SENSOR_UPDATE_INTERVAL_MS = 50;
    private double currentVoltage;

    /**
     * Constructs a new SensorSubsystem.
     *
     * @param hardwareMap The hardware map, used to get hardware references
     * @param robotState  The robot state, used to get the robot's current state
     */
    public SensorSubsystem(HardwareMap hardwareMap, RobotState robotState) {
        this.robotState = robotState;

        // Initialization of voltage sensor
        voltage = hardwareMap.voltageSensor.iterator().next();
        voltageAverage = new SlidingAverageCalculator(200);
        voltageAverage.clear();
        currentVoltage = 0;

        sensorReader = new HardwareReader(SENSOR_UPDATE_INTERVAL_MS);
    }

    /**
     * Reads all hardware sensors at a fixed interval
     */
    public void read() {
        sensorReader.read(() -> {
            currentVoltage = voltage.getVoltage();
        });
    }

    @Override
    public void periodic() {
        read();
        robotState.setVoltage(currentVoltage);
    }
}