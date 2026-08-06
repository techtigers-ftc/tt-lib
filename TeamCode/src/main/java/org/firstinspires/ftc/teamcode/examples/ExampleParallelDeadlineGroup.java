package org.firstinspires.ftc.teamcode.examples;

import org.firstinspires.ftc.teamcode.commands.ParallelDeadlineGroup;

/**
 * Demonstrates a parallel group that ends when its servo action finishes.
 */
public class ExampleParallelDeadlineGroup extends ParallelDeadlineGroup {
    private ExampleSubsystem exampleSubsystem;

    /**
     * Creates the example deadline command group.
     *
     * @param exampleSubsystem the subsystem used by the child commands
     */
    public ExampleParallelDeadlineGroup(ExampleSubsystem exampleSubsystem) {
        super(
                new ExampleServoAction(exampleSubsystem, () -> 0.75, 300),
                new ExampleCommand(exampleSubsystem, -0.75)
        );
        this.exampleSubsystem = exampleSubsystem;

    }
}
