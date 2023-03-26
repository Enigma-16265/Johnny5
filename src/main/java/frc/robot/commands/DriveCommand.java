package frc.robot.commands;

import java.util.function.Supplier;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.config.RobotConfig;
import frc.robot.config.RobotConfig.DriveCommandConfig;
import frc.robot.subsystems.RobotDrive;
import frc.robot.subsystems.RobotDriveListener;
import frc.robot.subsystems.RobotDrive.DriveMode;

public class DriveCommand extends CommandBase
                          implements RobotDriveListener
{
    private final RobotDrive       robotDrive;
    private final Supplier<Double> leftXAxisSpeedSupplier;
    private final Supplier<Double> leftYAxisSpeedSupplier;
    private final Supplier<Double> rightXAxisSpeedSupplier;
    private final Supplier<Double> rightYAxisSpeedSupplier;

    private final SlewRateLimiter arcadeSpeedLimiter;
    private final SlewRateLimiter arcadeRotationLimiter;
    private final SlewRateLimiter tankSpeedLeftLimiter;
    private final SlewRateLimiter tankSpeedRightLimiter;
    private final SlewRateLimiter curvatureSpeedLimiter;
    private final SlewRateLimiter curvatureRotationLimiter;

    private DriveMode mode;
    private Boolean   dirtyMode;

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
        
        mode      = robotDrive.getMode();
        dirtyMode = false;

        arcadeSpeedLimiter       = 
            new SlewRateLimiter( RobotConfig.DriveCommandConfig.ARCADE_SPEED_ACCEL_LIMIT_UNITS_PER_SEC );
        arcadeRotationLimiter    =
            new SlewRateLimiter( RobotConfig.DriveCommandConfig.ARCADE_ROTATION_SPEED_ACCEL_LIMIT_UNITS_PER_SEC );
        tankSpeedLeftLimiter     =
            new SlewRateLimiter( RobotConfig.DriveCommandConfig.TANK_SPEED_ACCEL_LIMIT_UNITS_PER_SEC );
        tankSpeedRightLimiter    =
            new SlewRateLimiter( RobotConfig.DriveCommandConfig.TANK_SPEED_ACCEL_LIMIT_UNITS_PER_SEC );
        curvatureSpeedLimiter    =
            new SlewRateLimiter( RobotConfig.DriveCommandConfig.CURVATURE_SPEED_ACCEL_LIMIT_UNITS_PER_SEC );
        curvatureRotationLimiter =
            new SlewRateLimiter( RobotConfig.DriveCommandConfig.CURVATURE_ROTATION_SPEED_ACCEL_LIMIT_UNITS_PER_SEC );

        addRequirements( robotDrive );
    }

    @Override
    public void initialize()
    {
        robotDrive.setListener( this );
    }

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

        if ( dirtyMode )
        {
            dirtyMode = false;

            arcadeSpeedLimiter.reset( RobotConfig.ZERO_SLEW_RATE );
            arcadeRotationLimiter.reset( RobotConfig.ZERO_SLEW_RATE );
            tankSpeedLeftLimiter.reset( RobotConfig.ZERO_SLEW_RATE );
            tankSpeedRightLimiter.reset( RobotConfig.ZERO_SLEW_RATE );
            curvatureSpeedLimiter.reset( RobotConfig.ZERO_SLEW_RATE );
            curvatureRotationLimiter.reset( RobotConfig.ZERO_SLEW_RATE );
        }
        
        switch( mode )
        {
            case ARCADE:
            default:
            {
                if ( ( DriveCommandConfig.ARCADE_ORIENTATION & 
                       DriveCommandConfig.ARCADE_ORIENTATION_INVERT_SPEED_MASK ) ==
                     DriveCommandConfig.ARCADE_ORIENTATION_INVERT_SPEED_MASK        )
                {
                    leftYAxisSpeed = -leftYAxisSpeed;
                }

                if ( ( DriveCommandConfig.ARCADE_ORIENTATION & 
                       DriveCommandConfig.ARCADE_ORIENTATION_INVERT_ROTATION_MASK ) ==
                     DriveCommandConfig.ARCADE_ORIENTATION_INVERT_ROTATION_MASK        )
                {
                    rightXAxisSpeed = -rightXAxisSpeed;
                }

                if ( DriveCommandConfig.ARCADE_SPEED_ACCEL_LIMIT_UNITS_PER_SEC != RobotConfig.ZERO_RATE_LIMIT )
                {
                    leftYAxisSpeed = arcadeSpeedLimiter.calculate( leftYAxisSpeed );
                }

                if ( DriveCommandConfig.ARCADE_ROTATION_SPEED_ACCEL_LIMIT_UNITS_PER_SEC != RobotConfig.ZERO_RATE_LIMIT )
                {
                    rightXAxisSpeed = arcadeRotationLimiter.calculate( rightXAxisSpeed );
                }

                robotDrive.drive( leftYAxisSpeed, rightXAxisSpeed );
            }
                break;
            case TANK:
            {

                if ( ( DriveCommandConfig.TANK_ORIENTATION & 
                       DriveCommandConfig.TANK_ORIENTATION_INVERT_LEFT_SPEED_MASK ) ==
                     DriveCommandConfig.TANK_ORIENTATION_INVERT_LEFT_SPEED_MASK        )
                {
                    leftYAxisSpeed  = -leftYAxisSpeed;
                }

                if ( ( DriveCommandConfig.TANK_ORIENTATION & 
                       DriveCommandConfig.TANK_ORIENTATION_INVERT_RIGHT_SPEED_MASK ) ==
                     DriveCommandConfig.TANK_ORIENTATION_INVERT_RIGHT_SPEED_MASK        )
                {
                    rightYAxisSpeed = -rightYAxisSpeed;
                }

                if ( DriveCommandConfig.TANK_SPEED_ACCEL_LIMIT_UNITS_PER_SEC != RobotConfig.ZERO_RATE_LIMIT )
                {
                    leftYAxisSpeed  = tankSpeedLeftLimiter.calculate( leftYAxisSpeed );
                    rightYAxisSpeed = tankSpeedRightLimiter.calculate( rightYAxisSpeed );
                }

                robotDrive.drive( leftYAxisSpeed, rightYAxisSpeed );
            }
                break;
            case CURVATURE:
            {

                if ( ( DriveCommandConfig.CURVATURE_ORIENTATION & 
                       DriveCommandConfig.CURVATURE_ORIENTATION_INVERT_ROTATION_MASK ) ==
                     DriveCommandConfig.CURVATURE_ORIENTATION_INVERT_ROTATION_MASK        )
                {
                    leftYAxisSpeed = -leftYAxisSpeed;
                }

                if ( ( DriveCommandConfig.CURVATURE_ORIENTATION & 
                       DriveCommandConfig.CURVATURE_ORIENTATION_INVERT_SPEED_MASK ) ==
                     DriveCommandConfig.CURVATURE_ORIENTATION_INVERT_SPEED_MASK        )
                {
                    rightXAxisSpeed = -rightXAxisSpeed;
                }

                if ( DriveCommandConfig.CURVATURE_SPEED_ACCEL_LIMIT_UNITS_PER_SEC != RobotConfig.ZERO_RATE_LIMIT )
                {
                    leftYAxisSpeed = curvatureSpeedLimiter.calculate( leftYAxisSpeed );
                }

                if ( DriveCommandConfig.CURVATURE_ROTATION_SPEED_ACCEL_LIMIT_UNITS_PER_SEC != RobotConfig.ZERO_RATE_LIMIT )
                {
                    rightXAxisSpeed = curvatureRotationLimiter.calculate (rightXAxisSpeed );
                }

                robotDrive.drive( leftYAxisSpeed, rightXAxisSpeed );
            }
                break;
        }

    }

    @Override
    public void end( boolean interrupted )
    {
        robotDrive.setListener( null );
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    public void driveModeUpdate( DriveMode mode )
    {
        if ( this.mode != mode )
        {
            this.mode = mode;
            dirtyMode = true;
        }
    }

}
