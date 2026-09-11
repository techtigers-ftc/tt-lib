package team.techtigers.utils;

/**
 * Various utilities for working with angles.
 */
public class AngleUtilities {
    public static final double TAU = Math.PI * 2;

    /**
     * Returns [angle] clamped to `[0, TAU]`.
     *
     * @param angle angle measure in radians
     */
    public static double norm(double angle) {
        double modifiedAngle = angle % TAU;

        modifiedAngle = (modifiedAngle + TAU) % TAU;

        return modifiedAngle;
    }

    /**
     * Returns distance from target position - current position
     *
     * @param currentPosition the current position
     * @param targetPosition the target position
     * @return angle error clamped to '[-PI, PI]'
     */
    public static double normDelta(double currentPosition, double targetPosition) {
        double modifiedAngleDelta = norm(targetPosition - currentPosition);

        if (modifiedAngleDelta > Math.PI) {
            modifiedAngleDelta -= TAU;
        }

        return modifiedAngleDelta;
    }

}