package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.gamepad.GamepadEx;
import org.firstinspires.ftc.teamcode.states.GamepadWaitState_A;
import org.firstinspires.ftc.teamcode.states.GamepadWaitState_B;
import org.firstinspires.ftc.teamcode.statemachine.StateMachine;
import org.firstinspires.ftc.teamcode.subsystems.AutoSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.DriveSubsystem;
import org.firstinspires.ftc.teamcode.subsystems.GoBodometrySubsystem;
import org.firstinspires.ftc.teamcode.utils.RobotState;
import org.firstinspires.ftc.teamcode.utils.TTLogger;
import org.firstinspires.ftc.teamcode.utils.enums.AutoStateCondition;

@TeleOp
public class StateMachineTestOpMode extends BaseOpMode{
    private AutoSubsystem auto;
    private DriveSubsystem drive;
    private GoBodometrySubsystem odometry;
    private RobotState robotState;
    private StateMachine<AutoStateCondition> stateMachine;


    @Override
    protected void initialize() {
        robotState = new RobotState(isBlue());
        drive = new DriveSubsystem(hardwareMap);
        odometry = new GoBodometrySubsystem(hardwareMap);
        stateMachine = new StateMachine<AutoStateCondition>();

        GamepadEx gamepad = new GamepadEx(gamepad1);

        GamepadWaitState_A gamepadWaitStateA = new GamepadWaitState_A(
                "GamepadWaitState_A", 99999, gamepad);

        GamepadWaitState_B gamepadWaitStateB = new GamepadWaitState_B(
                "GamepadWaitState_B", 99999, gamepad);

        stateMachine
                .addState(gamepadWaitStateA)
                .addState(gamepadWaitStateB)

                .addTransitions(gamepadWaitStateA, gamepadWaitStateB, AutoStateCondition.DRIVE_END)

                .setCurrentState(gamepadWaitStateA);

        auto = new AutoSubsystem(stateMachine, robotState);
        registerSubsystems(drive, odometry, auto);

        TTLogger.setLoggingLevel(TTLogger.DEBUG);
    }

    @Override
    protected boolean isBlue() {
        return false;
    }

    @Override
    public void update() {
        telemetry.addData("Current State", stateMachine.getCurrentState());
    }
}
