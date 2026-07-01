package org.firstinspires.ftc.teamcode.commands;

public abstract class Command {
    public abstract void initialize();
    public  abstract void update();
    public abstract boolean isFinished();
    public abstract void end(boolean interrupted);
}