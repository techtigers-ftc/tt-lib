package team.techtigers.pathing;

import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PredictiveBrakingCoefficients;
import com.pedropathing.paths.PathConstraints;

public class PedroConstants {
    // Default values set
    public double centripetalScaling = 0.0;
    public double mass = 13.0;
    public PIDFCoefficients headingPIDFCoefficients = new PIDFCoefficients(2, 0, 0, 0);
    public PredictiveBrakingCoefficients predictiveBrakingCoefficients = new PredictiveBrakingCoefficients(0.1, 0.047, 0.0017);
    public double maxPower = 1;
    public double xVelocity = 45.35;
    public double yVelocity = 65.18;
}
