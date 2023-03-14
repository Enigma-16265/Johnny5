package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.RobotArmLiftManipulator;

public class ArmLiftCommand extends CommandBase{
    private final RobotArmLiftManipulator manipulator;
    private final Supplier<Double>        speedSupplier;

    public ArmLiftCommand(
        RobotArmLiftManipulator manipulator,
        Supplier<Double>        speedSupplier )
    {
        this.manipulator   = manipulator;
        this.speedSupplier = speedSupplier;
        addRequirements( manipulator );
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        manipulator.lift( speedSupplier.get() );
    }

    @Override
    public void end( boolean interrupted ) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
