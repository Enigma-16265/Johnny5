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

public class RobotArmLiftManipulator extends SubsystemBase
{
    private static final Log log = LogManager.getLogger( "subsystems.ArmLift" );

    enum LIFT_STATE {
        OPEN,
        BACK_LIMIT,
        BACK,
        NEUTRAL,
        FORWARD,
        FORWARD_LIMIT
    }

    private static final DataNetworkTableLog dataLog =
        new DataNetworkTableLog( 
            "subsystems.ArmLift",
            Map.of( "position",         DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "velocity",         DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "rotationDistance", DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "speed",            DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "scaledSpeed",      DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "liftState",        DataNetworkTableLog.COLUMN_TYPE.STRING,
                    "absDist",          DataNetworkTableLog.COLUMN_TYPE.DOUBLE ) );

    //Limit switch on DIO 0 (magnetic, WCP, 971)

    public static final int     ARM_LIFT_CAN_ID                    = 16;
    public static final double  ARM_LIFT_SCALE_FACTOR              = 1.00;
    public static final double  ENCODER_POSITION_CONVERSION_FACTOR = 0.01;
    public static final double  PULLY_CIRCUMFERENCE                = Math.PI * 4.25; // inches
    public static final double  MIN_ROTATION_DISTANCE              = 0.0;
    public static final double  MAX_DISTANCE_BACKOFF               = 5.0;
    public static final double  MAX_ROTATION_DISTANCE              = ( PULLY_CIRCUMFERENCE * 3.127 ) - MAX_DISTANCE_BACKOFF; // Target: 41.752739 Circ: 13.351769
    public static final boolean ENFORCE_LIMITS_DEFAULT             = true;

    private CANSparkMax     armLift        = new CANSparkMax( ARM_LIFT_CAN_ID, MotorType.kBrushless );
    private RelativeEncoder armLiftEncoder = armLift.getEncoder();

    private boolean         enforceLimits  = ENFORCE_LIMITS_DEFAULT;

    public RobotArmLiftManipulator()
    {

        // Invert the motor so positive speed lowers the lift
        armLift.setInverted( true );

        armLiftEncoder.setPosition( RobotConfig.ZERO_POSITION );
        armLiftEncoder.setPositionConversionFactor( ENCODER_POSITION_CONVERSION_FACTOR );
    }

    private int cnt = 1;
    public void lift( double speed )
    {
        double rotationDistance = armLiftEncoder.getPosition() * PULLY_CIRCUMFERENCE;
        double scaledSpeed      = speed * ARM_LIFT_SCALE_FACTOR;
        
        if ( ( cnt % 10 ) == 0 )
        {
            log.trace( "speed: " + speed + " rotationDistance: " + rotationDistance );
        }
        cnt++;

        dataLog.publish( "position",         armLiftEncoder.getPosition() );
        dataLog.publish( "velocity",         armLiftEncoder.getVelocity() );
        dataLog.publish( "rotationDistance", rotationDistance );
        dataLog.publish( "speed",            speed );
        dataLog.publish( "scaledSpeed",      scaledSpeed );

        if ( enforceLimits )
        {

            if ( speed < RobotConfig.ZERO_SPEED )
            {
                if ( Math.abs( rotationDistance - MIN_ROTATION_DISTANCE ) > RobotConfig.EPLISON_DIST )
                {
                    log.trace( "Lift Back" );
                    dataLog.publish( "liftState", LIFT_STATE.BACK.name() );
                    armLift.set( scaledSpeed );
                }
                else
                {
                    log.trace( "Limit Lift Back" );
                    dataLog.publish( "liftState", LIFT_STATE.BACK_LIMIT.name() );
                    armLift.set( RobotConfig.ZERO_SPEED );
                }
            } else
            if ( speed > RobotConfig.ZERO_SPEED )
            {
                if ( MAX_ROTATION_DISTANCE >= rotationDistance )
                {
                    log.trace( "Lift Forward" );
                    dataLog.publish( "liftState", LIFT_STATE.FORWARD.name() );
                    armLift.set( scaledSpeed );
                }
                else
                {
                    log.trace( "Limit Lift Forward" );
                    dataLog.publish( "liftState", LIFT_STATE.FORWARD_LIMIT.name() );
                    armLift.set( RobotConfig.ZERO_SPEED );
                }
            }
            else
            {
                dataLog.publish( "liftState", LIFT_STATE.NEUTRAL.name() );
                armLift.set( RobotConfig.ZERO_SPEED );
            }

        }
        else
        {
            dataLog.publish( "liftState", LIFT_STATE.OPEN.name() );
            armLift.set( scaledSpeed );
        }

    }

    public void simulationInit()
    {
        log.info( "subsystem sim init" );
        REVPhysicsSim.getInstance().addSparkMax( armLift, DCMotor.getNEO( 1 ) );
    }

    public void resetEncoder()
    {
        armLiftEncoder.setPosition( RobotConfig.ZERO_POSITION );
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
