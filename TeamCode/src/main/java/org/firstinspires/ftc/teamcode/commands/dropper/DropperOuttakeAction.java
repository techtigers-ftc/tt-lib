package org.firstinspires.ftc.teamcode.commands.dropper;

import org.firstinspires.ftc.teamcode.commands.ParallelCommandGroup;
import org.firstinspires.ftc.teamcode.commands.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.DropperSubsystem;

public class DropperOuttakeAction extends ParallelCommandGroup {
    public DropperOuttakeAction(DropperSubsystem dropper) {
        addCommands(
                new DropperSlidesAbsoluteCommand(dropper, DropperSubsystem.SLIDES_DROP_HEIGHT),
                new DropperPitchAction(dropper, () -> DropperSubsystem.PITCH_DROP_POSITION, 700)
        );
    }
}
