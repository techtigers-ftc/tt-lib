package org.firstinspires.ftc.teamcode.examples;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.commands.Command;

public class ExampleCommand extends Command {

    private ExampleSubsystem exampleSubsystem;
    private ElapsedTime timer;

    public ExampleCommand (ExampleSubsystem exampleSubsystem) {
        this.exampleSubsystem = exampleSubsystem;

        timer = new ElapsedTime();
    }

    @Override
    public void initialize() {
        timer.reset();
    }

    @Override
    public void update() {
        exampleSubsystem.setMotorPower(1.0);
    }

    @Override
    public boolean isFinished() {
        return timer.seconds() > 2;
    }

    @Override
    public void end(boolean interrupted) {
        exampleSubsystem.setMotorPower(0.0);
    }
}
