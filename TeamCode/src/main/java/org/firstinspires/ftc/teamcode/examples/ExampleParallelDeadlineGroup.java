package org.firstinspires.ftc.teamcode.examples;

import org.firstinspires.ftc.teamcode.commands.ParallelDeadlineGroup;

public class ExampleParallelDeadlineGroup extends ParallelDeadlineGroup {
    private ExampleSubsystem exampleSubsystem;

    public ExampleParallelDeadlineGroup(ExampleSubsystem exampleSubsystem) {
        super(
                new ExampleServoAction(exampleSubsystem, () -> 0.75, 300),
                new ExampleCommand(exampleSubsystem, -0.75)
        );
        this.exampleSubsystem = exampleSubsystem;

    }
}
