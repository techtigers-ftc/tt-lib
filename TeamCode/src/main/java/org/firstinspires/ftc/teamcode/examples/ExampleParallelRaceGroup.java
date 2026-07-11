package org.firstinspires.ftc.teamcode.examples;

import org.firstinspires.ftc.teamcode.commands.ParallelRaceGroup;

public class ExampleParallelRaceGroup extends ParallelRaceGroup {

    public ExampleParallelRaceGroup(ExampleSubsystem exampleSubsystem) {
        addCommands(
                new ExampleServoAction(exampleSubsystem, () -> -1.0, 5000),
                new ExampleCommand(exampleSubsystem, 0.25)
        );
    }
}
