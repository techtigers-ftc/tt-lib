# TT Lib

TT Lib is an FTC robot library built around commands, subsystems, state machines, and Pedro Pathing. Commands describe robot actions, subsystems own hardware, and a state machine connects those actions into an autonomous routine.

TT Lib was created by FTC team **Tech Tigers**. Working example files are available in the [Tech Tigers Quickstart repository](https://github.com/techtigers-ftc/TechTigersQuickstart), and video tutorials explaining how to use TT Lib will be available soon.

If you have questions or suggestions, contact the team at [techtigers33958@gmail.com](mailto:techtigers33958@gmail.com).

## Choose a version

TT Lib has two distributions. They contain the same command, subsystem, state-machine, gamepad, drivetrain, and odometry tools, but only one includes Pedro Pathing:

- **Pathing — `v1.3.0-pathing`:** includes TT Lib's Pedro Pathing integration and the Pedro dependency. Use this version when you want `Follower`, `DriveStateBase`, `BasicDriveState`, and the pathing utilities described later in this README.
- **Non-pathing — `v1.3.0-nopathing`:** does not include Pedro Pathing or TT Lib's Pedro-specific classes. Use this lighter version when you only need the command-based architecture, state machine, gamepad helpers, drivetrain, or odometry.

Add only one distribution to a project. Do not add both at the same time.

## Installation

In your FTC project, open `build.dependencies.gradle` and add JitPack to the `repositories` section:

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

Then add the distribution you want to the `dependencies` section of the same file.

For TT Lib with Pedro Pathing:

```groovy
dependencies {
    implementation 'com.github.techtigers-ftc:tt-lib:v1.3.0-pathing'
}
```

For TT Lib without Pedro Pathing:

```groovy
dependencies {
    implementation 'com.github.techtigers-ftc:tt-lib:v1.3.0-nopathing'
}
```

Sync the project with Gradle after saving the file. The full `pathing` and `nopathing` suffixes make it clear which distribution is being installed.

## Command-based architecture

The command-based architecture separates *what the robot does* from *how its hardware is controlled*:

- A **subsystem** owns a piece of hardware and exposes methods that commands can use.
- A **command** performs one action through one or more subsystems.
- The **command scheduler** initializes commands, updates them every robot loop, and ends them when they finish or are canceled.
- A **trigger** schedules a command in response to a gamepad button or another boolean condition.
- A **command group** combines several commands into a larger action.
- `BaseOpMode` connects these parts to the FTC OpMode lifecycle.

### Subsystems

Extend `Subsystem` for each major robot mechanism. A subsystem owns its hardware and exposes methods that commands can call. Use TT Lib's `CachedMotor` wrapper for motors so repeated power values are not unnecessarily sent to the hardware controller.

Registered subsystems follow this lifecycle:

1. The constructor creates or receives the hardware objects.
2. `initLoop()` runs repeatedly while the OpMode is in INIT.
3. `justAfterStart()` runs once immediately after Start.
4. `periodic()` runs once during every active robot loop.
5. `close()` runs once when the OpMode ends.

Override only the hooks the subsystem needs. Put sensor reads, control calculations, and other continuously repeated work in `periodic()`. Use `close()` to stop hardware or release resources.

For example:

```java
public class IntakeSubsystem extends Subsystem {
    private final CachedMotor intakeMotor;

    public IntakeSubsystem(HardwareMap hardwareMap) {
        intakeMotor = new CachedMotor(hardwareMap, "intake");
    }

    public void setPower(double power) {
        intakeMotor.setPower(power);
    }

    @Override
    public void periodic() {
        telemetry.addData("intake current", intakeMotor.getCurrent());
    }

    @Override
    public void close() {
        intakeMotor.stop();
    }
}
```

### Commands

Create a command by extending `CommandBase`. The scheduler runs a scheduled command through this lifecycle:

1. `initialize()` runs once when the command is scheduled.
2. `update()` runs every scheduler loop while the command remains scheduled.
3. `isFinished()` is checked every loop after `update()`.
4. `end(false)` runs after normal completion.
5. `end(true)` runs instead when the command is canceled or interrupted.

A command that should remain active must return `false` from `isFinished()`. Its `end(...)` method should leave its hardware in a safe state regardless of whether it finished normally or was interrupted.

For example:

```java
public class RunIntakeCommand extends CommandBase {
    private final IntakeSubsystem intake;
    private final ElapsedTime timer = new ElapsedTime();

    public RunIntakeCommand(IntakeSubsystem intake) {
        this.intake = intake;
    }

    @Override
    public void initialize() {
        timer.reset();
    }

    @Override
    public void update() {
        intake.setPower(1.0);
    }

    @Override
    public boolean isFinished() {
        return timer.seconds() >= 2.0;
    }

    @Override
    public void end(boolean interrupted) {
        intake.setPower(0.0);
    }
}
```

## BaseOpMode

`BaseOpMode` is TT Lib's connection to the FTC SDK. It extends `LinearOpMode`, creates the shared `RobotState`, wraps `gamepad1` and `gamepad2` as `driverGamepad` and `manipulatorGamepad`, updates every registered subsystem, evaluates triggers, runs scheduled commands, and updates telemetry.

The `BaseOpMode` lifecycle is:

1. `isBlue()` is used to create `robotState`, and both `GamepadEx` objects are created.
2. Your `initialize()` method runs once. Construct all subsystems and commands here, register the subsystems, and create trigger bindings.
3. While the robot is in INIT, every registered subsystem's `initLoop()` runs repeatedly.
4. After Start, every subsystem's `justAfterStart()` runs, followed by the OpMode's `justAfterStart()`.
5. During each active loop, subsystem `periodic()` methods run first, then the command scheduler evaluates triggers and commands, and then the OpMode's `update()` runs.
6. When the OpMode stops, subsystem `close()` methods run, followed by the OpMode's `close()`.

Call `registerSubsystems(...)` once with every subsystem. Registration provides each subsystem with telemetry and the shared `RobotState`. It also resets the subsystem controller and command scheduler, so register the subsystems before creating trigger bindings.

Here is a complete OpMode using the subsystem and command shown above:

```java
@TeleOp(name = "Example Command TeleOp")
public class ExampleTeleOp extends BaseOpMode {
    private IntakeSubsystem intake;

    @Override
    protected void initialize() {
        RobotStateInitializer.initialize(robotState);

        intake = new IntakeSubsystem(hardwareMap);
        registerSubsystems(intake);

        RunIntakeCommand runIntake = new RunIntakeCommand(intake);

        driverGamepad
                .getGamepadButton(GamepadKeys.Button.A)
                .whileActiveOnce(runIntake);
    }

    @Override
    protected void update() {
        telemetry.addData("runtime", getRuntime());
    }

    @Override
    protected boolean isBlue() {
        return true;
    }
}
```

You can also schedule or cancel commands directly:

```java
CommandScheduler.getInstance().schedule(runIntake);
CommandScheduler.getInstance().cancel(runIntake);
```

### Triggers and GamepadEx

A `Trigger` turns any `BooleanSupplier` into an event source that the command scheduler evaluates every loop. Construct triggers once in `initialize()`; creating them repeatedly in `update()` would keep registering new trigger objects.

Create a trigger directly for a sensor, state value, or analog threshold:

```java
Trigger objectDetected = new Trigger(distanceSensor::isObjectDetected);

Trigger rightTriggerPressed = new Trigger(
        () -> driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER) > 0.5
);
```

For digital gamepad buttons, `GamepadEx.getGamepadButton(...)` creates the trigger for you:

```java
Trigger aButton = driverGamepad.getGamepadButton(GamepadKeys.Button.A);
Trigger leftBumper = driverGamepad.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER);
```

Bind a command or `Runnable` to the trigger:

- `whenActive(...)` schedules it once on the inactive-to-active edge.
- `whileActiveOnce(...)` schedules it on activation and cancels it on deactivation.
- `toggleWhenActive(...)` starts it on one activation and cancels it on the next.
- `toggleWhenActive(first, second)` alternates between two commands or actions.

Triggers can be compounded with `and(...)`, `or(...)`, and `negate()`:

```java
Trigger allowedToRun = aButton
        .and(leftBumper.negate())
        .or(rightTriggerPressed.and(objectDetected));

allowedToRun.whileActiveOnce(runIntake);
```

The compound trigger above is active when A is pressed without the left bumper, or when both the right analog trigger and the object sensor are active.

## Command groups and utility commands

TT Lib includes several ways to compose commands:

- `SequentialCommandGroup` runs commands one at a time in the supplied order.
- `ParallelCommandGroup` runs all commands together and finishes after all of them finish.
- `ParallelRaceGroup` runs all commands together and finishes when the first command finishes.
- `ParallelDeadlineGroup` runs all commands together and finishes when its deadline command finishes.
- `InstantCommand` runs a `Runnable` once.
- `WaitCommand` finishes after a duration; milliseconds are the default unit.
- `WaitUntilCommand` finishes when a `BooleanSupplier` becomes true.
- `TimeoutWaitUntilCommand` finishes when its `BooleanSupplier` becomes true or its timeout expires. Its default time unit is milliseconds, or you can pass a `TimeUnit` explicitly.

For example:

```java
Command scorePixel = new SequentialCommandGroup(
        new InstantCommand(() -> lift.moveToScoringHeight()),
        new TimeoutWaitUntilCommand(lift::isAtTarget, 2, TimeUnit.SECONDS),
        new InstantCommand(claw::open),
        new WaitCommand(250)
);
```

## Robot configuration

Robot-wide hardware settings can be used with either TT Lib distribution. Put them in a class with static methods instead of repeating motor names, directions, odometry offsets, and encoder directions across multiple OpModes. This follows the `RobotConfiguration` pattern used by the Pollination Run examples.

```java
public final class RobotConfiguration {
    private RobotConfiguration() {
    }

    public static DriveConfig driveConfig() {
        return new DriveConfig()
                .fl("left_front")
                .fr("right_front")
                .bl("left_back")
                .br("right_back")
                .flDirection(DcMotorSimple.Direction.FORWARD)
                .frDirection(DcMotorSimple.Direction.REVERSE)
                .blDirection(DcMotorSimple.Direction.FORWARD)
                .brDirection(DcMotorSimple.Direction.REVERSE);
    }

    public static OdomConfig odomConfig() {
        return new OdomConfig()
                .name("odo")
                .xOffset(-3.5)
                .yOffset(0.0)
                .xDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
                .yDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);
    }
}
```

`DriveConfig` contains the four Robot Controller motor names and their directions. `OdomConfig` contains the Pinpoint hardware name, pod offsets in inches, and encoder directions. The X count must increase when the robot moves forward, and the Y count must increase when it moves left.

These configurations are useful even when you are not following Pedro paths:

```java
DriveSubsystem drive = new DriveSubsystem(
        hardwareMap,
        RobotConfiguration.driveConfig()
);
```

If you use the pathing distribution, add the tuned `ForesightConfig` method described in the Pedro Pathing section to this same class.

## Shared robot state

`RobotState` wraps a shared `HashMap<String, Object>` for values that several commands and subsystems need to read or update. `BaseOpMode` creates one instance named `robotState` and passes that same instance to every registered subsystem.

Store a value with `set(key, value)` and retrieve it with `get(key)`:

```java
robotState.set("hasPixel", true);
robotState.set("liftTarget", 1200);

boolean hasPixel = robotState.get("hasPixel");
int liftTarget = robotState.get("liftTarget");
Pose robotPose = robotState.get("robotPose");
```

Keys are case-sensitive, and each key should always hold the same type. A missing key makes `get(...)` log an error and return `null`, which can cause a `NullPointerException` when the value is used. Initialize every key before any command or subsystem reads it.

If you only need TT Lib's built-in values, call this at the beginning of your OpMode's `initialize()` method:

```java
@Override
protected void initialize() {
    RobotStateInitializer.initialize(robotState);

    // Create and register subsystems, commands, and states after initialization.
}
```

`RobotStateInitializer.initialize(...)` defines the standard pose, velocity, voltage, autonomous-state, remaining-time, and drivetrain-current values used by TT Lib.

If your robot needs additional shared values, create a team-specific initializer that first loads the TT Lib defaults and then adds your own keys:

```java
public final class TeamRobotStateInitializer {
    private TeamRobotStateInitializer() {
    }

    public static void initialize(RobotState robotState) {
        RobotStateInitializer.initialize(robotState);

        robotState.set("hasPixel", false);
        robotState.set("liftTarget", 0);
        robotState.set("selectedAuto", "NONE");
    }
}
```

Then call only your team initializer at the beginning of the OpMode:

```java
TeamRobotStateInitializer.initialize(robotState);
```

This keeps all defaults in one place and makes the keys available before the robot loop begins.

## State machine

A state machine is useful when an autonomous routine needs to choose its next action from the current result instead of following only one fixed command sequence.

Each state is also a command. A state:

- has a unique name;
- configures the commands it will run;
- reports its current condition, normally with an enum;
- can transition to another state when that condition matches a configured transition.

### 1. Define the conditions

Use an enum so transition values are easy to understand:

```java
public enum AutoCondition {
    RUNNING,
    COMPLETE,
    TIMED_OUT
}
```

One `StateMachine<T>` uses one condition type, so every state added to that machine must use the same `T`.

Pedro drive states extend the generic `DriveStateBase<T>`. Create a `DriveStateConfig<T>` at the beginning of each pathing autonomous OpMode's `initialize()` method, after creating the follower and before constructing any drive states. The config supplies the follower and the condition values that `getCurrentCondition()` returns while running, after path completion, and on a state timeout. Pass that config to each drive state's constructor, using the same condition type as the state machine.

You can use TT Lib's `AutoStateCondition` values (`RUNNING`, `DRIVE_END`, and `TIMEOUT`), or supply values from your own enum, such as the `AutoCondition` above. The pathing section shows the required configuration and constructor calls. The existing `AutoSubsystem` and `EndState` still use `AutoStateCondition`; a machine using a custom enum needs matching lifecycle and terminal-state implementations.

### 2. Create a state

Use `SequentialCommandGroupState` when the state's commands should run in order, or `ParallelCommandGroupState` when they should run together.

```java
public class ScoreState extends SequentialCommandGroupState<AutoCondition> {
    private final LiftSubsystem lift;

    public ScoreState(String name, LiftSubsystem lift, double timeoutSeconds) {
        super(name, timeoutSeconds);
        this.lift = lift;
    }

    @Override
    public void configureCommands() {
        addCommands(
                new InstantCommand(() -> lift.moveToScoringHeight()),
                new WaitUntilCommand(lift::isAtTarget)
        );
    }

    @Override
    public AutoCondition getCurrentCondition() {
        if (isTimeoutReached()) {
            return AutoCondition.TIMED_OUT;
        }
        return lift.isAtTarget() ? AutoCondition.COMPLETE : AutoCondition.RUNNING;
    }
}
```

### 3. Add states and transitions

Add every state before adding transitions. Then select the initial state.

```java
StateMachine<AutoCondition> stateMachine = new StateMachine<>();

ScoreState score = new ScoreState("score", lift, 3.0);
ParkState park = new ParkState("park");
StopState stop = new StopState("stop");

stateMachine
        .addState(score)
        .addState(park)
        .addState(stop)
        .addTransitions(score, park,
                AutoCondition.COMPLETE,
                AutoCondition.TIMED_OUT)
        .addTransitions(park, stop,
                AutoCondition.COMPLETE,
                AutoCondition.TIMED_OUT);

stateMachine.setCurrentState(score);
```

`addTransitions(from, to, conditions...)` means "while `from` is active, move to `to` when any supplied condition matches."

> **Important:** `addState(...)` calls that state's `configureCommands()` method. Do not call `configureCommands()` yourself before adding the state, or its child commands may be added more than once.

### 4. Run the machine

For a general state machine, call `start()` once after the OpMode starts and `update()` every active loop.

For an autonomous routine using `AutoStateCondition`, the easier option is to create and register an `AutoSubsystem`:

```java
StateMachine<AutoStateCondition> stateMachine = new StateMachine<>();
AutoSubsystem auto = new AutoSubsystem(stateMachine, robotState);

registerSubsystems(auto, odometry, drive);
```

`AutoSubsystem.justAfterStart()` starts the machine, and `AutoSubsystem.periodic()` updates it and stores the current and previous state names in `RobotState`.

## Pedro Pathing 3.0 configuration

TT Lib connects Pedro Pathing's `Follower` to `DriveSubsystem` and to the pose stored in `RobotState`. Set up Pedro in this order:

1. Tune the drivetrain, localizer, and Foresight algorithm for the physical robot.
2. Add the generated `ForesightConfig` to `RobotConfiguration`.
3. Create the follower with the tuned configuration.
4. Create a `DriveStateConfig` for this autonomous OpMode, supplying the follower and condition values.
5. Pass that config to each drive-state constructor, then assign its path and optional settings.

### 1. Tune Foresight before creating the follower

Do not begin with copied or guessed Foresight values. Use the Pedro Pathing 3 [AutoTune procedures](https://pedropathing.com/docs/pathing/tuning) and run the [Foresight AutoTuner](https://pedropathing.com/docs/pathing/tuning/foresight) on your robot. The procedure measures the maximum forward and strafe velocities, natural forward and strafe deceleration, linear and heading braking coefficients, translational controller gains, heading gain, and coast and brake feedforward values.

When AutoTune finishes, open its **Java** tab and copy the complete generated `ForesightConfig` into your team's constants file. Pedro's guide calls this file `Constants.java`; in a TT Lib project, you can place the same generated configuration in `RobotConfiguration.foresightConfig()` to avoid confusing it with TT Lib's own `team.techtigers.pathing.Constants` class. Do this before calling `Constants.createFollower(...)`: all required Foresight values must be defined when Pedro constructs the algorithm.

The generated values are only the initial configuration. Test the completed follower on the robot and make any refinements recommended by Pedro's tuning guide. Never use another team's numbers as final values because drivetrain speed, weight, friction, wheel condition, and voltage behavior differ between robots.

### 2. Add Foresight to `RobotConfiguration`

Add a method for the generated Foresight configuration to the `RobotConfiguration` class introduced earlier:

```java
public static ForesightConfig foresightConfig() {
    return new ForesightConfig(c -> {
        // Paste the complete configuration generated by the
        // Pedro Pathing 3 Foresight AutoTuner here.
    });
}
```

The empty `ForesightConfig` body above is a placement example, not a working configuration. It must be replaced with the complete Java output from AutoTune. Among other values, that output sets the required forward and strafe translation controllers, coast and brake controllers, heading controller, braking matrices, heading braking coefficients, achievable velocities, and natural decelerations.

Use all three configuration methods when constructing the pathing-related objects in `initialize()`:

```java
Pose startPose = new Pose(0, 0, Math.toRadians(90));

GoBodometrySubsystem odometry = new GoBodometrySubsystem(
        hardwareMap,
        startPose,
        RobotConfiguration.odomConfig()
);

DriveSubsystem drive = new DriveSubsystem(
        hardwareMap,
        RobotConfiguration.driveConfig()
);

registerSubsystems(auto, odometry, drive);

Follower follower = Constants.createFollower(
        drive,
        robotState,
        RobotConfiguration.foresightConfig()
);
```

`Constants.createFollower(...)` connects the drive subsystem to TT Lib's Pedro drivetrain adapter, supplies a `RobotStateLocalizer`, and constructs Pedro's `Foresight` algorithm from the tuned configuration.

### 3. Create a `DriveStateConfig` before constructing drive states

Every autonomous OpMode that uses TT Lib's Pedro drive states must create a `DriveStateConfig<T>` during `initialize()`. Create it once for that OpMode, immediately after creating the follower, and reuse it for the drive states that share that follower and condition mapping. The config contains a live follower reference, so construct it as part of the OpMode's initialization rather than keeping it in a static configuration class.

Import the configuration class and the condition enum:

```java
import team.techtigers.utils.DriveStateConfig;
import team.techtigers.utils.enums.AutoStateCondition;
```

After the follower setup shown above, create the config:

```java
// Inside initialize(), after creating follower and before any drive states.
DriveStateConfig<AutoStateCondition> driveStateConfig = new DriveStateConfig<>(
        AutoStateCondition.RUNNING,
        AutoStateCondition.DRIVE_END,
        AutoStateCondition.TIMEOUT,
        follower
);
```

The arguments are the running condition, finished condition, timeout condition, and follower, in that order. `BasicDriveState<T>` and any custom state extending `DriveStateBase<T>` receive this config in their constructor instead of receiving a follower directly. The state timeout is still a separate constructor argument in seconds.

The three configuration roles are different: `ForesightConfig` tunes Pedro's following algorithm, `DriveStateConfig<T>` connects a follower to your state-machine conditions, and `AutoConfigurator` assigns each state's path and optional overrides.

To use the custom enum from the state-machine example, the equivalent config is:

```java
DriveStateConfig<AutoCondition> driveStateConfig = new DriveStateConfig<>(
        AutoCondition.RUNNING,
        AutoCondition.COMPLETE,
        AutoCondition.TIMED_OUT,
        follower
);
```

In that case, use `BasicDriveState<AutoCondition>` with a `StateMachine<AutoCondition>` and other states that use `AutoCondition` too.

### 4. Create poses and paths with the Pedro 3 factories

Pedro 3 replaces the old `follower.pathBuilder()` system with static factory methods in `Paths`. A `PoseFactory` is the recommended way to create poses because it makes the heading unit explicit and can apply field transformations to every pose it produces. The pose factory, named poses, path construction, and drive-state setters can all live together in the autonomous configuration class shown in the next section.

```java
import static com.pedropathing.api.Paths.*;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
```

Use `PoseFactory.degrees()` when the third argument should be interpreted as degrees, or `PoseFactory.radians()` when it is already in radians. A transformed factory such as `POSES.mirrorX(axis)` or `POSES.mirrorY(axis)` can generate the corresponding paths for the opposite side of the field without rewriting every pose.

Pedro provides these path factories:

- `line(start, end)` creates a straight path.
- `curve(start, control..., end)` creates a Bézier curve. The first and last poses are endpoints; every pose between them is a control point that shapes the curve and is not necessarily crossed by the robot.
- `through(pose...)` creates a Bézier path that passes through the supplied poses. Use it when intermediate poses are locations the robot must cross rather than control points.
- `path(first, second, ...)` joins existing paths into one compound path whose segments are followed in order.

Create paths inside methods instead of storing mutable path objects throughout the OpMode. This is also the form generated by the Pedro visualizer.

Every path also needs a heading interpolation. The most common choices are:

- `.constant(heading)` keeps one heading for the whole path.
- `.linear(startHeading, endHeading)` rotates through the shortest angular distance while driving. Passing two poses uses their headings.
- `.linear(startHeading, endHeading, endT)` finishes the rotation early at the supplied path-progress value.
- `.tangent()` points the robot along the direction of travel.
- `.reverseTangent()` points opposite the direction of travel.
- `.facingPoint(target)` continuously aims the robot at a pose or point.

For a compound path, place the interpolation on each child path when the segments need different heading behavior. Placing one interpolation on the result of `path(...)` overrides the child headings across the complete compound path.

See Pedro's [path creation reference](https://pedropathing.com/docs/pathing/reference/api) and [interpolation reference](https://pedropathing.com/docs/pathing/reference/interpolation) for additional examples.

### 5. Put poses, paths, and drive-state settings in `AutoConfigurator`

Create each `BasicDriveState` in the OpMode, but move its poses, path creation, and setter calls to static methods in a separate `AutoConfigurator` class. This is the Pedro 3 equivalent of the Pollination Run `AutoConfigurator` pattern and keeps the state-machine transitions readable.

The old builder required a `Follower` argument because it called `follower.pathBuilder()`. Pedro 3's static `Paths` factories do not need the follower, so each configuration method only needs the `DriveStateBase` that it will configure.

```java
package org.firstinspires.ftc.teamcode;

import static com.pedropathing.api.Paths.*;
import static team.techtigers.subsystems.AutoSubsystem.MEDIUM_TOLERANCE;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import team.techtigers.autostates.DriveStateBase;

public final class AutoConfigurator {
    // Change this to PoseFactory.radians() if headings will be supplied in radians.
    private static final PoseFactory POSES = PoseFactory.degrees();

    // Keep every named autonomous pose in one place.
    public static final Pose START = POSES.of(0, 0, 90);
    private static final Pose SCORE = POSES.of(40, 0, 0);
    private static final Pose CONTROL = POSES.of(20, 35, 45);
    private static final Pose PARK = POSES.of(0, 0, 90);

    private AutoConfigurator() {
    }

    private static Path driveStateOnePath() {
        return line(START, SCORE).linear(START, SCORE);
    }

    private static Path driveStateTwoPath() {
        return curve(SCORE, CONTROL, PARK).linear(SCORE, PARK);
    }

    public static <T> void configureDriveStateOne(DriveStateBase<T> state) {
        state
                // The path is the only value every drive state must set.
                // All omitted options keep their existing configured defaults.
                .setPath(driveStateOnePath());
    }

    public static <T> void configureDriveStateTwo(DriveStateBase<T> state) {
        state
                .setPath(driveStateTwoPath())
                // Set only the options that this path needs to change.
                .setMaxPathSpeed(0.80)
                .setTolerance(MEDIUM_TOLERANCE)
                .setAngleTolerance(Math.toRadians(2.0))
                .setTimeoutConstraint(500.0);
    }
}
```

`START` is public so the OpMode can pass the exact same pose to `GoBodometrySubsystem`. Additional poses can remain private when only `AutoConfigurator` uses them. The private path methods keep path construction reusable while the public methods describe exactly how each drive state should run. `setAngleTolerance(...)` expects radians, so convert a degree value with `Math.toRadians(...)` as shown.

`setPath(...)` is the only setter that every drive state must call. All other setters are optional. If an optional setter is omitted, the state keeps the value already supplied by the follower's tuned `ForesightConfig`, or Pedro's built-in default when that option was not changed in the configuration. TT Lib's `DriveStateBase` constructor sets its translational and heading tolerances to `AutoSubsystem.MEDIUM_TOLERANCE` and `AutoSubsystem.MEDIUM_ANGLE_TOLERANCE`; call the corresponding setters only when a path needs different endpoint tolerances.

The second method intentionally changes only four options. The remaining options still use their configured defaults. Its numbers are examples; tune any overridden values for the path and robot instead of copying them unchanged.

Configure every drive state before starting the state machine. Drive states that use the same `Follower` also use the same `ForesightConfig`; their setter calls therefore update shared settings, and the last value assigned for each setting is the value that follower uses. Use common settings for states that share a follower, or provide separate followers and configurations when states require different Foresight values.

| Setting | Effect |
| --- | --- |
| `setPath(path)` | Selects the Pedro 3 `Path` to follow. It accepts either an individual or compound path and must be set before the state starts. |
| `setHoldEnd(hold)` | If `true`, Pedro continues correcting toward the final pose after completion. If `false`, it becomes idle and stops commanding the drive. |
| `setMaxPathSpeed(scale)` | Caps speed to a fraction of the direction-dependent achievable speed; `0.8` means 80 percent. It is not an inches-per-second value. |
| `setMaxVelocityConstraint(velocity)` | Applies an absolute coasting-speed cap in distance units per second. Pedro uses the lower of this cap, the achievable speed, and the fractional path-speed cap. |
| `setMaxAccelerationConstraint(acceleration)` | Limits how quickly target tangential velocity may increase, in distance units per second squared. |
| `setMaxDecelerationConstraint(deceleration)` | Builds a coasting deceleration profile before active braking. Do not set it above the robot's measured natural deceleration. |
| `setCoastDownToVelocity(velocity)` | Sets the speed that the maximum-deceleration profile approaches instead of zero. It only has an effect when a finite maximum-deceleration constraint is set. |
| `setBrakeAggression(value)` | Adjusts predicted braking: `1.0` is neutral, values above `1.0` allow more overshoot, and values below `1.0` favor undershooting. |
| `setBrakeAtEnd(brake)` | Enables or disables active braking at the final end of the path. |
| `setPathSkip(skip)` | For compound paths, allows Pedro to preserve momentum by advancing to the next segment at the predicted braking point instead of waiting for the current segment's parametric end. |
| `setHeadingDriveRatio(ratio)` | Chooses heading-versus-translation priority when output must be limited. `1.0` prioritizes heading; `0.0` prioritizes translation. |
| `setTolerance(distance)` | Sets the permitted final translational error in the path's distance units. |
| `setAngleTolerance(radians)` | Sets the permitted final heading error in radians. |
| `setTValue(margin)` | Sets the remaining parametric margin. With `0.025`, the path reaches its parametric threshold at `t >= 0.975`. Larger values permit earlier completion. |
| `setVelocityConstraint(velocity)` | Sets the maximum tangential speed allowed when evaluating final completion. |
| `setTimeoutConstraint(milliseconds)` | Sets how long Pedro may correct at the endpoint after parametric completion before it finishes anyway. This value is in milliseconds. |

The timeout supplied to the `BasicDriveState` constructor is separate and is measured in seconds. It limits the entire state; `setTimeoutConstraint(...)` only limits Pedro's endpoint-settling period.

In the same `initialize()` method, pass the `driveStateConfig` created above to each state constructor. Then call the corresponding `AutoConfigurator` method to assign the path and any optional overrides:

```java
BasicDriveState<AutoStateCondition> driveStateOne = new BasicDriveState<>(
        "drive state one",
        driveStateConfig,
        robotState,
        10.0
);
AutoConfigurator.configureDriveStateOne(driveStateOne);

BasicDriveState<AutoStateCondition> driveStateTwo = new BasicDriveState<>(
        "drive state two",
        driveStateConfig,
        robotState,
        10.0
);
AutoConfigurator.configureDriveStateTwo(driveStateTwo);

EndState end = new EndState("end");

stateMachine
        .addState(driveStateOne)
        .addState(driveStateTwo)
        .addState(end)
        .addTransitions(
                driveStateOne,
                driveStateTwo,
                AutoStateCondition.DRIVE_END,
                AutoStateCondition.TIMEOUT
        )
        .addTransitions(
                driveStateTwo,
                end,
                AutoStateCondition.DRIVE_END,
                AutoStateCondition.TIMEOUT
        );

stateMachine.setCurrentState(driveStateOne);
```

Register `AutoSubsystem`, `GoBodometrySubsystem`, and `DriveSubsystem` with the OpMode. `BaseOpMode` will then update the odometry, state machine, drivetrain, and command scheduler each loop.
