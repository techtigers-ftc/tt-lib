package org.firstinspires.ftc.teamcode.commands.intake;

import org.firstinspires.ftc.teamcode.commands.ParallelCommandGroup;
import org.firstinspires.ftc.teamcode.commands.dropper.DropperSpinServoCommand;
import org.firstinspires.ftc.teamcode.gamepad.GamepadEx;
import org.firstinspires.ftc.teamcode.subsystems.DropperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;

public class IntakeAction extends ParallelCommandGroup {
    public IntakeAction(GamepadEx gamepad, IntakeSubsystem intake, DropperSubsystem dropper) {
        addCommands(
                new ManualIntakeCommand(gamepad, intake),
                new DropperSpinServoCommand(dropper, 1)
        );
    }
}
