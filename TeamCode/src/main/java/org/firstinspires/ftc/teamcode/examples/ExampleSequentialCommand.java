package org.firstinspires.ftc.teamcode.examples;

import org.firstinspires.ftc.teamcode.commands.SequentialCommandGroup;

/**
 * Demonstrates running motor and servo commands in sequence.
 */
public class ExampleSequentialCommand extends SequentialCommandGroup {

    ExampleSubsystem exampleSubsystem;

    /**
     * Creates the example sequential command group.
     *
     * @param exampleSubsystem the subsystem used by the child commands
     */
    public ExampleSequentialCommand(ExampleSubsystem exampleSubsystem) {
        this.exampleSubsystem = exampleSubsystem;

        addCommands(
            new ExampleCommand(exampleSubsystem, 1.0),
            new ExampleServoAction(exampleSubsystem, () -> 0.5, 600),
            new ExampleCommand(exampleSubsystem, -1.0),
            new ExampleServoAction(exampleSubsystem, () -> 0.0, 300)
        );
    }
}
