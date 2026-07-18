package org.firstinspires.ftc.teamcode.gamepad;

import org.firstinspires.ftc.teamcode.commands.Command;
import org.firstinspires.ftc.teamcode.commands.CommandScheduler;

import java.util.ArrayList;
import java.util.function.BooleanSupplier;

public abstract class Trigger {
    private ArrayList<Command> commands;
    private BooleanSupplier condition;

    public Trigger(BooleanSupplier condition) {
        this.condition = condition;

    }

    public BooleanSupplier getCondition() {
        return condition;
    }

    public void whenActive(Command command) {
        commands.add(command);
        CommandScheduler.getInstance().schedule(command);
    }

    public void update() {

    }
}
