package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.utils.CachedMotor;

/**
 * A subsystem to control the Intake functions
 */
public class IntakeSubsystem extends Subsystem {
    private CachedMotor intakeMotor;

    /**
     * Creates a new IntakeSubsystem.
     *
     * @param hardwareMap HardwareMap, used to get hardware references
     */
    public IntakeSubsystem(HardwareMap hardwareMap) {
        super("Intake Subsystem");
        intakeMotor = new CachedMotor(hardwareMap, "intake_motor");
    }

    /**
     * A method to set the intake power
     * @param power the power for the intake to run at
     */
    public void setIntakePower(double power) {
        intakeMotor.setPower(power);
    }

    @Override
    public void periodic() {
        robotState.setIntakeCurrent(intakeMotor.getCurrent());
    }
}
