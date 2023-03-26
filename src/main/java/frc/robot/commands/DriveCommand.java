package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.config.RobotConfig;
import frc.robot.subsystems.RobotDrive;

public class DriveCommand extends CommandBase{
    private final RobotDrive       robotDrive;
    private final Supplier<Double> leftXAxisSpeedSupplier;
    private final Supplier<Double> leftYAxisSpeedSupplier;
    private final Supplier<Double> rightXAxisSpeedSupplier;
    private final Supplier<Double> rightYAxisSpeedSupplier;

    // private final SlewRateLimiter arcadeXLimiter;
    // private final SlewRateLimiter arcadeYLimiter;
    // private final SlewRateLimiter tankLeftLimiter;
    // private final SlewRateLimiter tankRightLimiter;
    // private final SlewRateLimiter curvatureXLimiter;
    // private final SlewRateLimiter curvatureYLimiter;

    public DriveCommand(
        RobotDrive       robotDrive,
        Supplier<Double> leftXAxisSpeedSupplier,
        Supplier<Double> leftYAxisSpeedSupplier,
        Supplier<Double> rightXAxisSpeedSupplier,
        Supplier<Double> rightYAxisSpeedSupplier )
    {
        this.robotDrive = robotDrive;
        this.leftXAxisSpeedSupplier  = leftXAxisSpeedSupplier;
        this.leftYAxisSpeedSupplier  = leftYAxisSpeedSupplier;
        this.rightXAxisSpeedSupplier = rightXAxisSpeedSupplier;
        this.rightYAxisSpeedSupplier = rightYAxisSpeedSupplier;
        
        addRequirements( robotDrive );
    }

    @Override
    public void initialize() {}

    @Override
    public void execute()
    {

        double leftXAxisSpeed  = leftXAxisSpeedSupplier.get();
        double leftYAxisSpeed  = leftYAxisSpeedSupplier.get();
        double rightXAxisSpeed = rightXAxisSpeedSupplier.get();
        double rightYAxisSpeed = rightYAxisSpeedSupplier.get();

        leftXAxisSpeed  = ( Math.abs( leftXAxisSpeed ) > RobotConfig.JOYSTICK_DEAD_BAND ) ? leftXAxisSpeed : RobotConfig.ZERO_SPEED;
        leftYAxisSpeed  = ( Math.abs( leftYAxisSpeed ) > RobotConfig.JOYSTICK_DEAD_BAND ) ? leftYAxisSpeed : RobotConfig.ZERO_SPEED;
        rightXAxisSpeed = ( Math.abs( rightXAxisSpeed ) > RobotConfig.JOYSTICK_DEAD_BAND ) ? rightXAxisSpeed : RobotConfig.ZERO_SPEED;
        rightYAxisSpeed = ( Math.abs( rightYAxisSpeed ) > RobotConfig.JOYSTICK_DEAD_BAND ) ? rightYAxisSpeed : RobotConfig.ZERO_SPEED;

        robotDrive.drive( leftXAxisSpeed,
                          leftYAxisSpeed,
                          rightXAxisSpeed,
                          rightYAxisSpeed );
    }

    @Override
    public void end( boolean interrupted ) {}

    @Override
    public boolean isFinished() {
        return false;
    }
}
