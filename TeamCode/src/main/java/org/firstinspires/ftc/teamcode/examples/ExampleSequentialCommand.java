package org.firstinspires.ftc.teamcode.examples;

import org.firstinspires.ftc.teamcode.commands.SequentialCommandGroup;

public class ExampleSequentialCommand extends SequentialCommandGroup {

    ExampleSubsystem exampleSubsystem;

    public ExampleSequentialCommand(ExampleSubsystem exampleSubsystem) {
        this.exampleSubsystem = exampleSubsystem;

        addCommands(
            new ExampleCommand(exampleSubsystem, 1.0),
            new ExampleServoAction(exampleSubsystem, () -> 0.5, 1000),
            new ExampleCommand(exampleSubsystem, -1.0),
            new ExampleServoAction(exampleSubsystem, () -> 0.0, 300)
        );
    }
}
