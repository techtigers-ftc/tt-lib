package team.techtigers.utils;

import java.util.HashMap;

/**
 * A class to store information that is global to the entire robot. This is
 * intended to be extended from, and child classes can add in additional
 * attributes that are desired in the state.
 */
public class RobotState extends GlobalState {
    private HashMap<String, Object> stateMap = new HashMap<>();
    private final boolean isBlue;

    /**
     * Creates a new RobotState with the specified alliance color.
     *
     * @param isBlue true if the robot is on the blue alliance, false if on the red alliance
     */
    public RobotState(boolean isBlue) {
        this.isBlue = isBlue;
    }

    /**
     * Returns the value associated with the specified key in the stateMap.
     *
     * @param key the key to look up in the stateMap
     * @return the value associated with the key, or null if the key is not found
     * @param <T> the type of the value associated with the key
     */
    public <T> T get(String key) {
        if (stateMap.containsKey(key)) {
            return (T) stateMap.get(key);
        } else {
            TTLogger.ee("RobotState", "Key not found in stateMap: " + key);
            return null;
        }
    }

    public void set(String key, Object value) {
        stateMap.put(key, value);
    }

    /**
     * Returns true if the robot is on the blue alliance, false if on the red alliance.
     *
     * @return true if the robot is on the blue alliance, false if on the red alliance
     */
    public boolean isBlue() {
        return isBlue;
    }
}