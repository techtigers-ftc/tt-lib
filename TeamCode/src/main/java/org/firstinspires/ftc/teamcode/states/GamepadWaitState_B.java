package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.commands.WaitCommand;
import org.firstinspires.ftc.teamcode.gamepad.GamepadEx;
import org.firstinspires.ftc.teamcode.gamepad.GamepadKeys;
import org.firstinspires.ftc.teamcode.statemachine.SequentialCommandGroupState;
import org.firstinspires.ftc.teamcode.utils.enums.AutoStateCondition;

public class GamepadWaitState_B extends SequentialCommandGroupState<AutoStateCondition> {
    private GamepadEx gamepad;

    public GamepadWaitState_B(String name, double timeout, GamepadEx gamepad) {
        super(name, timeout);
        this.gamepad = gamepad;
    }

    @Override
    public AutoStateCondition getCurrentCondition() {
        return gamepad.getGamepadButton(GamepadKeys.Button.B).get() ?
                AutoStateCondition.DRIVE_END : AutoStateCondition.RUNNING;
    }

    @Override
    public void configureCommands() {
        addCommands(
                new WaitCommand(100)
        );
    }
}
