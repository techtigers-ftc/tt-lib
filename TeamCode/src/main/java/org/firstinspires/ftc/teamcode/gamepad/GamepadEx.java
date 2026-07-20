package org.firstinspires.ftc.teamcode.gamepad;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.utils.TTLogger;

public class GamepadEx {
    private Gamepad gamepad;
    private final String tag = this.getClass().getSimpleName();
    public GamepadEx(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    public Trigger getGamepadButton(GamepadButtons button) {
        switch(button) {
            case A:
                TTLogger.dd(tag, "Case A ran");
                return new Trigger(() -> gamepad.a);
            case B:
                return new Trigger(() -> gamepad.b);
            case X:
                return new Trigger(() -> gamepad.x);
            case Y:
                return new Trigger(() -> gamepad.y);
            case LEFT_BUMPER:
                return new Trigger(() -> gamepad.left_bumper);
            case RIGHT_BUMPER:
                return new Trigger(() -> gamepad.right_bumper);
            case LEFT_STICK_BUTTON:
                return new Trigger(() -> gamepad.left_stick_button);
            case RIGHT_STICK_BUTTON:
                return new Trigger(() -> gamepad.right_stick_button);
            case DPAD_UP:
                return new Trigger(() -> gamepad.dpad_up);
            case DPAD_DOWN:
                return new Trigger(() -> gamepad.dpad_down);
            case DPAD_LEFT:
                return new Trigger(() -> gamepad.dpad_left);
            case DPAD_RIGHT:
                return new Trigger(() -> gamepad.dpad_right);
            case START:
                return new Trigger(() -> gamepad.start);
            case  BACK:
                return new Trigger(() -> gamepad.back);
            default:
                return new Trigger(() -> false);
        }
    }

    public void rumble(Gamepad.RumbleEffect rumbleEffect) {
        gamepad.runRumbleEffect(rumbleEffect);
    }

    public void rumble(double leftMotorPower, double rightMotorPower, int duration) {
        gamepad.rumble(leftMotorPower, rightMotorPower, duration);
    }
}
