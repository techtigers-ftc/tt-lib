package org.firstinspires.ftc.teamcode.examples;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.utils.TTLogger;

public class ExampleSubsystem extends Subsystem {
    private DcMotorEx exampleMotor;
    private Servo exampleServo;

    public ExampleSubsystem(HardwareMap hardwareMap) {
        super("ExampleSubsystem");
        exampleMotor = hardwareMap.get(DcMotorEx.class, "example_motor");
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
        if (exampleServo.getPosition() == 1){
            exampleServo.setPosition(-1);
        } else {
            exampleServo.setPosition(1);
        }
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
