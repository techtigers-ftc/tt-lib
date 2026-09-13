package team.techtigers.utils;

import com.qualcomm.robotcore.hardware.PIDFCoefficients;

/**
 * This class encapsulates the logic for setting slides to a given position into one class so that
 * it can be used by other subsystems (namely the intake and dropper subsystems) without
 * duplicating code.
 */
public class SlideController {
    private final double ticksPerInch;
    private final PIDFController pidfController;
    private double kF;
    private double tolerance;

    /**
     * Initializes the SlideController and PIDs movement of the slides
     *
     * @param ticksPerInch the number of encoder ticks per inch of slide travel
     * @param pidf  the initial PIDF coefficients for movement of the slides
     */
    public SlideController(double ticksPerInch, PIDFCoefficients pidf) {
        this.ticksPerInch = ticksPerInch;
        this.pidfController = new PIDFController(pidf.p, pidf.i, pidf.d, 0);
        kF = pidf.f;

        tolerance = 0;
    }

    /**
     * Overload constructor for SlideController that takes in individual PIDF coefficients instead of a PIDFCoefficients object
     *
     * @param ticksPerInch the number of encoder ticks per inch of slide travel
     * @param p the proportional coefficient for the PID controller
     * @param i the integral coefficient for the PID controller
     * @param d the derivative coefficient for the PID controller
     * @param f the feedforward coefficient for the PID controller
     */
    public SlideController(double ticksPerInch, double p, double i, double d, double f) {
        this(ticksPerInch, new PIDFCoefficients(p, i, d, f));
    }

    /**
     * Allows users to change/set the PIDF coefficients for the feedback controller
     *
     * @param coefficients the new PIDF coefficients
     */
    public void setPIDFCoefficients(PIDFCoefficients coefficients) {
        pidfController.setPIDF(coefficients.p, coefficients.i, coefficients.d, 0);
        kF = coefficients.f;
    }

    /**
     * Sets the tolerance for the PID controller
     *
     * @param tolerance the tolerance (position) for the controller
     */
    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    /**
     * Moves the slides to a specific, absolute position
     *
     * @param targetDistance the target distance in inches to move the slides to
     */
    public void moveToInches(double targetDistance) {
        pidfController.setSetPoint(targetDistance * ticksPerInch);
    }

    /**
     * Calculates and returns the needed motor power to move slides to a given position
     *
     * @param currentTicks the current encoder ticks of the slides
     * @return the motor power needed to move the slides to the target position
     */
    public double calculateMotorPowers(double currentTicks) {
        double currentPower = pidfController.calculate(currentTicks);

        if (tolerance > 0 && Math.abs(pidfController.getPositionError()) < tolerance) {
            return 0;
        }

        int sign = (int) Math.signum(currentPower);
        return (Math.abs(currentPower) + Math.abs(kF)) * sign;
    }

    /**
     *
     * @return the target ticks for the slides
     */
    public double getTargetTicks() {
        return pidfController.getSetPoint();
    }
}