package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.RobotIntakeManipulator;

public class IntakeCommand extends CommandBase{
    private final RobotIntakeManipulator manipulator;
    private final Supplier<Double>       speedSupplier;

    public IntakeCommand(
        RobotIntakeManipulator manipulator,
        Supplier<Double>       speedSupplier )
    {
        this.manipulator   = manipulator;
        this.speedSupplier = speedSupplier;
        addRequirements( manipulator );
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        manipulator.intake( speedSupplier.get() );
    }

    @Override
    public void end( boolean interrupted ) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
