package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.config.RobotConfig;
import frc.robot.config.RobotConfig.TurretSpinCommandConfig;
import frc.robot.subsystems.RobotTurretSpinManipulator;

public class TurretSpinCommand extends CommandBase
{
    private final RobotTurretSpinManipulator manipulator;
    private final Supplier<Double>           speedSupplier;
    private final SlewRateLimiter            limiter;

    public TurretSpinCommand(
        RobotTurretSpinManipulator manipulator,
        Supplier<Double>           speedSupplier )
    {
        this.manipulator   = manipulator;
        this.speedSupplier = speedSupplier;

        limiter = new SlewRateLimiter( TurretSpinCommandConfig.SPEED_ACCEL_LIMIT_UNITS_PER_SEC );
        
        addRequirements( manipulator );
    }

    @Override
    public void initialize() {}

    @Override
    public void execute()
    {
        double speed = speedSupplier.get();
        
        speed = ( Math.abs( speed ) > RobotConfig.JOYSTICK_DEAD_BAND ) ? speed : RobotConfig.ZERO_SPEED;

        if ( TurretSpinCommandConfig.SPEED_ACCEL_LIMIT_UNITS_PER_SEC != RobotConfig.ZERO_RATE_LIMIT )
        {
            speed = limiter.calculate( speed );
        }

        manipulator.spin( speed );
    }

    @Override
    public void end( boolean interrupted ) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
