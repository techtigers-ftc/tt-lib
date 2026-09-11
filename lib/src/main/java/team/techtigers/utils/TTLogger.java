package team.techtigers.utils;

import com.qualcomm.robotcore.util.RobotLog;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * A logging class which wraps RobotLog and allows for setting a logging level.
 */
public class TTLogger {
    public static final int VERBOSE = 0;
    public static final int DEBUG = 1;
    public static final int INFO = 2;
    public static final int WARN = 3;
    public static final int ERROR = 4;
    public static final int DISABLED = 5;

    // default level
    private static int currentLevel = DISABLED;
    private static final Set<String> exceptionTags = new HashSet<>();

    /**
     * Sets the logging level for the logger should use the static constants defined in this class.
     * 0 - Verbose
     * 1 - Debug
     * 2 - Info
     * 3 - Warn
     * 4 - Error
     *
     * @param level the level of output the logger will output
     */
    public static void setLoggingLevel(int level) {
        currentLevel = level;
    }

    /**
     * Adds tags to the exception list, which will be logged regardless of the logging level.
     *
     * @param tags the tags to add to the exception list
     */
    public static void setTagException(String... tags) {
        exceptionTags.addAll(Arrays.asList(tags));
    }
    /**
     * Logs a RobotLog with the logging level verbose.
     *
     * @param tag     the tag associated with the message
     * @param message the message to log
     */
    public static void vv(String tag, String message, Object... args) {
        if (currentLevel <= VERBOSE || exceptionTags.contains(tag)) {
            RobotLog.vv(tag, message, args);
        }
    }

    /**
     * Logs a RobotLog with the logging level debug.
     *
     * @param tag     the tag associated with the message
     * @param message the message to log
     */
    public static void dd(String tag, String message, Object... args) {
        if (currentLevel <= DEBUG || exceptionTags.contains(tag)) {
            RobotLog.dd(tag, message, args);
        }
    }

    /**
     * Logs a RobotLog with the logging level info.
     *
     * @param tag     the tag associated with the message
     * @param message the message to log
     */
    public static void ii(String tag, String message, Object... args) {
        if (currentLevel <= INFO || exceptionTags.contains(tag)) {
            RobotLog.ii(tag, message, args);
        }
    }

    /**
     * Logs a RobotLog with the logging level warning.
     *
     * @param tag     the tag associated with the message
     * @param message the message to log
     */
    public static void ww(String tag, String message, Object... args) {
        if (currentLevel <= WARN || exceptionTags.contains(tag)) {
            RobotLog.ww(tag, message, args);
        }
    }

    /**
     * Logs a RobotLog with the logging level error.
     *
     * @param tag     the tag associated with the message
     * @param message the message to log
     */
    public static void ee(String tag, String message, Object... args) {
        if (currentLevel <= ERROR || exceptionTags.contains(tag)) {
            RobotLog.ee(tag, message, args);
        }
    }

    /**
     * Logs a RobotLog with the logging level verbose.
     *
     * @param tag     the tag associated with the message
     * @param message the message to log
     */
    public static void vv(String tag, String message) {
        if (currentLevel <= VERBOSE || exceptionTags.contains(tag)) {
            RobotLog.vv(tag, message);
        }
    }

    /**
     * Logs a RobotLog with the logging level debug.
     *
     * @param tag     the tag associated with the message
     * @param message the message to log
     */
    public static void dd(String tag, String message) {
        if (currentLevel <= DEBUG || exceptionTags.contains(tag)) {
            RobotLog.dd(tag, message);
        }
    }

    /**
     * Logs a RobotLog with the logging level info.
     *
     * @param tag     the tag associated with the message
     * @param message the message to log
     */
    public static void ii(String tag, String message) {
        if (currentLevel <= INFO || exceptionTags.contains(tag)) {
            RobotLog.ii(tag, message);
        }
    }

    /**
     * Logs a RobotLog with the logging level warning.
     *
     * @param tag     the tag associated with the message
     * @param message the message to log
     */
    public static void ww(String tag, String message) {
        if (currentLevel <= WARN || exceptionTags.contains(tag)) {
            RobotLog.ww(tag, message);
        }
    }

    /**
     * Logs a RobotLog with the logging level error.
     *
     * @param tag     the tag associated with the message
     * @param message the message to log
     */
    public static void ee(String tag, String message) {
        if (currentLevel <= ERROR || exceptionTags.contains(tag)) {
            RobotLog.ee(tag, message);
        }
    }

    /**
     * Logs a global warning message.
     *
     * @param msg the message to log
     */
    public static void addGlobalWarningMessage(String msg) {
        RobotLog.addGlobalWarningMessage(msg);
    }

    /**
     * Sets the global error message.
     *
     * @param message the message to set as the global error
     */
    public static void setGlobalErrorMsg(String message) {
        RobotLog.setGlobalErrorMsg(message);
    }
}

