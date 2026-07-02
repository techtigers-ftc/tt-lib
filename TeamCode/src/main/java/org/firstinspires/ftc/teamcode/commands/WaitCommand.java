package org.firstinspires.ftc.teamcode.commands;

import com.qualcomm.robotcore.util.ElapsedTime;

public class WaitCommand extends Command {

    private double waitTime;
    private ElapsedTime timer;

    public WaitCommand(double waitTime) {
        this.waitTime = waitTime;
        timer = new ElapsedTime();
    }

    @Override
    public void initialize() {
        timer.reset();
    }

    @Override
    public boolean isFinished() {
        return timer.milliseconds() >= waitTime;
    }
}
