package org.firstinspires.ftc.teamcode.examples;

import org.firstinspires.ftc.teamcode.commands.ParallelCommandGroup;

/**
 * Demonstrates running a servo action and motor command in parallel.
 */
public class ExampleParallelCommandGroup extends ParallelCommandGroup {

    /**
     * Creates the example parallel command group.
     *
     * @param exampleSubsystem the subsystem used by the child commands
     */
    public ExampleParallelCommandGroup(ExampleSubsystem exampleSubsystem) {
        addCommands(
                new ExampleServoAction(exampleSubsystem, () -> 0.5, 200),
                new ExampleCommand(exampleSubsystem, -1)
        );
    }
}
