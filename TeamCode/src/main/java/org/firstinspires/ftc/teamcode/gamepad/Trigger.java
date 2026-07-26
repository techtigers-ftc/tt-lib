package org.firstinspires.ftc.teamcode.gamepad;

import org.firstinspires.ftc.teamcode.commands.Command;
import org.firstinspires.ftc.teamcode.commands.CommandScheduler;
import org.firstinspires.ftc.teamcode.commands.InstantCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * Binds commands to a boolean condition, such as a gamepad button or a sensor state.
 */
public class Trigger {
    private final BooleanSupplier condition;
    private List<Runnable> bindings;

    public Trigger(BooleanSupplier condition) {
        bindings = new ArrayList<>();
        this.condition = condition;
        CommandScheduler.getInstance().registerTrigger(this);
    }

    public BooleanSupplier getCondition() {
        return condition;
    }

    /**
     * Returns whether this trigger is currently active.
     */
    public boolean get() {
        return condition.getAsBoolean();
    }

    /**
     * Schedules a command when the trigger changes from inactive to active.
     */
    public Trigger whenActive(final Command command) {
        bindings.add(new Runnable() {
            private boolean wasActive = get();

            @Override
            public void run() {
                boolean isActive = get();
                if (isActive && !wasActive) {
                    CommandScheduler.getInstance().schedule(command);
                }
                wasActive = isActive;
            }
        });
        return this;
    }

    /**
     * Runs an action once when the trigger changes from inactive to active.
     */
    public Trigger whenActive(Runnable runnable) {
        return whenActive(new InstantCommand(runnable));
    }

    /**
     * Schedules a command while the trigger is held and cancels it when released.
     * If the command finishes while held, it will be scheduled again on the next loop.
     */
    public Trigger whileHeld(final Command command) {
        bindings.add(new Runnable() {
            private boolean wasActive = get();

            @Override
            public void run() {
                boolean isActive = get();
                if (isActive) {
                    CommandScheduler.getInstance().schedule(command);
                } else if (wasActive) {
                    CommandScheduler.getInstance().cancel(command);
                }
                wasActive = isActive;
            }
        });
        return this;
    }

    /**
     * Runs an action continuously while the trigger is held.
     */
    public Trigger whileHeld(Runnable runnable) {
        return whileHeld(new InstantCommand(runnable));
    }

    /**
     * Starts a command on press and cancels it on the next press.
     */
    public Trigger toggleWhenActive(final Command command) {
        bindings.add(new Runnable() {
            private boolean wasActive = get();

            @Override
            public void run() {
                boolean isActive = get();
                if (isActive && !wasActive) {
                    if (CommandScheduler.getInstance().isScheduled(command)) {
                        CommandScheduler.getInstance().cancel(command);
                    } else {
                        CommandScheduler.getInstance().schedule(command);
                    }
                }
                wasActive = isActive;
            }
        });
        return this;
    }

    /**
     * Alternates between two commands on each press, interrupting the previously selected command.
     */
    public Trigger toggleWhenActive(final Command firstCommand, final Command secondCommand) {
        bindings.add(new Runnable() {
            private boolean wasActive = get();
            private boolean firstCommandActive;

            @Override
            public void run() {
                boolean isActive = get();
                if (isActive && !wasActive) {
                    if (firstCommandActive) {
                        CommandScheduler.getInstance().cancel(firstCommand);
                        CommandScheduler.getInstance().schedule(secondCommand);
                    } else {
                        CommandScheduler.getInstance().cancel(secondCommand);
                        CommandScheduler.getInstance().schedule(firstCommand);
                    }
                    firstCommandActive = !firstCommandActive;
                }
                wasActive = isActive;
            }
        });
        return this;
    }

    public Trigger toggleWhenActive(Runnable runnable) {
        return toggleWhenActive(new InstantCommand(runnable));
    }

    public Trigger toggleWhenActive(Runnable firstRunnable, Runnable secondRunnable) {
        return toggleWhenActive(
                new InstantCommand(firstRunnable),
                new InstantCommand(secondRunnable)
        );
    }

    public Trigger and(Trigger trigger) {
        return new Trigger(() -> get() && trigger.get());
    }

    public Trigger or(Trigger trigger) {
        return new Trigger(() -> get() || trigger.get());
    }

    public Trigger negate() {
        return new Trigger(() -> !get());
    }

    /**
     * Evaluates all bindings.
     */
    public void update() {
        for (Runnable binding : new ArrayList<>(bindings)) {
            binding.run();
        }
    }
}
