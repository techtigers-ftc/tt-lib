package org.firstinspires.ftc.teamcode.commands.dropper;

import org.firstinspires.ftc.teamcode.commands.ParallelCommandGroup;
import org.firstinspires.ftc.teamcode.subsystems.DropperSubsystem;

public class DropperOuttakeAction extends ParallelCommandGroup {
    public DropperOuttakeAction(DropperSubsystem dropper) {
        addCommands(
                new DropperSlidesAction(dropper, 10),
                new DropperPitchAction(dropper, () -> 1, 400),
                new DropperSpinServoAction(dropper, -1)
        );
    }
}
