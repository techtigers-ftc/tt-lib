package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.subsystems.IntakeSubsystem;

@TeleOp(name = "Intake Test")
public class IntakeTestOpMode extends BaseOpMode {
    private IntakeSubsystem intake;
    @Override
    protected void initialize() {
        intake = new IntakeSubsystem(hardwareMap);
        registerSubsystems(intake);
    }

    @Override
    protected void update() {
        double power = gamepad1.right_trigger - gamepad1.left_trigger;
        intake.setIntakePower(power);

        telemetry.addData("Intake Power: ", power);
        telemetry.addData("Intake current: ", robotState.getIntakeCurrent());
    }

    @Override
    protected boolean isBlue() {
        return false;
    }
}
