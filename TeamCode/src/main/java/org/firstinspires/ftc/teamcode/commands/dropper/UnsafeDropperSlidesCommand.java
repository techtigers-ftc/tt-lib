package org.firstinspires.ftc.teamcode.commands.dropper;

import org.firstinspires.ftc.teamcode.commands.CommandBase;
import org.firstinspires.ftc.teamcode.gamepad.GamepadEx;
import org.firstinspires.ftc.teamcode.subsystems.DropperSubsystem;

/**
 * A command that allows the driver to control the intake slides manually
 * with no limits
 */
public class UnsafeDropperSlidesCommand extends CommandBase {
    private GamepadEx gamepad;
    private DropperSubsystem dropper;

    /**
     * Constructs a new UnsafeDropperSlidesCommand
     * @param dropper the dropper subsystem
     * @param gamepad the gamepad to control the slides
     */
    public UnsafeDropperSlidesCommand(DropperSubsystem dropper,
                                      GamepadEx gamepad) {
        this.gamepad = gamepad;
        this.dropper = dropper;
    }

    @Override
    public void update() {
        dropper.moveSlidesRelative(gamepad.getLeftY()*4);
    }
}
