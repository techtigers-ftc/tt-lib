package team.techtigers.utils;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PIDFController;

/**
 * This class encapsulates the logic for setting slides to a given position into one class so that
 * it can be used by other team.techtigers.subsystems (namely the intake and dropper team.techtigers.subsystems) without
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
        this.pidfController = new PIDFController(new PIDFCoefficients(pidf.P, pidf.I, pidf.D, 0));
        kF = pidf.F;

        tolerance = 0;
    }

    /**
     * Allows users to change/set the PIDF coefficients for the feedback controller
     *
     * @param coefficients the new PIDF coefficients
     */
    public void setPIDFCoefficients(PIDFCoefficients coefficients) {
        pidfController.setCoefficients(new PIDFCoefficients(coefficients.P, coefficients.I, coefficients.D, 0));
        kF = coefficients.F;
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
        pidfController.setTargetPosition(targetDistance * ticksPerInch);
    }

    /**
     * Calculates and returns the needed motor power to move slides to a given position
     *
     * @param currentTicks the current encoder ticks of the slides
     * @return the motor power needed to move the slides to the target position
     */
    public double calculateMotorPowers(double currentTicks) {
        pidfController.updatePosition(currentTicks);

        if (tolerance > 0 && Math.abs(pidfController.getError()) < tolerance) {
            return 0;
        }

        double currentPower = pidfController.run();
        int sign = (int) Math.signum(currentPower);
        return (Math.abs(currentPower) + Math.abs(kF)) * sign;
    }

    /**
     *
     * @return the target ticks for the slides
     */
    public double getTargetTicks() {
        return pidfController.getTargetPosition();
    }
}