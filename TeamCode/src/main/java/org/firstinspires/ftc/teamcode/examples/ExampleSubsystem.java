package org.firstinspires.ftc.teamcode.examples;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.subsystems.Subsystem;

public class ExampleSubsystem extends Subsystem {
    private DcMotor exampleMotor;
    private Servo exampleServo;

    public ExampleSubsystem() {
        hardwareMap.get(DcMotor.class, "example_motor");
        hardwareMap.get(Servo.class, "example_servo");
    }
    @Override
    public void initLoop() {

    }

    @Override
    public void justAfterStart() {
        exampleServo.setPosition(0.5);
    }

    @Override
    public void periodic() {
        exampleMotor.setPower(0.5);
    }

    @Override
    public void close() {
        exampleMotor.setPower(0);
    }
}
