package org.firstinspires.ftc.teamcode.gamepad;

import org.firstinspires.ftc.teamcode.commands.Command;
import org.firstinspires.ftc.teamcode.commands.CommandScheduler;
import org.firstinspires.ftc.teamcode.commands.InstantCommand;
import org.firstinspires.ftc.teamcode.commands.WaitCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BooleanSupplier;

public class Trigger {
    private HashMap<Command, TriggerTypes> commands;
    private HashMap<Command, Command> toggleCommands;
    private final BooleanSupplier condition;
    private boolean wasActive;
    private boolean initialCommandRun;

    public Trigger(BooleanSupplier condition) {
        this.condition = condition;
        wasActive = condition.getAsBoolean();
        CommandScheduler.getInstance().registerTrigger(this);
        commands = new HashMap<>();
        toggleCommands = new HashMap<>();
        initialCommandRun = false;
    }

    public BooleanSupplier getCondition() {
        return condition;
    }


    public void whenActive(Command command) {
       commands.put(command, TriggerTypes.WHEN_ACTIVE);
    }

    public void whenActive(Runnable runnable) {
       commands.put(new InstantCommand(runnable), TriggerTypes.WHEN_ACTIVE);
    }

    public void whileHeld(Command command) {
         commands.put(command, TriggerTypes.WHILE_HELD);
    }

    public void toggleWhenActive(Command command) {
       toggleCommands.put(command, new WaitCommand(0));
    }

    public void toggleWhenActive(Command firstCommand, Command secondCommand) {
        toggleCommands.put(firstCommand, secondCommand);
    }

    public void toggleWhenActive(Runnable runnable) {
        toggleCommands.put(new InstantCommand(runnable), new WaitCommand(0));
    }

    public void toggleWhenActive(Runnable firstRunnable, Runnable secondRunnable) {
        toggleCommands.put(new InstantCommand(firstRunnable), new InstantCommand(secondRunnable));
    }

    public Trigger and(Trigger trigger) {
        return new Trigger(() -> this.condition.getAsBoolean() && trigger.condition.getAsBoolean());
    }

    public Trigger or(Trigger trigger) {
        return new Trigger(() -> this.condition.getAsBoolean() || trigger.condition.getAsBoolean());
    }

    public Trigger negate() {
        return new Trigger(() -> !this.condition.getAsBoolean());
    }

    public void updateWhenActive() {
        boolean isActive = condition.getAsBoolean();
        if (isActive && !wasActive) {
           for (Command command: commands.keySet()) {
               if (commands.get(command) == TriggerTypes.WHEN_ACTIVE) {
                   CommandScheduler.getInstance().schedule(command);
               }
           }
        }
        wasActive = isActive;
    }

    public void updateWhileHeld() {
        boolean isActive = condition.getAsBoolean();
        if (isActive) {
            for (Command command: commands.keySet()) {
                if (commands.get(command) == TriggerTypes.WHILE_HELD) {
                    CommandScheduler.getInstance().schedule(command);
                }
            }
        }
    }

    public void updateToggleWhenActive() {
        boolean isActive = condition.getAsBoolean();
        if (isActive && !wasActive) {
            for (Command command: toggleCommands.keySet()) {
                if (!initialCommandRun) {
                    CommandScheduler.getInstance().schedule(command);
                    initialCommandRun = true;
                } else {
                    CommandScheduler.getInstance().cancel(command);
                    CommandScheduler.getInstance().schedule(toggleCommands.get(command));
                    initialCommandRun = false;
                }
            }
        }
        wasActive = isActive;
    }

    public void update() {
        updateToggleWhenActive();
        updateWhenActive();
        updateWhileHeld();
    }
}
