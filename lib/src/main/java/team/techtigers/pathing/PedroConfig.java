package team.techtigers.pathing;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PredictiveBrakingCoefficients;

/**
 * Class with configuration for Pedro pathing, using the predictive braking algorithm
 */
public class PedroConfig {
    // Default values set
    public double centripetalScaling = 0.0;
    public double mass = 13.0;
    public PIDFCoefficients headingPIDFCoefficients = new PIDFCoefficients(2, 0, 0, 0);
    public PredictiveBrakingCoefficients predictiveBrakingCoefficients = new PredictiveBrakingCoefficients(0.1, 0.047, 0.0017);
    public double maxPower = 1;
    public double xVelocity = 45.35;
    public double yVelocity = 65.18;

    /**
     * Sets the centripetal scaling for the predictive braking algorithm
     *
     * @param centripetalScaling the centripetal scaling to set
     * @return the current instance of PedroConfig for method chaining
     */
    public PedroConfig centripetalScaling(double centripetalScaling) {
        this.centripetalScaling = centripetalScaling;
        return this;
    }

    /**
     * Sets the mass for the predictive braking algorithm
     *
     * @param mass the mass to set
     * @return the current instance of PedroConfig for method chaining
     */
    public PedroConfig mass(double mass) {
        this.mass = mass;
        return this;
    }

    /**
     * Sets the heading PIDF coefficients for the predictive braking algorithm
     *
     * @param headingPIDFCoefficients the heading PIDF coefficients to set
     * @return the current instance of PedroConfig for method chaining
     */
    public PedroConfig headingPIDFCoefficients(PIDFCoefficients headingPIDFCoefficients) {
        this.headingPIDFCoefficients = headingPIDFCoefficients;
        return this;
    }

    /**
     * Sets the predictive braking coefficients for the predictive braking algorithm
     *
     * @param predictiveBrakingCoefficients the predictive braking coefficients to set
     * @return the current instance of PedroConfig for method chaining
     */
    public PedroConfig predictiveBrakingCoefficients(PredictiveBrakingCoefficients predictiveBrakingCoefficients) {
        this.predictiveBrakingCoefficients = predictiveBrakingCoefficients;
        return this;
    }

    /**
     * Sets the maximum power for the predictive braking algorithm
     *
     * @param maxPower the maximum power to set
     * @return the current instance of PedroConfig for method chaining
     */
    public PedroConfig maxPower(double maxPower) {
        this.maxPower = maxPower;
        return this;
    }

    /**
     * Sets the x velocity for the predictive braking algorithm
     *
     * @param xVelocity the x velocity to set
     * @return the current instance of PedroConfig for method chaining
     */
    public PedroConfig xVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
        return this;
    }

    /**
     * Sets the y velocity for the predictive braking algorithm
     *
     * @param yVelocity the y velocity to set
     * @return the current instance of PedroConfig for method chaining
     */
    public PedroConfig yVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
        return this;
    }
}
