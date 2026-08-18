package org.firstinspires.ftc.teamcode.commands.dropper;

import org.firstinspires.ftc.teamcode.commands.CommandBase;
import org.firstinspires.ftc.teamcode.subsystems.DropperSubsystem;

/**
 * A command to spin the dropper servos at a set power
 */
public class DropperSpinServoAction extends CommandBase {
    private DropperSubsystem dropper;
    private double power;

    /**
     * Initializes the command
     *
     * @param dropper The dropper subsystem
     * @param power The power for the dropper servos to run at
     */
    public DropperSpinServoAction(DropperSubsystem dropper, double power) {
        this.dropper = dropper;
        this.power = power;
    }

    @Override
    public void initialize() {
        dropper.setDropperServoPowers(power);
    }

    @Override
    public void end(boolean interrupted) {
        dropper.stopDropperServos();
    }
}
