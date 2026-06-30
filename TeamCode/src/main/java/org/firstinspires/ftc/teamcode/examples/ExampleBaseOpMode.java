package org.firstinspires.ftc.teamcode.examples;

import org.firstinspires.ftc.teamcode.opmodes.BaseOpMode;

public class ExampleBaseOpMode extends BaseOpMode {

    @Override
    public void initialize() {
        super.initialize();
        ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
        registerSubsystems(exampleSubsystem);
    }

    @Override
    public void update() {
        super.update();
        telemetry.addLine("Update Loop Running");
    }


    @Override
    protected boolean isBlue() {
        return false;
    }
}
