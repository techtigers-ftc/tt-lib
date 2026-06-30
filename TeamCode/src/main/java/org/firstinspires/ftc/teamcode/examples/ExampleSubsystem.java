package org.firstinspires.ftc.teamcode.examples;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.utils.TTLogger;

public class ExampleSubsystem extends Subsystem {
    private DcMotor exampleMotor;
    private Servo exampleServo;

    public ExampleSubsystem() {
        super("ExampleSubsystem");
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
        telemetry.addData("Example Motor Power", exampleMotor.getPower());
        TTLogger.dd(tag, "Example Motor Power: %.2f", exampleMotor.getPower());
    }

    @Override
    public void close() {
        exampleMotor.setPower(0);
    }
}
