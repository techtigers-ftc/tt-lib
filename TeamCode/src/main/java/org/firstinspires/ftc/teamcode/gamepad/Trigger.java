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
     * Schedules a command while the trigger is held.
     * If the command finishes while held, it will be scheduled again on the next loop.
     */
    public Trigger whileHeld(final Command command) {
        bindings.add(() -> CommandScheduler.getInstance().schedule(command));
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

    /**
     * Runs an action on press and cancels it on the next press.
     *
     * @param runnable The action to run.
     * @return This trigger, for chaining.
     */
    public Trigger toggleWhenActive(Runnable runnable) {
        return toggleWhenActive(new InstantCommand(runnable));
    }

    /**
     * Alternates between two actions on each press, interrupting the previously selected action.
     *
     * @param firstRunnable The first action to run.
     * @param secondRunnable The second action to run.
     * @return This trigger, for chaining.
     */
    public Trigger toggleWhenActive(Runnable firstRunnable, Runnable secondRunnable) {
        return toggleWhenActive(
                new InstantCommand(firstRunnable),
                new InstantCommand(secondRunnable)
        );
    }

    /**
     * Combines this trigger with another trigger using logical AND.
     *
     * @param trigger The other trigger to combine with.
     * @return A new trigger that is active when both triggers are active.
     */
    public Trigger and(Trigger trigger) {
        return new Trigger(() -> get() && trigger.get());
    }

    /**
     * Combines this trigger with another trigger using logical OR.
     *
     * @param trigger The other trigger to combine with.
     * @return A new trigger that is active when either trigger is active.
     */
    public Trigger or(Trigger trigger) {
        return new Trigger(() -> get() || trigger.get());
    }

    /**
     * Negates this trigger, making it active when it is inactive and vice versa.
     *
     * @return A new trigger that is active when this trigger is inactive.
     */
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
