package team.techtigers.subsystems;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import team.techtigers.utils.RobotState;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * SubsystemController is a singleton class that runs of all the subsystems methods and loops.
 * It provides methods to register subsystems, initialize them, and call their periodic methods.
 */
public class SubsystemController {
    private static SubsystemController instance;
    private ArrayList<Subsystem> subsystems;

    private SubsystemController() {
        subsystems = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of SubsystemController. If the instance does not exist, it creates a new one.
     *
     * @return the singleton instance of SubsystemController
     */
    public static SubsystemController getInstance() {
        if (instance == null) {
            instance = new SubsystemController();
        }
        return instance;
    }

    /**
     * Registers the provided subsystems with the SubsystemController and sets their parameters.
     *
     * @param telemetry the telemetry object to be used by the subsystems
     * @param robotState the robotState object to be used by the subsystems
     * @param subsystems the subsystems to be registered
     */
    public void registerSubsystems(Telemetry telemetry, RobotState robotState, Subsystem... subsystems) {
        this.subsystems.addAll(Arrays.asList(subsystems));
        for (Subsystem subsystem : subsystems) {
            subsystem.setParameters(telemetry, robotState);
        }
    }

    /**
     * Calls the initLoop method of all registered subsystems. Init loop in the subsystem should
     * not be blocking.
     */
    public void initLoop() {
        for (Subsystem subsystem : subsystems) {
            subsystem.initLoop();
        }
    }

    /**
     * Calls the justAfterStart method of all registered subsystems. This method is called after the start of the opmode.
     */
    public void justAfterStart() {
        for (Subsystem subsystem : subsystems) {
            subsystem.justAfterStart();
        }
    }

    /**
     * Calls the periodic method of all registered subsystems. This method is called in the main loop of the opmode.
     */
    public void periodic() {
        for (Subsystem subsystem : subsystems) {
            subsystem.periodic();
        }
    }

    /**
     * Calls the close method of all registered subsystems. This method is called at the end of the opmode.
     */
    public void close() {
        for (Subsystem subsystem : subsystems) {
            subsystem.close();
        }
    }

    /**
     * Resets the instance of this class to be null
     */
    public static void reset() {
        instance = null;
    }
}
