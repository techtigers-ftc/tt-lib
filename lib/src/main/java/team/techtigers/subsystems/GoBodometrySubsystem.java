package team.techtigers.subsystems;

import com.pedropathing.geometry.Pose;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;

import team.techtigers.subsystems.utils.OdomConfig;
import team.techtigers.utils.AngleUtilities;

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
     * @param odomConfig  The odometry configuration, including offsets and encoder directions.
     */
    public GoBodometrySubsystem(HardwareMap hardwareMap,
                                Pose startPose, OdomConfig odomConfig) {
        super("Gobodometry Subsystem");

        odo = hardwareMap.get(GoBildaPinpointDriver.class, odomConfig.name);
        odo.setOffsets(odomConfig.xOffset, odomConfig.yOffset, DistanceUnit.INCH);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.setEncoderDirections(odomConfig.xDirection,
                odomConfig.yDirection);

        odo.resetPosAndIMU();

        this.startPose = new Pose2D(DistanceUnit.INCH, startPose.getX(),
                startPose.getY(), AngleUnit.RADIANS, startPose.getHeading());
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

    @Override
    public void justAfterStart() {
        odo.setPosition(startPose);
        robotState.set("robotPose", startPose);
    }

    @Override
    public void periodic() {
        odo.update();

        double heading = AngleUtilities.norm(odo.getHeading(AngleUnit.RADIANS));
        double headingVelocity = odo.getHeadingVelocity(UnnormalizedAngleUnit.RADIANS);

        Pose robotPose = new Pose(odo.getPosX(DistanceUnit.INCH),
                odo.getPosY(DistanceUnit.INCH), heading);

        if (Double.isNaN(robotPose.getX()) || Double.isNaN(robotPose.getY()) || Double.isNaN(robotPose.getHeading())) {
            return;
        }

        robotState.set("robotPose", robotPose);

        Pose robotVelocity = new Pose(odo.getVelX(DistanceUnit.INCH),
                odo.getVelY(DistanceUnit.INCH), headingVelocity);

        if (Double.isNaN(robotVelocity.getX()) || Double.isNaN(robotVelocity.getY()) || Double.isNaN(robotVelocity.getHeading())) {
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