package team.techtigers.utils;

/**
 * Represents a waypoint in a trajectory. A waypoint is a point in a trajectory that the robot must
 * pass through.
 */
public class Waypoint { // TODO Integrate core and remove this waypoint class
    private final Point point;
    private final double heading;

    /**
     * Constructs a new Waypoint using a point input
     *
     * @param point   The pose to be saved inside the class
     * @param heading The heading
     */
    public Waypoint(Point point, double heading) {
        this.point = point;
        this.heading = heading;
    }

    /**
     * Constructs a new Waypoint using x, y, and heading inputs
     *
     * @param x       The x coordinate of the waypoint
     * @param y       The y coordinate of the waypoint
     * @param heading The heading of the waypoint
     */
    public Waypoint(double x, double y, double heading) {
        this(new Point(x, y), heading);
    }

    /**
     * Constructs a new Waypoint using x and y inputs, assuming heading is 0
     *
     * @param x The x coordinate of the waypoint
     * @param y The y coordinate of the waypoint
     */
    public Waypoint(double x, double y) {
        this(new Point(x, y), 0);
    }

    /**
     * Constructs a Waypoint using a Point
     *
     * @param point The point of the Waypoint
     */
    public Waypoint(Point point) {
        this(point.x, point.y);
    }


    /**
     * @return the waypoint as a Point
     */
    public Point getPoint() {
        return point;
    }

    /**
     * @return the x coordinate of the waypoint
     */
    public double getX() {
        return point.getX();
    }

    /**
     * @return the y coordinate of the waypoint
     */
    public double getY() {
        return point.getY();
    }

    /**
     * @return the heading of the waypoint
     */
    public double getHeading() {
        return this.heading;
    }

    /**
     * Adds this waypoint to another waypoint, combining their positions and headings.
     *
     * @param other The waypoint to add
     * @return A new Waypoint with the sum of positions and headings
     */
    public Waypoint add(Waypoint other) {
        return new Waypoint(
            this.getX() + other.getX(),
            this.getY() + other.getY(),
            this.heading + other.heading
        );
    }

    /**
     * Subtracts another waypoint from this waypoint.
     *
     * @param other The waypoint to subtract
     * @return A new Waypoint with the difference of positions and headings
     */
    public Waypoint subtract(Waypoint other) {
        return new Waypoint(
            this.getX() - other.getX(),
            this.getY() - other.getY(),
            this.heading - other.heading
        );
    }

    /**
     * Scales this waypoint by a scalar factor while keeping the original waypoint immutable.
     *
     * @param factor The scalar to multiply by
     * @return A new Waypoint scaled by the factor
     */
    public Waypoint multiply(double factor) {
        return new Waypoint(
            this.getX() * factor,
            this.getY() * factor,
            this.heading * factor
        );
    }

    /**
     * Divides this waypoint by a scalar factor while keeping the original waypoint immutable.
     *
     * @param divisor The scalar to divide by
     * @return A new Waypoint scaled by 1 / divisor
     * @throws IllegalArgumentException if divisor is zero
     */
    public Waypoint divide(double divisor) {
        if (divisor == 0.0d) {
            throw new IllegalArgumentException("Divisor cannot be zero");
        }
        return multiply(1.0d / divisor);
    }

    @Override
    public String toString() {
        return "x: " + getX() + " y: " + getY() + " h: " + getHeading();
    }
}
