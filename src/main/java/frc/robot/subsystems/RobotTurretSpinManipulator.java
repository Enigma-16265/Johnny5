package frc.robot.subsystems;

import java.util.Map;

import com.revrobotics.CANSparkMax;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.config.RobotConfig;
import frc.robot.logging.DataNetworkTableLog;
import frc.robot.logging.Log;
import frc.robot.logging.LogManager;

public class RobotTurretSpinManipulator extends SubsystemBase
{
    private static final Log log = LogManager.getLogger( "subsystems.TurretSpin" );

    enum SPIN_STATE {
        OPEN,
        LEFT_LIMIT,
        LEFT,
        NEUTRAL,
        RIGHT,
        RIGHT_LIMIT
    }

    private static final DataNetworkTableLog dataLog =
        new DataNetworkTableLog( 
            "subsystems.TurretSpin",
            Map.of( "position",         DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "velocity",         DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "rotationDistance", DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "speed",            DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "scaledSpeed",      DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "spinState",        DataNetworkTableLog.COLUMN_TYPE.STRING ) );

    public static final int     TURRET_SPIN_CAN_ID                 = 15;
    public static final double  TURRET_SPIN_SCALE_FACTOR           = 0.20;
    public static final double  ENCODER_POSITION_CONVERSION_FACTOR = 0.25;
    public static final double  PULLY_CIRCUMFERENCE                = Math.PI * 1.0; // inches
    public static final double  MIN_ROTATION_DISTANCE              = -1.0;
    public static final double  MAX_ROTATION_DISTANCE              = 1.0;
    public static final boolean ENFORCE_LIMITS_DEFAULT             = true;

    private CANSparkMax     turretSpin        = new CANSparkMax( TURRET_SPIN_CAN_ID, MotorType.kBrushless );
    private RelativeEncoder turretSpinEncoder = turretSpin.getEncoder();

    private boolean         enforceLimits     = ENFORCE_LIMITS_DEFAULT;

    public RobotTurretSpinManipulator()
    {
        turretSpinEncoder.setPosition( RobotConfig.ZERO_POSITION );
        turretSpinEncoder.setPositionConversionFactor( ENCODER_POSITION_CONVERSION_FACTOR );
    }
    
    private int cnt = 1;
    public void spin( double speed )
    {
        double rotationDistance = turretSpinEncoder.getPosition() * PULLY_CIRCUMFERENCE;
        double scaledSpeed      = speed * TURRET_SPIN_SCALE_FACTOR;
        
        if ( ( cnt % 10 ) == 0 )
        {
            log.trace( "speed: " + speed + " rotationDistance: " + rotationDistance );
        }
        cnt++;

        dataLog.publish( "position",         turretSpinEncoder.getPosition() );
        dataLog.publish( "velocity",         turretSpinEncoder.getVelocity() );
        dataLog.publish( "rotationDistance", rotationDistance );
        dataLog.publish( "speed",            speed );
        dataLog.publish( "scaledSpeed",      scaledSpeed );

        if ( enforceLimits )
        {

            if ( speed < RobotConfig.ZERO_SPEED )
            {
                if ( MIN_ROTATION_DISTANCE <= rotationDistance )
                {
                    log.trace( "Spin Left" );
                    dataLog.publish( "spinState", SPIN_STATE.LEFT.name() );
                    turretSpin.set( scaledSpeed );
                }
                else
                {
                    log.trace( "Limit Spin Left" );
                    dataLog.publish( "spinState", SPIN_STATE.LEFT_LIMIT.name() );
                    turretSpin.set( RobotConfig.ZERO_SPEED );
                }
            }
            else
            if ( speed > RobotConfig.ZERO_SPEED )
            {
                if ( MAX_ROTATION_DISTANCE >= rotationDistance )
                {
                    log.trace( "Spin Right" );
                    dataLog.publish( "spinState", SPIN_STATE.RIGHT.name() );
                    turretSpin.set( scaledSpeed );
                }
                else
                {
                    log.trace( "Limit Spin Right" );
                    dataLog.publish( "spinState", SPIN_STATE.RIGHT_LIMIT.name() );
                    turretSpin.set( RobotConfig.ZERO_SPEED );
                }
            }
            else
            {
                dataLog.publish( "spinState", SPIN_STATE.NEUTRAL.name() );
                turretSpin.set( RobotConfig.ZERO_SPEED );
            }

        }
        else
        {
            dataLog.publish( "spinState", SPIN_STATE.OPEN.name() );
            turretSpin.set( scaledSpeed );
        }

    }

    public void simulationInit()
    {
        log.info( "subsystem sim init" );
        REVPhysicsSim.getInstance().addSparkMax( turretSpin, DCMotor.getNEO( 1 ) );
    }

    public void resetEncoder()
    {
        turretSpinEncoder.setPosition( RobotConfig.ZERO_POSITION );
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
