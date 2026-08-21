package org.firstinspires.ftc.teamcode.subsystems;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.utils.CachedMotor;
import org.firstinspires.ftc.teamcode.utils.SlidingAverageCalculator;
import org.firstinspires.ftc.teamcode.utils.Vector2d;

@Configurable
public class DriveSubsystem extends Subsystem {
    public static double MAX_CURRENT_DRAW = 100;
    private double currentMultiplier = 1;
    public final CachedMotor frontLeft, frontRight;
    public final CachedMotor backLeft, backRight;
    private final SlidingAverageCalculator frontLeftSlideCurrentAverage;
    private final SlidingAverageCalculator frontRightSlideCurrentAverage;
    private final SlidingAverageCalculator backLeftSlideCurrentAverage;
    private final SlidingAverageCalculator backRightSlideCurrentAverage;
    private static final double TICKS_PER_REVOLUTION = 28.0;
    private static final double RPM = 392;
    private static final double GEAR_RATIO = RPM / 6000.0;
    private static final double TICKS_PER_WHEEL_REVOLUTION = TICKS_PER_REVOLUTION / GEAR_RATIO;
    private final ElapsedTime timer;

    /**
     * Constructs a new DriveSubsystem.
     *
     * @param hardwareMap The hardware map, used to get hardware references
     */
    public DriveSubsystem(HardwareMap hardwareMap) {
        super("Drive Subsystem");
        frontLeft = new CachedMotor(hardwareMap, "left_front");
        frontRight = new CachedMotor(hardwareMap, "right_front");
        backLeft = new CachedMotor(hardwareMap, "left_back");
        backRight = new CachedMotor(hardwareMap, "right_back");

        frontRightSlideCurrentAverage = new SlidingAverageCalculator(3);
        frontLeftSlideCurrentAverage = new SlidingAverageCalculator(3);
        backRightSlideCurrentAverage = new SlidingAverageCalculator(3);
        backLeftSlideCurrentAverage = new SlidingAverageCalculator(3);

        CachedMotor[] motors = {frontLeft, backLeft, frontRight, backRight};
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);

        for (CachedMotor motor : motors) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        timer = new ElapsedTime();
        timer.reset();
    }

    //From FTC Lib RobotDrive
    private void normalize(double[] wheelSpeeds, double magnitude) {
        double maxMagnitude = Math.abs(wheelSpeeds[0]);
        for (int i = 1; i < wheelSpeeds.length; i++) {
            double temp = Math.abs(wheelSpeeds[i]);
            if (maxMagnitude < temp) {
                maxMagnitude = temp;
            }
        }
        for (int i = 0; i < wheelSpeeds.length; i++) {
            wheelSpeeds[i] = (wheelSpeeds[i] / maxMagnitude) * magnitude;
        }
    }

    //From FTC Lib RobotDrive
    private void normalize(double[] wheelSpeeds) {
        double maxMagnitude = Math.abs(wheelSpeeds[0]);
        for (int i = 1; i < wheelSpeeds.length; i++) {
            double temp = Math.abs(wheelSpeeds[i]);
            if (maxMagnitude < temp) {
                maxMagnitude = temp;
            }
        }
        if (maxMagnitude > 1) {
            for (int i = 0; i < wheelSpeeds.length; i++) {
                wheelSpeeds[i] = (wheelSpeeds[i] / maxMagnitude);
            }
        }
    }

    /**
     * Drives the robot in robot centric mode, with movement inputs relative to the robot's orientation.
     *
     * @param forward  The forward power
     * @param strafe   The strafe power
     * @param rotation The rotation power
     */
    public void driveRobotCentric(double forward, double strafe, double rotation) {
        driveFieldCentric(forward, strafe, rotation, 0.0);
    }

    /**
     * Drives the robot in field centric mode, with movement inputs relative to the field's orientation.
     *
     * @param forward  The forward power
     * @param strafe   The strafe power
     * @param rotation The rotation power
     * @param heading  The robot's heading
     */
    public void driveFieldCentric(double forward, double strafe, double rotation, double heading) {
        double strafeSpeed = Range.clip(strafe, -1, 1);
        double forwardSpeed = Range.clip(forward, -1, 1);
        double turnSpeed = Range.clip(rotation, -1, 1);

        Vector2d input = new Vector2d(strafeSpeed, forwardSpeed);
        input = input.rotateBy(-heading);

        double theta = input.angle();

        double[] wheelSpeeds = new double[4];
        //Front Left
        wheelSpeeds[0] = Math.sin(theta + Math.PI / 4);
        //Front Right
        wheelSpeeds[2] = Math.sin(theta - Math.PI / 4);
        //Back Left
        wheelSpeeds[1] = Math.sin(theta - Math.PI / 4);
        //Back Right
        wheelSpeeds[3] = Math.sin(theta + Math.PI / 4);

        normalize(wheelSpeeds, input.magnitude());

        wheelSpeeds[0] += turnSpeed;
        wheelSpeeds[2] -= turnSpeed;
        wheelSpeeds[1] += turnSpeed;
        wheelSpeeds[3] -= turnSpeed;

        normalize(wheelSpeeds);

        setMotorPowers(wheelSpeeds[0], wheelSpeeds[1], wheelSpeeds[2], wheelSpeeds[3]);
    }

    /**
     * Private method to set the motor powers.
     *
     * @param fl The front left motor power
     * @param fr The front right motor power
     * @param bl The back left motor power
     * @param br The back right motor power
     */
    public void setMotorPowers(double fl, double bl, double fr, double br) {
        frontLeft.setPower(fl * currentMultiplier);
        frontRight.setPower(fr * currentMultiplier);
        backLeft.setPower(bl * currentMultiplier);
        backRight.setPower(br * currentMultiplier);
    }

    public double[] getRRM() {
        return new double[]{
                frontLeft.getVelocity() / TICKS_PER_WHEEL_REVOLUTION * 60.0,
                frontRight.getVelocity() / TICKS_PER_WHEEL_REVOLUTION * 60.0,
                backLeft.getVelocity() / TICKS_PER_WHEEL_REVOLUTION * 60.0,
                backRight.getVelocity() / TICKS_PER_WHEEL_REVOLUTION * 60.0
        };
    }

    @Override
    public void periodic() {
        frontLeftSlideCurrentAverage.add(frontLeft.getCurrent());
        frontRightSlideCurrentAverage.add(frontRight.getCurrent());
        backLeftSlideCurrentAverage.add(backLeft.getCurrent());
        backRightSlideCurrentAverage.add(backRight.getCurrent());

        robotState.setDriveCurrent(frontLeftSlideCurrentAverage.getAverage() + frontRightSlideCurrentAverage.getAverage() + backLeftSlideCurrentAverage.getAverage() + backRightSlideCurrentAverage.getAverage());

        if ((robotState.getDriveCurrent() > MAX_CURRENT_DRAW) && timer.milliseconds() > 500) {
            currentMultiplier = currentMultiplier * 0.95;
            timer.reset();
        } else {
            currentMultiplier = 1.0;
        }
    }
}
