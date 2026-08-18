package org.firstinspires.ftc.teamcode.commands.dropper;

import org.firstinspires.ftc.teamcode.commands.ServoActionCommand;
import org.firstinspires.ftc.teamcode.subsystems.DropperSubsystem;

import java.util.function.DoubleSupplier;

public class DropperPitchAction extends ServoActionCommand {
    private final DropperSubsystem dropperSubsystem;
    public DropperPitchAction(DropperSubsystem dropperSubsystem, DoubleSupplier expectedPosSupplier, long duration) {
        super(expectedPosSupplier, duration);
        this.dropperSubsystem = dropperSubsystem;
    }

    @Override
    protected double getPosition() {
        return dropperSubsystem.getDropperPitchPosition();
    }

    @Override
    protected void setPosition(double position) {
        dropperSubsystem.setDropperPosition(position);
    }
}
