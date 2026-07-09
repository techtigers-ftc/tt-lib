package org.firstinspires.ftc.teamcode.examples;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.utils.CachedMotor;

/**
 * ExampleSubsystem is a simple implementation of the Subsystem class that demonstrates how to use TTLib.
 * It initializes the motor and servo, sets their directions, and demonstrates the subsystem architecture.
 */
public class ExampleSubsystem extends Subsystem {
    private CachedMotor exampleMotor;
    private Servo exampleServo;

    /**
     * Constructor for ExampleSubsystem.
     *
     * @param hardwareMap The hardware map used to initialize the motor and servo
     */
    public ExampleSubsystem(HardwareMap hardwareMap) {
        super("ExampleSubsystem");
        exampleMotor = new CachedMotor(hardwareMap, "example_motor");
        exampleServo = hardwareMap.get(Servo.class, "example_servo");

        exampleMotor.setDirection(DcMotor.Direction.FORWARD);
        exampleMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        exampleServo.setDirection(Servo.Direction.FORWARD);
    }

    @Override
    public void initLoop() {

    }

    @Override
    public void justAfterStart() {
        if (exampleServo.getPosition() == 1) {
            exampleServo.setPosition(-1);
        } else {
            exampleServo.setPosition(1);
        }
    }

    @Override
    public void periodic() {
        telemetry.addData("Example Motor Power", exampleMotor.getPower());
    }

    @Override
    public void close() {
        exampleMotor.setPower(0);
    }

    public void setMotorPower(double power) {
        exampleMotor.setPower(power);
    }

    public void setServoPosition(double position) {
        exampleServo.setPosition(position);
    }

    public double getServoPosition() {
        return exampleServo.getPosition();
    }
}
