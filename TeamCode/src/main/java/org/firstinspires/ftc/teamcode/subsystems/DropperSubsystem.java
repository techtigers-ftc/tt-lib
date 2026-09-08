package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.control.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.utils.CachedCRServo;
import org.firstinspires.ftc.teamcode.utils.CachedMotor;
import org.firstinspires.ftc.teamcode.utils.SlideController;

public class DropperSubsystem extends Subsystem {
    public static final double PITCH_INTAKE_POSITION = 0.135;
    public static final double PITCH_DROP_POSITION = 0.68;
    public static final double SLIDES_DROP_HEIGHT = 12.5;
    public static final double SLIDES_INTAKE_HEIGHT = 1.0;
    private CachedMotor leftSlideMotor;
    private CachedMotor righSlideMotor;
    private Servo leftPitchServo;
    private Servo rightPitchServo;
    private CachedCRServo rightDropperServo;
    private CachedCRServo leftDropperServo;
    private static double DROPPER_P = 0.009;
    private static double DROPPER_D = 0.0;
    private final SlideController slideController;
    private static final double TICKS_PER_INCH = 145.1/ (112.0 / 25.4);
    private final CachedMotor encoderMotor;
    private static double SLIDES_MAX_INCHES = 13.0;

    public DropperSubsystem(HardwareMap hardwareMap) {
        super("Dropper Subsystem");

        leftSlideMotor = new CachedMotor(hardwareMap, "left_slide");
        righSlideMotor = new CachedMotor(hardwareMap, "right_slide");
        leftPitchServo = hardwareMap.get(Servo.class, "left_pitch");
        rightPitchServo = hardwareMap.get(Servo.class, "right_pitch");
        leftDropperServo = new CachedCRServo(hardwareMap, "left_dropper");
        rightDropperServo = new CachedCRServo(hardwareMap, "right_dropper");

        leftDropperServo.setDirection(DcMotorSimple.Direction.FORWARD);
        rightDropperServo.setDirection(DcMotorSimple.Direction.REVERSE);

        leftPitchServo.setDirection(Servo.Direction.FORWARD);
        rightPitchServo.setDirection(Servo.Direction.REVERSE);

        leftSlideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        righSlideMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftSlideMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        righSlideMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        encoderMotor = leftSlideMotor;

        slideController = new SlideController(TICKS_PER_INCH, new PIDFCoefficients(DROPPER_P, 0, DROPPER_D, 0));
    }

    @Override
    public void justAfterStart() {
        setDropperPosition(PITCH_INTAKE_POSITION);
        setDropperServoPowers(0);
    }

    public void setDropperServoPowers(double power) {
        leftDropperServo.setPower(power);
        rightDropperServo.setPower(power);
    }

    public void stopDropperServos() {
        leftDropperServo.stop();
        rightDropperServo.stop();
    }

    public void setDropperPosition(double position) {
        rightPitchServo.setPosition(position);
        leftPitchServo.setPosition(position);
    }

    public double getCurrentSlidePositionInches() {
        return getCurrentSlidePositionTicks() / TICKS_PER_INCH;
    }

    public double getSlideTargetPositionInches() {
        return slideController.getTargetTicks() / TICKS_PER_INCH;
    }

    /**
     * Moves the slides to a position
     *
     * @param targetPosition the target position in inches
     */
    public void moveSlidesAbsolute(double targetPosition) {
        slideController.moveToInches(Range.clip(targetPosition, 0, SLIDES_MAX_INCHES));
    }

    public void moveSlidesRelative(double delta) {
        moveSlidesAbsolute(getCurrentSlidePositionInches() + delta);
    }

    public double getDropperPitchPosition() {
        return rightPitchServo.getPosition();
    }

    private double getCurrentSlidePositionTicks() {
        return encoderMotor.getPosition();
    }

    @Override
    public void periodic() {
        double power = slideController.calculateMotorPowers(getCurrentSlidePositionTicks());
        telemetry.addData("Slide Power", power);
        telemetry.addData("Slide Target Position (inches)", getSlideTargetPositionInches());
        righSlideMotor.setPower(power);
        leftSlideMotor.setPower(power);
    }
}
