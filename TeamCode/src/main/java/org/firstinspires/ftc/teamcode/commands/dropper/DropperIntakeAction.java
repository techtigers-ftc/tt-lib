package org.firstinspires.ftc.teamcode.commands.dropper;

import org.firstinspires.ftc.teamcode.commands.ParallelCommandGroup;
import org.firstinspires.ftc.teamcode.commands.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.commands.WaitCommand;
import org.firstinspires.ftc.teamcode.subsystems.DropperSubsystem;

public class DropperIntakeAction extends ParallelCommandGroup {
    public DropperIntakeAction(DropperSubsystem dropper) {
        addCommands(
                new SequentialCommandGroup(
                        new WaitCommand(200),
                        new DropperSlidesAbsoluteCommand(dropper, DropperSubsystem.SLIDES_INTAKE_HEIGHT)
                ),
                new DropperPitchAction(dropper, () -> DropperSubsystem.PITCH_INTAKE_POSITION, 700)
        );
    }
}
