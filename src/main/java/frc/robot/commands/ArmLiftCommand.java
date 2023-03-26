package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.config.RobotConfig;
import frc.robot.config.RobotConfig.ArmLiftCommandConfig;
import frc.robot.subsystems.RobotArmLiftManipulator;

public class ArmLiftCommand extends CommandBase{
    private final RobotArmLiftManipulator manipulator;
    private final Supplier<Double>        speedSupplier;
    private final SlewRateLimiter         limiter;

    public ArmLiftCommand(
        RobotArmLiftManipulator manipulator,
        Supplier<Double>        speedSupplier )
    {
        this.manipulator   = manipulator;
        this.speedSupplier = speedSupplier;
        
        limiter = new SlewRateLimiter( ArmLiftCommandConfig.SPEED_ACCEL_LIMIT_UNITS_PER_SEC );

        addRequirements( manipulator );
    }

    @Override
    public void initialize() {}

    @Override
    public void execute()
    {
        double speed = speedSupplier.get();

        speed = ( Math.abs( speed ) > RobotConfig.JOYSTICK_DEAD_BAND ) ? speed : RobotConfig.ZERO_SPEED;

        if ( ArmLiftCommandConfig.SPEED_ACCEL_LIMIT_UNITS_PER_SEC != RobotConfig.ZERO_RATE_LIMIT )
        {
            speed = limiter.calculate( speed );
        }

        manipulator.lift( speed );
    }

    @Override
    public void end( boolean interrupted ) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
