package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.display.AdafruitNeoPixel;
import org.firstinspires.ftc.teamcode.display.TestView;
import org.firstinspires.ftc.teamcode.display.VisualDisplaySubsystem;
import org.firstinspires.ftc.teamcode.gamepad.GamepadKeys;
import org.firstinspires.ftc.teamcode.gamepad.Trigger;
import org.firstinspires.ftc.teamcode.utils.SlidingAverageCalculator;
import org.firstinspires.ftc.teamcode.utils.TTLogger;

@TeleOp
public class DisplayTestOpMode extends BaseOpMode {
    private VisualDisplaySubsystem visualDisplay;
    private double lastLoopTime = 0;
    private SlidingAverageCalculator slidingAverageCalculator;
    private ElapsedTime timer;

    @Override
    protected void initialize() {
        AdafruitNeoPixel driver = hardwareMap.get(AdafruitNeoPixel.class, "visual_display");
        driver.initialize(256, 3);
        visualDisplay = new VisualDisplaySubsystem(driver, new TestView(robotState));
        slidingAverageCalculator = new SlidingAverageCalculator(100);

        Trigger switchLEDs = new Trigger(() -> driverGamepad.getGamepadButton(GamepadKeys.Button.DPAD_UP).get());
        switchLEDs.toggleWhenActive(() -> robotState.setLedShowing(true), () -> robotState.setLedShowing(false));

        timer = new ElapsedTime();
        timer.reset();

        TTLogger.setLoggingLevel(TTLogger.DEBUG);
    }

    @Override
    protected void update() {
        visualDisplay.periodic();

        telemetry.addData("X Showing", robotState.isLedShowing());
        telemetry.addData("Cycle time", slidingAverageCalculator.getAverage());
        slidingAverageCalculator.add(timer.milliseconds());

        double loop = System.nanoTime();
        telemetry.addData("Cycle Time (hz)", 1e9 / (loop - lastLoopTime));
        lastLoopTime = loop;
        timer.reset();

        telemetry.update();
    }

    @Override
    protected boolean isBlue() {
        return false;
    }
} 