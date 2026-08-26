package org.firstinspires.ftc.teamcode.commands.intake;

import org.firstinspires.ftc.teamcode.commands.CommandBase;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;

public class RunIntakeCommand extends CommandBase {
    private IntakeSubsystem intake;
    private double power;

    RunIntakeCommand(IntakeSubsystem intake, double power) {
        this.intake = intake;
        this.power = power;
    }

    @Override
    public void initialize() {
        intake.setIntakePower(power);
    }
}
