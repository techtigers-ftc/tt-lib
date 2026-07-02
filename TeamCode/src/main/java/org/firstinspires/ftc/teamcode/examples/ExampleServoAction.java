package org.firstinspires.ftc.teamcode.examples;

import org.firstinspires.ftc.teamcode.commands.ServoActionCommand;

import java.util.function.DoubleSupplier;

public class ExampleServoAction extends ServoActionCommand {
    private ExampleSubsystem exampleSubsystem;
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
