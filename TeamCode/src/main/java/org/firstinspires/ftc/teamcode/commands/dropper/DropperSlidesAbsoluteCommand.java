package org.firstinspires.ftc.teamcode.commands.dropper;

import org.firstinspires.ftc.teamcode.commands.CommandBase;
import org.firstinspires.ftc.teamcode.subsystems.DropperSubsystem;

import java.util.function.DoubleSupplier;

public class DropperSlidesAbsoluteCommand extends CommandBase {
    private DropperSubsystem dropper;
    private DoubleSupplier targetPosition;
    private double tolerance;

    public DropperSlidesAbsoluteCommand(DropperSubsystem dropper, DoubleSupplier targetPosition, double tolerance) {
        this.dropper = dropper;
        this.targetPosition = targetPosition;
        this.tolerance = tolerance;
    }

    public DropperSlidesAbsoluteCommand(DropperSubsystem dropper, double targetPosition, double tolerance) {
        this(dropper, () -> targetPosition, tolerance);
    }

    public DropperSlidesAbsoluteCommand(DropperSubsystem dropper, DoubleSupplier targetPosition) {
        this(dropper, targetPosition, 1);
    }

    public DropperSlidesAbsoluteCommand(DropperSubsystem dropper, double targetPosition) {
        this(dropper, () -> targetPosition);
    }

    @Override
    public void initialize() {
        dropper.moveSlidesAbsolute(targetPosition.getAsDouble());
    }

    @Override
    public boolean isFinished() {
        return Math.abs(dropper.getCurrentSlidePositionInches() - dropper.getSlideTargetPositionInches()) < tolerance;
    }
}
