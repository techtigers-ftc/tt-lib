package team.techtigers.commands;

import java.util.function.BooleanSupplier;

/**
 * A command that waits until a condition is met
 */
public class WaitUntilCommand extends CommandBase {
    private BooleanSupplier condition;

    /**
     * Constructor for the WaitUntilCommand
     *
     * @param condition The condition to wait for
     */
    public WaitUntilCommand(BooleanSupplier condition) {
        this.condition = condition;
    }

    @Override
    public boolean isFinished() {
        return condition.getAsBoolean();
    }
}
