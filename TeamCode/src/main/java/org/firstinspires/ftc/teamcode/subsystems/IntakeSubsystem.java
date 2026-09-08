package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.utils.CachedMotor;
import org.firstinspires.ftc.teamcode.utils.TTLogger;

/**
 * A subsystem to control the Intake functions
 */
public class IntakeSubsystem extends Subsystem {
    private CachedMotor intakeMotor;
    private CachedMotor transferMotor;

    /**
     * Creates a new IntakeSubsystem.
     *
     * @param hardwareMap HardwareMap, used to get hardware references
     */
    public IntakeSubsystem(HardwareMap hardwareMap) {
        super("Intake Subsystem");
        intakeMotor = new CachedMotor(hardwareMap, "intake_motor");
        transferMotor = new CachedMotor(hardwareMap, "transfer_motor");
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        transferMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    /**
     * A method to set the intake power
     * @param power the power for the intake to run at
     */
    public void setIntakePower(double power) {
        intakeMotor.setPower(power);
        transferMotor.setPower(power);
    }

    /**
     * A method to stop the intake
     */
    public void stop() {
        intakeMotor.setPower(0);
        transferMotor.setPower(0);
    }

    @Override
    public void periodic() {
        robotState.setIntakeCurrent(intakeMotor.getCurrent() + transferMotor.getCurrent());
        TTLogger.dd(tag, "Intake Power: %f", intakeMotor.getPower());
    }
}
