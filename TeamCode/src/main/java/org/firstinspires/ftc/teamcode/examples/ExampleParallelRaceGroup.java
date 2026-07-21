package org.firstinspires.ftc.teamcode.examples;

import org.firstinspires.ftc.teamcode.commands.ParallelRaceGroup;

/**
 * Demonstrates a parallel group that ends when its first child finishes.
 */
public class ExampleParallelRaceGroup extends ParallelRaceGroup {

    /**
     * Creates the example race command group.
     *
     * @param exampleSubsystem the subsystem used by the child commands
     */
    public ExampleParallelRaceGroup(ExampleSubsystem exampleSubsystem) {
        addCommands(
                new ExampleServoAction(exampleSubsystem, () -> -1.0, 1000),
                new ExampleCommand(exampleSubsystem, 0.25)
        );
    }
}
