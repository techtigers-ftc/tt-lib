package org.firstinspires.ftc.teamcode.examples;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.commands.Command;
import org.firstinspires.ftc.teamcode.utils.TTLogger;

/**
 * Runs the example motor at a fixed power for two seconds.
 */
public class ExampleCommand extends Command {

    private ExampleSubsystem exampleSubsystem;
    private ElapsedTime timer;
    private double power;

    /**
     * Creates a command for the example motor.
     *
     * @param exampleSubsystem the subsystem that controls the motor
     * @param power the motor power to apply
     */
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
        TTLogger.dd(tag, "Update Running, isFinished: %b", isFinished());
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
