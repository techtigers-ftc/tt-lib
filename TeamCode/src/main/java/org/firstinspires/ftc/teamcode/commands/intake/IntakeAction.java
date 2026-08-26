package org.firstinspires.ftc.teamcode.commands.intake;

import org.firstinspires.ftc.teamcode.commands.ParallelCommandGroup;
import org.firstinspires.ftc.teamcode.commands.ParallelDeadlineGroup;
import org.firstinspires.ftc.teamcode.commands.dropper.DropperSpinServoCommand;
import org.firstinspires.ftc.teamcode.gamepad.GamepadEx;
import org.firstinspires.ftc.teamcode.subsystems.DropperSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;

public class IntakeAction extends ParallelCommandGroup {
    private DropperSubsystem dropper;
    private IntakeSubsystem intake;

    public IntakeAction(IntakeSubsystem intake, DropperSubsystem dropper) {
        super(
                new RunIntakeCommand(intake, 0.7),
                new DropperSpinServoCommand(dropper, 1)
        );

        this.intake = intake;
        this.dropper = dropper;
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        intake.stop();
        dropper.setDropperServoPowers(0);
    }
}
