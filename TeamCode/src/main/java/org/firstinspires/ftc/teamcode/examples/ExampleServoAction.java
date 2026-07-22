package org.firstinspires.ftc.teamcode.examples;

import org.firstinspires.ftc.teamcode.commands.ServoActionCommand;

import java.util.function.DoubleSupplier;

/**
 * Moves the example servo to a supplied position over a duration.
 */
public class ExampleServoAction extends ServoActionCommand {
    private ExampleSubsystem exampleSubsystem;

    /**
     * Creates an action that moves the example servo.
     *
     * @param exampleSubsystem the subsystem that controls the servo
     * @param expectedPosSupplier supplies the target servo position
     * @param duration the motion duration in milliseconds
     */
    public ExampleServoAction(ExampleSubsystem exampleSubsystem, DoubleSupplier expectedPosSupplier, long duration) {
        super(expectedPosSupplier, duration);
        this.exampleSubsystem = exampleSubsystem;
    }

    @Override
    protected double getPosition() {
       return exampleSubsystem.getServoPosition();
    }

    @Override
    protected void setPosition(double position) {
        exampleSubsystem.setServoPosition(position);
    }
}
