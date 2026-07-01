package org.firstinspires.ftc.teamcode.commands;

public abstract class Command {
    protected void initialize(){

    }
    protected void update(){

    }
    public abstract boolean isFinished();
    protected void end(boolean interrupted){

    }
}