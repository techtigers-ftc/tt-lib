package org.firstinspires.ftc.teamcode.examples;

import org.firstinspires.ftc.teamcode.commands.ParallelCommandGroup;

public class ExampleParallelCommandGroup extends ParallelCommandGroup {

    public ExampleParallelCommandGroup(ExampleSubsystem exampleSubsystem) {
        addCommands(
                new ExampleServoAction(exampleSubsystem, () -> 0.5, 200),
                new ExampleCommand(exampleSubsystem, -1)
        );
    }
}
