package org.firstinspires.ftc.teamcode.opmodes;

import android.graphics.Point;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.autostates.BasicDriveState;
import org.firstinspires.ftc.teamcode.autostates.EndState;
import org.firstinspires.ftc.teamcode.pathing.Constants;
import org.firstinspires.ftc.teamcode.statemachine.StateMachine;
import org.firstinspires.ftc.teamcode.subsystems.AutoSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.GoBodometrySubsystem;
import org.firstinspires.ftc.teamcode.utils.TTLogger;
import org.firstinspires.ftc.teamcode.utils.enums.AutoStateCondition;

@Autonomous
public class PedroTestOpMode extends BaseOpMode{
    @Override
    protected void initialize() {
        TTLogger.setLoggingLevel(TTLogger.DEBUG);

        StateMachine<AutoStateCondition> stateMachine = new StateMachine<>();
        AutoSubsystem auto = new AutoSubsystem(stateMachine, robotState);

        GoBodometrySubsystem odometry = new GoBodometrySubsystem(hardwareMap, new Pose(0,0, Math.toRadians(90)));
        DriveSubsystem drive = new DriveSubsystem(hardwareMap);

        registerSubsystems(auto, odometry, drive);

        Follower follower = Constants.createFollower(drive, robotState);

        BasicDriveState driveStateOne = new BasicDriveState("drive state one", follower, robotState, 10);
        driveStateOne.setPathChain(
                follower.pathBuilder()
                        .addPath(new BezierLine(
                            new Pose(0,0),
                            new Pose(20,0)
                        ))
                        .setLinearHeadingInterpolation(Math.toRadians(90), 0)
                        .addPath(new BezierCurve(
                                new Pose(20,0),
                                new Pose(12,24),
                                new Pose(0,0)
                        ))
                        .setTranslationalConstraint(1)
                        .setLinearHeadingInterpolation(Math.PI / 2, 0)
                        .build()
        )
                .setHeadingPIDF(3.0, 0);

        BasicDriveState driveStateTwo = new BasicDriveState("drive state two", follower, robotState, 10);
        driveStateTwo.setPathChain(
                follower.pathBuilder()
//                        .addPath(
//                        new BezierLine(
//                                new Pose(20,0),
//                                new Pose(0,0)
//                        ))
//                        .setTangentHeadingInterpolation()
                        .addPath(new BezierCurve(
                                new Pose(0,0),
                                new Pose(12,24),
                                new Pose(20,0)
                        ))
                        .setTangentHeadingInterpolation()
                        .build()
        )
                .setHeadingPIDF(3.0, 0.0);

        EndState endState = new EndState("end state");

        driveStateOne.configureCommands();
        driveStateTwo.configureCommands();

        stateMachine.addState(driveStateOne);
        stateMachine.addState(driveStateTwo);
        stateMachine.addState(endState);

        stateMachine.setCurrentState(driveStateOne);
        stateMachine.addTransitions(driveStateOne, driveStateTwo, AutoStateCondition.DRIVE_END);
        stateMachine.addTransitions(driveStateTwo, endState, AutoStateCondition.DRIVE_END);
        telemetry.addLine("Initialize Done");
    }

    @Override
    protected void update() {
        telemetry.addData("current pose", robotState.getRobotPose());
        telemetry.addData("current state", robotState.getCurrentAutoState());
    }

    @Override
    protected boolean isBlue() {
        return false;
    }
}
