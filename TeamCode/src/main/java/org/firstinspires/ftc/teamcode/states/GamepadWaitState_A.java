package org.firstinspires.ftc.teamcode.states;

import org.firstinspires.ftc.teamcode.commands.InstantCommand;
import org.firstinspires.ftc.teamcode.commands.WaitCommand;
import org.firstinspires.ftc.teamcode.gamepad.GamepadEx;
import org.firstinspires.ftc.teamcode.gamepad.GamepadKeys;
import org.firstinspires.ftc.teamcode.statemachine.SequentialCommandGroupState;
import org.firstinspires.ftc.teamcode.utils.TTLogger;
import org.firstinspires.ftc.teamcode.utils.enums.AutoStateCondition;

public class GamepadWaitState_A extends SequentialCommandGroupState<AutoStateCondition> {
    private GamepadEx gamepad;

    public GamepadWaitState_A(String name, double timeout, GamepadEx gamepad) {
        super(name, timeout);
        this.gamepad = gamepad;
    }

    @Override
    public AutoStateCondition getCurrentCondition() {
        return gamepad.getGamepadButton(GamepadKeys.Button.A).get() ?
                AutoStateCondition.DRIVE_END : AutoStateCondition.RUNNING;
    }

    @Override
    public void configureCommands() {
        addCommands(
                new InstantCommand(() -> TTLogger.dd(tag, "Ran Commands For GamepadWaitState_A")),
                new WaitCommand(100)
        );
    }
}
