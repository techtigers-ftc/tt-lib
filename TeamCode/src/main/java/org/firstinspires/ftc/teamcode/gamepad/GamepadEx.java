package org.firstinspires.ftc.teamcode.gamepad;

import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * A wrapper for the FTC SDK's gamepad class that provides additional functionality.
 */
public class GamepadEx {
    private final Gamepad gamepad;

    public GamepadEx(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    /**
     * Returns a command trigger for a digital gamepad button.
     *
     * @param button the button to monitor
     * @return a trigger that is active while the button is pressed
     */
    public Trigger getGamepadButton(GamepadKeys.Button button) {
        switch(button) {
            case A:
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

    /**
     * Returns how far an analog trigger is pressed, from {@code 0.0} to {@code 1.0}.
     *
     * @param trigger the analog trigger to read
     * @return the trigger's current value
     */
    public double getTrigger(GamepadKeys.Trigger trigger) {
        switch (trigger) {
            case LEFT_TRIGGER:
                return gamepad.left_trigger;
            case RIGHT_TRIGGER:
                return gamepad.right_trigger;
            default:
                return 0.0;
        }
    }

    /**
     * Returns the left stick's horizontal value, from {@code -1.0} to {@code 1.0}.
     */
    public double getLeftX() {
        return gamepad.left_stick_x;
    }

    /**
     * Returns the left stick's vertical value, from {@code -1.0} to {@code 1.0},
     * with up reported as positive.
     */
    public double getLeftY() {
        return -gamepad.left_stick_y;
    }

    /**
     * Returns the right stick's horizontal value, from {@code -1.0} to {@code 1.0}.
     */
    public double getRightX() {
        return gamepad.right_stick_x;
    }

    /**
     * Returns the right stick's vertical value, from {@code -1.0} to {@code 1.0}.
     */
    public double getRightY() {
        return gamepad.right_stick_y;
    }

    /**
     * Runs a rumble effect on the gamepad.
     *
     * @param rumbleEffect the rumble effect to run
     */
    public void rumble(Gamepad.RumbleEffect rumbleEffect) {
        gamepad.runRumbleEffect(rumbleEffect);
    }

    /**
     * Runs a rumble effect on the gamepad.
     *
     * @param leftMotorPower the power for the left rumble motor, from {@code 0.0} to {@code 1.0}
     * @param rightMotorPower the power for the right rumble motor, from {@code 0.0} to {@code 1.0}
     * @param duration the duration of the rumble effect in milliseconds
     */
    public void rumble(double leftMotorPower, double rightMotorPower, int duration) {
        gamepad.rumble(leftMotorPower, rightMotorPower, duration);
    }
}
