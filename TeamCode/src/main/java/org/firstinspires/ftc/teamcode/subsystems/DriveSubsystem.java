package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.utils.CachedMotor;
import org.firstinspires.ftc.teamcode.utils.SlidingAverageCalculator;
import org.firstinspires.ftc.teamcode.utils.TTLogger;
import org.firstinspires.ftc.teamcode.utils.Vector2d;

public class DriveSubsystem extends Subsystem {
    private double maxCurrentDraw = 100;
    private double currentMultiplier;
    public final CachedMotor frontLeft, frontRight;
    public final CachedMotor backLeft, backRight;
    private final SlidingAverageCalculator frontLeftSlideCurrentAverage;
    private final SlidingAverageCalculator frontRightSlideCurrentAverage;
    private final SlidingAverageCalculator backLeftSlideCurrentAverage;
    private final SlidingAverageCalculator backRightSlideCurrentAverage;

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

        frontRightSlideCurrentAverage = new SlidingAverageCalculator(10);
        frontLeftSlideCurrentAverage = new SlidingAverageCalculator(10);
        backRightSlideCurrentAverage = new SlidingAverageCalculator(10);
        backLeftSlideCurrentAverage = new SlidingAverageCalculator(10);

        CachedMotor[] motors = {frontLeft, backLeft, frontRight, backRight};
        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        for (CachedMotor motor : motors) {
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
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

    @Override
    public void periodic() {
        frontLeftSlideCurrentAverage.add(frontLeft.getCurrent());
        frontRightSlideCurrentAverage.add(frontRight.getCurrent());
        backLeftSlideCurrentAverage.add(backLeft.getCurrent());
        backRightSlideCurrentAverage.add(backRight.getCurrent());

        robotState.setDriveCurrent(frontLeftSlideCurrentAverage.getAverage() + frontRightSlideCurrentAverage.getAverage() + backLeftSlideCurrentAverage.getAverage() + backRightSlideCurrentAverage.getAverage());

        if (robotState.getDriveCurrent() > maxCurrentDraw) {
            currentMultiplier = currentMultiplier * 0.95;
        }

        TTLogger.dd(tag, "RPM: %f", (frontLeft.getVelocity() / 28) * 500/6000 * 60);
    }
}
