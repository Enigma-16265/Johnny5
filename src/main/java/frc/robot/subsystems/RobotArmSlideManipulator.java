package frc.robot.subsystems;

import java.util.Map;

import com.revrobotics.CANSparkMax;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.config.RobotConfig;
import frc.robot.config.RobotConfig.ArmSlideConfig;
import frc.robot.logging.DataNetworkTableLog;
import frc.robot.logging.Log;
import frc.robot.logging.LogManager;

public class RobotArmSlideManipulator extends SubsystemBase
{
    private static final Log log = LogManager.getLogger( "subsystems.ArmSlide" );

    enum SLIDE_STATE {
        OPEN,
        IN_LIMIT,
        IN,
        NEUTRAL,
        OUT,
        OUT_LIMIT
    }

    private static final DataNetworkTableLog dataLog =
        new DataNetworkTableLog( 
            "subsystems.ArmSlide",
            Map.of( "position",         DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "velocity",         DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "rotationDistance", DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "speed",            DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "scaledSpeed",      DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "slideState",       DataNetworkTableLog.COLUMN_TYPE.STRING ) );

    public static final int CAN_ID = 17;

    private CANSparkMax     armSlide        = new CANSparkMax( CAN_ID, MotorType.kBrushless );
    private RelativeEncoder armSlideEncoder = armSlide.getEncoder();
 
    private boolean         enforceLimits   = ArmSlideConfig.ENFORCE_LIMITS_DEFAULT;

    public RobotArmSlideManipulator()
    {
        armSlideEncoder.setPosition( RobotConfig.ZERO_POSITION );
        armSlideEncoder.setPositionConversionFactor( ArmSlideConfig.ENCODER_POSITION_CONVERSION_FACTOR );
    }

    private int cnt = 1;
    public void slide( double speed )
    {
        double rotationDistance = armSlideEncoder.getPosition() * ArmSlideConfig.PULLY_CIRCUMFERENCE;
        double scaledSpeed      = speed * ArmSlideConfig.SPEED_SCALE_FACTOR;
        
        if ( ( cnt % 10 ) == 0 )
        {
            log.trace( "speed: " + speed + " rotationDistance: " + rotationDistance );
        }
        cnt++;

        dataLog.publish( "position",         armSlideEncoder.getPosition() );
        dataLog.publish( "velocity",         armSlideEncoder.getVelocity() );
        dataLog.publish( "rotationDistance", rotationDistance );
        dataLog.publish( "speed",            speed );
        dataLog.publish( "scaledSpeed",      scaledSpeed );

        if ( enforceLimits )
        {

            if ( speed < RobotConfig.ZERO_SPEED )
            {
                if ( Math.abs( rotationDistance - ArmSlideConfig.MIN_ROTATION_DISTANCE ) > RobotConfig.EPLISON_DIST )
                {
                    log.trace( "Slide In" );
                    dataLog.publish( "slideState", SLIDE_STATE.IN.name() );
                    armSlide.set( scaledSpeed );
                }
                else
                {
                    log.trace( "Limit Slide In" );
                    dataLog.publish( "slideState", SLIDE_STATE.IN_LIMIT.name() );
                    armSlide.set( RobotConfig.ZERO_SPEED );
                }
            } else
            if ( speed > RobotConfig.ZERO_SPEED )
            {
                if ( ArmSlideConfig.MAX_ROTATION_DISTANCE >= rotationDistance )
                {
                    log.trace( "Slide Out" );
                    dataLog.publish( "slideState", SLIDE_STATE.OUT.name() );
                    armSlide.set( scaledSpeed );
                }
                else
                {
                    log.trace( "Limit Slide Out" );
                    dataLog.publish( "slideState", SLIDE_STATE.OUT_LIMIT.name() );
                    armSlide.set( RobotConfig.ZERO_SPEED );
                }
            }
            else
            {
                dataLog.publish( "slideState", SLIDE_STATE.NEUTRAL.name() );
                armSlide.set( RobotConfig.ZERO_SPEED );
            }

        }
        else
        {
            dataLog.publish( "slideState", SLIDE_STATE.OPEN.name() );
            armSlide.set( scaledSpeed );
        }

    }

    public void simulationInit()
    {
        log.info( "subsystem sim init" );
        REVPhysicsSim.getInstance().addSparkMax( armSlide, DCMotor.getNEO( 1 ) );
    }

    public void resetEncoder()
    {
        armSlideEncoder.setPosition( RobotConfig.ZERO_POSITION );
    }

    public void enforceLimitToggle()
    {
        if ( enforceLimits )
        {
            enforceLimits = false;
        }
        else
        {
            enforceLimits = true;
        }
    }

}
