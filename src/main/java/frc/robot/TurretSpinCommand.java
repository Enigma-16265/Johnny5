package frc.robot;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class TurretSpinCommand extends CommandBase{
    private final RobotTurretSpinManipulator manipulator;
    private final Supplier<Double>           speedSupplier;

    public TurretSpinCommand(
        RobotTurretSpinManipulator manipulator,
        Supplier<Double>           speedSupplier )
    {
        this.manipulator   = manipulator;
        this.speedSupplier = speedSupplier;
        addRequirements( manipulator );
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        manipulator.spin( speedSupplier.get() );
    }

    @Override
    public void end( boolean interrupted ) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
