package team.techtigers.subsystems;

import com.pedropathing.math.Pose;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;

/**
 * The odometry subsystem, using Gobilda Pinpoint.
 */
public class GoBodometrySubsystem extends Subsystem {
    private final GoBildaPinpointDriver odo;
    private final Pose2D startPose;
    private boolean resetComplete = false;

    /**
     * Initializes a new GoBodometrySubsystem.
     *
     * @param hardwareMap The hardware map, used to get hardware references
     * @param startPose   The starting pose of the robot.
     * @param odoName    The name of the GoBilda Pinpoint in the hardware map.
     * @param xOffset    X pod offset refers to how far sideways from the tracking point the X (forward) odometry pod is
     * @param yOffset    Y pod offset refers to how far forwards from the tracking point the Y (strafe) odometry pod is
     */
    public GoBodometrySubsystem(HardwareMap hardwareMap,
                                Pose startPose, String odoName, double xOffset, double yOffset) {
        super("Gobodometry Subsystem");

        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot team.techtigers.configuration step on the DS or RC devices.

        odo = hardwareMap.get(GoBildaPinpointDriver.class, odoName);

        /*
        Set the odometry pod positions relative to the point that the odometry computer tracks around.
        The X pod offset refers to how far sideways from the tracking point the
        X (forward) odometry pod is. Left of the center is a positive number,
        right of center is a negative number. the Y pod offset refers to how far forwards from
        the tracking point the Y (strafe) odometry pod is. forward of center is a positive number,
        backwards is a negative number.
         */
        odo.setOffsets(xOffset, yOffset, DistanceUnit.INCH);

        /*
        Set the kind of pods used by your robot. If you're using goBILDA odometry pods, select either
        the goBILDA_SWINGARM_POD, or the goBILDA_4_BAR_POD.
        If you're using another kind of odometry pod, uncomment setEncoderResolution and input the
        number of ticks per mm of your odometry pod.
         */
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

        /*
        Set the direction that each of the two odometry pods count. The X (forward) pod should
        increase when you move the robot forward. And the Y (strafe) pod should increase when
        you move the robot to the left.
         */
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.REVERSED,
                GoBildaPinpointDriver.EncoderDirection.FORWARD);

        /*
        Before running the robot, recalibrate the IMU. This needs to happen when the robot is stationary
        The IMU will automatically calibrate when first powered on, but recalibrating before running
        the robot is a good idea to ensure that the calibration is "good".
        resetPosAndIMU will reset the position to 0,0,0 and also recalibrate the IMU.
        This is recommended before you run your autonomous, as a bad initial calibration can cause
        an incorrect starting value for x, y, and heading.
         */
        odo.resetPosAndIMU();

        this.startPose = new Pose2D(DistanceUnit.INCH, startPose.x(),
                startPose.y(), AngleUnit.RADIANS, startPose.heading());
    }

    /**
     * Overload constructor for a GoBodometrySubsystem with a default odometry name of "odo".
     *
     * @param hardwareMap the hardware map, used to get hardware references
     * @param startPose the starting pose of the robot
     * @param xOffset    X pod offset refers to how far sideways from the tracking point the X (forward) odometry pod is
     * @param yOffset    Y pod offset refers to how far forwards from the tracking point the Y (strafe) odometry pod is
     */
    public GoBodometrySubsystem(HardwareMap hardwareMap, Pose startPose, double xOffset, double yOffset) {
        this(hardwareMap, startPose, "odo", xOffset, yOffset);
    }

    @Override
    public void initLoop() {
        ElapsedTime resetTimer = new ElapsedTime();
        resetTimer.reset();
        if (resetTimer.milliseconds() > 500 && !resetComplete) {
            telemetry.addLine("Odometry Reset Complete");
            resetComplete = true;
        }
    }

    /**
     * Initializes a new GoBodometrySubsystem with the start pose at (0, 0, 0).
     *
     * @param hardwareMap The hardware map, used to get hardware references
     */
    public GoBodometrySubsystem(HardwareMap hardwareMap) {
        this(hardwareMap, new Pose(0, 0), 0, 0);
    }

    @Override
    public void justAfterStart() {
        odo.setPosition(startPose);
    }

    @Override
    public void periodic() {
        odo.update();

        double heading = odo.getHeading(AngleUnit.RADIANS);
        double headingVelocity = odo.getHeadingVelocity(UnnormalizedAngleUnit.RADIANS);

        Pose robotPose = new Pose(odo.getPosX(DistanceUnit.INCH),
                odo.getPosY(DistanceUnit.INCH), heading);

        if (Double.isNaN(robotPose.x()) || Double.isNaN(robotPose.y()) || Double.isNaN(robotPose.heading())) {
            return;
        }

        robotState.set("robotPose", robotPose);

        Pose robotVelocity = new Pose(odo.getVelX(DistanceUnit.INCH),
                odo.getVelY(DistanceUnit.INCH), headingVelocity);

        if (Double.isNaN(robotVelocity.x()) || Double.isNaN(robotVelocity.y()) || Double.isNaN(robotVelocity.heading())) {
            return;
        }

        robotState.set("robotVelocity", robotVelocity);
    }

    /**
     * Gets the Pinpoint's internal loop time
     *
     * @return the Pinpoint's internal loop time
     */
    public double getLoopTime() {
        return odo.getLoopTime();
    }
}