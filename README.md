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
    implementation 'com.github.techtigers-ftc:tt-lib:v1.0.2-rc13'
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

Extend `Subsystem` for each major robot mechanism. Construct the hardware in the subsystem, put continuously repeated work in `periodic()`, and leave hardware behavior in methods that commands can call.

```java
public class IntakeSubsystem extends Subsystem {
    private final DcMotor intakeMotor;

    public IntakeSubsystem(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
    }

    public void setPower(double power) {
        intakeMotor.setPower(power);
    }

    @Override
    public void close() {
        intakeMotor.setPower(0.0);
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

## Pedro Pathing configuration

The setup used by `ExamplePedroOpMode` has four parts: configure odometry, configure the drivetrain, create a Pedro follower, and use that follower in drive states.

The values below are examples only. Measure and tune them for your robot with the Pedro Pathing tuning tools.

### 1. Configure odometry

`OdomConfig` describes the Pinpoint odometry computer. The offsets are in inches. The X encoder count should increase when the robot moves forward, and the Y encoder count should increase when it moves left.

```java
OdomConfig odomConfig = new OdomConfig()
        .name("odo")
        .xOffset(-3.5)
        .yOffset(0.0)
        .xDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
        .yDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);

GoBodometrySubsystem odometry = new GoBodometrySubsystem(
        hardwareMap,
        new Pose(0, 0, Math.toRadians(90)),
        odomConfig
);
```

The `Pose` passed to `GoBodometrySubsystem` is the robot's starting pose.

### 2. Configure the drivetrain

`DriveConfig` maps the four drivetrain motors and sets their directions. The names must match the Robot Controller configuration.

```java
DriveConfig driveConfig = new DriveConfig()
        .fl("left_front")
        .fr("right_front")
        .bl("left_back")
        .br("right_back")
        .flDirection(DcMotorSimple.Direction.FORWARD)
        .frDirection(DcMotorSimple.Direction.REVERSE)
        .blDirection(DcMotorSimple.Direction.FORWARD)
        .brDirection(DcMotorSimple.Direction.REVERSE);

DriveSubsystem drive = new DriveSubsystem(hardwareMap, driveConfig);
```

### 3. Configure and create the follower

`PedroConfig` collects the robot-specific values needed by the Pedro follower:

- `centripetalScaling` controls centripetal-force compensation.
- `mass` is the robot mass value used by Pedro's predictive braking model.
- `headingPIDFCoefficients` controls heading correction.
- `predictiveBrakingCoefficients` controls how the follower predicts and compensates for braking.
- `maxPower` limits drivetrain output.
- `xVelocity` and `yVelocity` are the tuned forward and lateral velocities.

```java
PedroConfig pedroConfig = new PedroConfig()
        .centripetalScaling(0.0)
        .mass(13.0)
        .headingPIDFCoefficients(
                new PIDFCoefficients(2.0, 0.0, 0.0, 0.0)
        )
        .predictiveBrakingCoefficients(
                new PredictiveBrakingCoefficients(0.1, 0.047, 0.0017)
        )
        .maxPower(1.0)
        .xVelocity(45.0)
        .yVelocity(65.0);

Follower follower = Constants.createFollower(drive, robotState, pedroConfig);
```

`Constants.createFollower(...)` copies these settings into Pedro's follower and drive constants, connects the follower to TT Lib's drivetrain adapter, and uses `RobotStateLocalizer` so Pedro reads the pose maintained in the shared `RobotState`.

### 4. Build paths and drive states

Create each path with the follower's path builder, then assign it to a `BasicDriveState`:

```java
BasicDriveState driveOut = new BasicDriveState(
        "drive out", follower, robotState, 10.0
);

driveOut
        .setPathChain(
                follower.pathBuilder()
                        .addPath(new BezierLine(
                                new Pose(0, 0),
                                new Pose(40, 0)
                        ))
                        .setLinearHeadingInterpolation(
                                Math.toRadians(90),
                                0
                        )
                        .build()
        )
        .setHeadingPIDF(0.5, 0.0);
```

The timeout passed to `BasicDriveState` is in seconds. `setHeadingPIDF(p, d)` overrides the follower's heading controller for that drive state. Other per-state options include translational and heading tolerances, a velocity constraint, a path `t` constraint, and Pedro's path timeout constraint.

Finally, add the drive states to the machine and transition on either normal path completion or the state timeout:

```java
EndState end = new EndState("end");

stateMachine
        .addState(driveOut)
        .addState(end)
        .addTransitions(
                driveOut,
                end,
                AutoStateCondition.DRIVE_END,
                AutoStateCondition.TIMEOUT
        );

stateMachine.setCurrentState(driveOut);
```

Register `AutoSubsystem`, `GoBodometrySubsystem`, and `DriveSubsystem` with the OpMode. `BaseOpMode` will then update the odometry, state machine, drivetrain, and command scheduler each loop.
