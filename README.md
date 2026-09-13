# TT Lib

TT Lib is an FTC robot library built around commands, subsystems, state machines, and Pedro Pathing. Commands describe robot actions, subsystems own hardware, and a state machine connects those actions into an autonomous routine.

TT Lib was created by FTC team **Tech Tigers**. Working example files are available in the [Tech Tigers Quickstart repository](https://github.com/techtigers-ftc/TechTigersQuickstart), and video tutorials explaining how to use TT Lib will be available soon.

If you have questions or suggestions, contact the team at [techtigers33958@gmail.com](mailto:techtigers33958@gmail.com).

## Installation

In your FTC project, open `build.dependencies.gradle` and add JitPack to the `repositories` section:

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

Then add TT Lib to the `dependencies` section of the same file:

```groovy
dependencies {
    implementation 'com.github.techtigers-ftc:tt-lib:v1.1.0'
}
```

Sync the project with Gradle after saving the file.

## Command-based architecture

The command-based architecture separates *what the robot does* from *how its hardware is controlled*:

- A **subsystem** owns a piece of hardware and exposes methods that commands can use.
- A **command** performs one action through one or more subsystems.
- The **command scheduler** initializes commands, updates them every robot loop, and ends them when they finish or are canceled.
- A **trigger** schedules a command in response to a gamepad button or another boolean condition.
- A **command group** combines several commands into a larger action.
- `BaseOpMode` connects these parts to the FTC OpMode lifecycle.

### Subsystems

Extend `Subsystem` for each major robot mechanism. Construct the hardware in the subsystem, put continuously repeated work in `periodic()`, and leave hardware behavior in methods that commands can call. Use TT Lib's `CachedMotor` wrapper for motors so repeated power values are not unnecessarily sent to the hardware controller.

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
    public void close() {
        intakeMotor.stop();
    }
}
```

Register every subsystem from your `BaseOpMode`. Registration gives each subsystem access to telemetry and the shared `RobotState`, and lets `BaseOpMode` call its lifecycle methods automatically.

```java
IntakeSubsystem intake = new IntakeSubsystem(hardwareMap);
registerSubsystems(intake);
```

The subsystem lifecycle is:

- `initLoop()` runs repeatedly while the robot is in INIT.
- `justAfterStart()` runs once after the driver presses Start.
- `periodic()` runs every active OpMode loop.
- `close()` runs once when the OpMode ends.

### Commands

Create a command by extending `CommandBase` and overriding the lifecycle methods you need:

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

The scheduler calls these methods in order:

1. `initialize()` once when the command is scheduled.
2. `update()` every loop while the command is scheduled.
3. `isFinished()` every loop to decide whether the command is complete.
4. `end(false)` after normal completion, or `end(true)` when the command is canceled.

When your OpMode extends `BaseOpMode`, its loop updates the scheduler automatically. Bind commands during `initialize()`:

```java
RunIntakeCommand runIntake = new RunIntakeCommand(intake);

driverGamepad
        .getGamepadButton(GamepadKeys.Button.A)
        .whenActive(runIntake);
```

You can also schedule a command directly with `CommandScheduler.getInstance().schedule(command)`.

### Command groups

TT Lib includes several ways to compose commands:

- `SequentialCommandGroup` runs commands one at a time in the supplied order.
- `ParallelCommandGroup` runs all commands together and finishes after all of them finish.
- `ParallelRaceGroup` runs all commands together and finishes when the first command finishes.
- `ParallelDeadlineGroup` runs all commands together and finishes when its deadline command finishes.
- `InstantCommand` runs a `Runnable` once.
- `WaitCommand` finishes after a duration; milliseconds are the default unit.
- `WaitUntilCommand` finishes when a `BooleanSupplier` becomes true.

For example:

```java
Command scorePixel = new SequentialCommandGroup(
        new InstantCommand(() -> lift.moveToScoringHeight()),
        new WaitUntilCommand(lift::isAtTarget),
        new InstantCommand(claw::open),
        new WaitCommand(250)
);
```

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

TT Lib provides `AutoStateCondition`, a predetermined set containing `RUNNING`, `DRIVE_END`, and `TIMEOUT` for its Pedro drive states. You can use it as-is for the built-in autonomous states, or create your own enum following the same pattern when your states need different results, such as `OBJECT_DETECTED`, `SCORE_COMPLETE`, or `FAILED`.

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

`addTransitions(from, to, conditions...)` means "while `from` is active, move to `to` when any supplied condition matches." The equivalent builder syntax is:

```java
stateMachine.from(score).to(park).when(AutoCondition.COMPLETE);
```

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
2. Put the robot-wide configuration in one class.
3. Create the odometry and drive subsystems.
4. Create the follower with the tuned `ForesightConfig`.
5. Build paths and assign them to drive states.

### 1. Tune Foresight before creating the follower

Do not begin with copied or guessed Foresight values. Use the Pedro Pathing 3 [AutoTune procedures](https://pedropathing.com/docs/pathing/tuning) and run the [Foresight AutoTuner](https://pedropathing.com/docs/pathing/tuning/foresight) on your robot. The procedure measures the maximum forward and strafe velocities, natural forward and strafe deceleration, linear and heading braking coefficients, translational controller gains, heading gain, and coast and brake feedforward values.

When AutoTune finishes, open its **Java** tab and copy the complete generated `ForesightConfig` into your team's constants file. Pedro's guide calls this file `Constants.java`; in a TT Lib project, you can place the same generated configuration in `RobotConfiguration.foresightConfig()` to avoid confusing it with TT Lib's own `team.techtigers.pathing.Constants` class. Do this before calling `Constants.createFollower(...)`: all required Foresight values must be defined when Pedro constructs the algorithm.

The generated values are only the initial configuration. Test the completed follower on the robot and make any refinements recommended by Pedro's tuning guide. Never use another team's numbers as final values because drivetrain speed, weight, friction, wheel condition, and voltage behavior differ between robots.

### 2. Keep robot-wide settings in a configuration class

The drivetrain mapping, odometry setup, and tuned Foresight configuration do not need to fill the OpMode. Put them in a separate class with static methods, following the pattern of `RobotConfiguration` in the Pollination Run example project:

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

    public static ForesightConfig foresightConfig() {
        return new ForesightConfig(c -> {
            // Paste the complete configuration generated by the
            // Pedro Pathing 3 Foresight AutoTuner here.
        });
    }
}
```

The empty `ForesightConfig` body above is a placement example, not a working configuration. It must be replaced with the complete Java output from AutoTune. Among other values, that output sets the required forward and strafe translation controllers, coast and brake controllers, heading controller, braking matrices, heading braking coefficients, achievable velocities, and natural decelerations.

Create the robot objects from those methods in `initialize()`:

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

`OdomConfig` contains the Pinpoint hardware name, pod offsets in inches, and encoder directions. The X count must increase when the robot moves forward, and the Y count must increase when it moves left. `DriveConfig` contains the four configured motor names and directions. The `Pose` passed to `GoBodometrySubsystem` is the autonomous starting pose.

`Constants.createFollower(...)` connects the drive subsystem to TT Lib's Pedro drivetrain adapter, supplies a `RobotStateLocalizer`, and constructs Pedro's `Foresight` algorithm from the tuned configuration.

### 3. Create poses and paths with the Pedro 3 factories

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

### 4. Put poses, paths, and drive-state settings in `AutoConfigurator`

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

    public static void configureDriveStateOne(DriveStateBase state) {
        state
                // The path is the only value every drive state must set.
                // All omitted options keep their existing configured defaults.
                .setPathChain(driveStateOnePath());
    }

    public static void configureDriveStateTwo(DriveStateBase state) {
        state
                .setPathChain(driveStateTwoPath())
                // Set only the options that this path needs to change.
                .setMaxPathSpeed(0.80)
                .setTolerance(MEDIUM_TOLERANCE)
                .setAngleTolerance(Math.toRadians(2.0))
                .setTimeoutConstraint(500.0);
    }
}
```

`START` is public so the OpMode can pass the exact same pose to `GoBodometrySubsystem`. Additional poses can remain private when only `AutoConfigurator` uses them. The private path methods keep path construction reusable while the public methods describe exactly how each drive state should run. `setAngleTolerance(...)` expects radians, so convert a degree value with `Math.toRadians(...)` as shown.

`setPathChain(...)` is the only setter that every drive state must call. All other setters are optional. If an optional setter is omitted, the state keeps the value already supplied by the follower's tuned `ForesightConfig`, or Pedro's built-in default when that option was not changed in the configuration. TT Lib's `DriveStateBase` constructor sets its translational and heading tolerances to `AutoSubsystem.MEDIUM_TOLERANCE` and `AutoSubsystem.MEDIUM_ANGLE_TOLERANCE`; call the corresponding setters only when a path needs different endpoint tolerances.

The second method intentionally changes only four options. The remaining options still use their configured defaults. Its numbers are examples; tune any overridden values for the path and robot instead of copying them unchanged.

Configure every drive state before starting the state machine. Drive states that use the same `Follower` also use the same `ForesightConfig`; their setter calls therefore update shared settings, and the last value assigned for each setting is the value that follower uses. Use common settings for states that share a follower, or provide separate followers and configurations when states require different Foresight values.

| Setting | Effect |
| --- | --- |
| `setPathChain(path)` | Selects the Pedro 3 `Path` to follow. Despite the method name, it accepts either an individual or compound path. This must be set before the state starts. |
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

Use the configuration method immediately after constructing the state:

```java
BasicDriveState driveStateOne = new BasicDriveState(
        "drive state one",
        follower,
        robotState,
        10.0
);
AutoConfigurator.configureDriveStateOne(driveStateOne);

BasicDriveState driveStateTwo = new BasicDriveState(
        "drive state two",
        follower,
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
