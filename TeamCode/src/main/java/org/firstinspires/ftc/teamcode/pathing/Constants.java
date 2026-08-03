package org.firstinspires.ftc.teamcode.pathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.subsystems.AutoSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.utils.RobotState;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .centripetalScaling(0)
            .mass(5.443);

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static DriveConstants driveConstants = new DriveConstants()
            .maxPower(1);

    public static Follower createFollower(HardwareMap hardwareMap, DriveSubsystem drive, RobotState robotState) {
        return new Follower(followerConstants, new RobotStateLocalizer(robotState), new Mecanum(driveConstants, drive, robotState));
    }
}