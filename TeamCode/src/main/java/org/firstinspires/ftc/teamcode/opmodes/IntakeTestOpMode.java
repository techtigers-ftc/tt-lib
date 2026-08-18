package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.gamepad.GamepadKeys;
import org.firstinspires.ftc.teamcode.gamepad.Trigger;
import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;
import org.firstinspires.ftc.teamcode.utils.TTLogger;

@TeleOp(name = "Intake Test")
public class IntakeTestOpMode extends BaseOpMode {
    private IntakeSubsystem intake;

    @Override
    protected void initialize() {
        isDebounceOn = true;
        intake = new IntakeSubsystem(hardwareMap);
        registerSubsystems(intake);

        Trigger intakeTrigger = new Trigger(() -> driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) != 0
                || driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER) != 0);

        intakeTrigger.whileHeld(() -> intake.setIntakePower(driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) - driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER)));

        TTLogger.setLoggingLevel(TTLogger.DEBUG);
    }

    @Override
    protected void update() {
        telemetry.addData("Intake Power: ", gamepad1.right_trigger - gamepad1.left_trigger);
        telemetry.addData("Intake Current: ", robotState.getIntakeCurrent());
    }

    @Override
    protected boolean isBlue() {
        return false;
    }
}
