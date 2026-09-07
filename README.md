# ftc-state-machine

This is a library intended to be used by FTC teams to create state machines to control their robot.
It allows the user to create a custom state maachine by creating custom team.techtigers.states and noting 
conditions where a state should end. It uses [FTCLib](https://docs.ftclib.org/ftclib/v/v2.0.0) and
their command structure to do this. 

# Installation 

TODO: Add installation instructions

# Usage

There are 4 main components of the state machine to use:

- **GlobalState**: This is meant to be used as a template for user code, and allows the user to store
                    robot data in a way that can be accessable to all. This class should be initialized
                    once in the opmode.
- **Condition**: This isn't in the library, but it should be created as an enum for usage in the team.techtigers.states.
- **State**: States extend an FTCLib Command with an addditional method called *getCurrentCondition*, which
              which returns the current condition of the State with the type of the Condition enum. In addition,
              it also has a name attribute.     
- **StateMachine**: This class is the engine behind the process, and should be initialized in the opmode using
                    the given methods to determine what state transitions there should be.

# Example

TODO: Add an example opmode
