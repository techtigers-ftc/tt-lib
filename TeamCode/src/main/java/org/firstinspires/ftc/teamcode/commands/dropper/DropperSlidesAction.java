package org.firstinspires.ftc.teamcode.commands.dropper;

import org.firstinspires.ftc.teamcode.commands.CommandBase;
import org.firstinspires.ftc.teamcode.subsystems.DropperSubsystem;

public class DropperSlidesAction extends CommandBase {
    private DropperSubsystem dropper;
    private double targetPosition;
    public DropperSlidesAction(DropperSubsystem dropper, double targetPosition) {
        this.dropper = dropper;
        this.targetPosition = targetPosition;
    }

    @Override
    public void initialize() {
        dropper.moveSlides(targetPosition);
    }

    @Override
    public boolean isFinished() {
        return Math.abs(dropper.getCurrentSlidePositionInches() - targetPosition) < 0.5;
    }
}
