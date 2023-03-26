package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.config.RobotConfig;
import frc.robot.subsystems.RobotArmSlideManipulator;

public class ArmSlideCommand extends CommandBase{
    private final RobotArmSlideManipulator manipulator;
    private final Supplier<Double>         speedSupplier;

    public ArmSlideCommand(
        RobotArmSlideManipulator manipulator,
        Supplier<Double>         speedSupplier )
    {
        this.manipulator   = manipulator;
        this.speedSupplier = speedSupplier;

        addRequirements( manipulator );
    }

    @Override
    public void initialize() {}

    @Override
    public void execute()
    {
        double speed = speedSupplier.get();
        
        speed = ( Math.abs( speed ) > RobotConfig.JOYSTICK_DEAD_BAND ) ? speed : RobotConfig.ZERO_SPEED;

        manipulator.slide( speed );
    }

    @Override
    public void end( boolean interrupted ) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
