package org.firstinspires.ftc.teamcode.examples;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.commands.Command;
import org.firstinspires.ftc.teamcode.utils.TTLogger;

public class ExampleCommand extends Command {

    private ExampleSubsystem exampleSubsystem;
    private ElapsedTime timer;
    private double power;

    public ExampleCommand (ExampleSubsystem exampleSubsystem, double power) {
        this.exampleSubsystem = exampleSubsystem;
        this.power = power;

        timer = new ElapsedTime();
    }

    @Override
    public void initialize() {
        timer.reset();
    }

    @Override
    public void update() {
        exampleSubsystem.setMotorPower(power);
    }

    @Override
    public boolean isFinished() {
        return timer.seconds() >= 2;
    }

    @Override
    public void end(boolean interrupted) {
        TTLogger.dd(tag, "End Running");
        exampleSubsystem.setMotorPower(0.0);
    }
}
