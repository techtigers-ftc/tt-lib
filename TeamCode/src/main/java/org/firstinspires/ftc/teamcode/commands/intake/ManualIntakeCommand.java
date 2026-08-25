package org.firstinspires.ftc.teamcode.commands.intake;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.commands.CommandBase;
import org.firstinspires.ftc.teamcode.gamepad.GamepadEx;
import org.firstinspires.ftc.teamcode.gamepad.GamepadKeys;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;

public class ManualIntakeCommand extends CommandBase {
    private GamepadEx gamepad;
    private IntakeSubsystem intake;
    public ManualIntakeCommand(GamepadEx gamepad, IntakeSubsystem intake) {
        this.gamepad = gamepad;
        this.intake = intake;
    }

    @Override
    public void update() {
        intake.setIntakePower(gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) -
                gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER));
    }

    @Override
    public boolean isFinished() {
        return gamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) == 0
                || gamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) == 0;
    }
}
