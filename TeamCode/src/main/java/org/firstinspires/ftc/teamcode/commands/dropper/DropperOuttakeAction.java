package org.firstinspires.ftc.teamcode.commands.dropper;

import org.firstinspires.ftc.teamcode.commands.ParallelCommandGroup;
import org.firstinspires.ftc.teamcode.commands.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.commands.WaitCommand;
import org.firstinspires.ftc.teamcode.subsystems.DropperSubsystem;

public class DropperOuttakeAction extends ParallelCommandGroup {
    public DropperOuttakeAction(DropperSubsystem dropper) {
        addCommands(
                new DropperSlidesAbsoluteCommand(dropper, DropperSubsystem.SLIDES_DROP_HEIGHT),
                new SequentialCommandGroup(
                        new WaitCommand(600),
                        new DropperPitchAction(dropper, () -> DropperSubsystem.PITCH_DROP_POSITION, 1000)
                )
        );
    }
}
