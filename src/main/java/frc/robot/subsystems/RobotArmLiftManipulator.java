package frc.robot.subsystems;

import java.util.Map;

import com.revrobotics.CANSparkMax;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.logging.DataNetworkTableLog;
import frc.robot.logging.Log;
import frc.robot.logging.LogManager;

public class RobotArmLiftManipulator extends SubsystemBase {

    private static final Log log = LogManager.getLogger( "subsystems.ArmLift" );

    private static final DataNetworkTableLog dataLog =
        new DataNetworkTableLog( 
            "subsystems.ArmLift",
            Map.of( "position",         DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "velocity",         DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "rotationDistance", DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "speed",            DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "scaledSpeed",      DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "limitDown",        DataNetworkTableLog.COLUMN_TYPE.BYTE,
                    "limitUp",          DataNetworkTableLog.COLUMN_TYPE.BYTE ) );

    public static final int     ARM_LIFT_CAN_ID                    = 16;
    public static final double  ARM_LIFT_SCALE_FACTOR              = 0.60;
    public static final double  ENCODER_POSITION_CONVERSION_FACTOR = 0.01;
    public static final double  PULLY_CIRCUMFERENCE                = Math.PI * 4.25; // inches
    public static final double  MIN_ROTATION_DISTANCE              = 0.0;
    public static final double  MAX_ROTATION_DISTANCE              = PULLY_CIRCUMFERENCE * 3;
    public static final boolean ENFORCE_LIMITS                     = true;
    
    public static final double  EPLISON_DIST                       = 0.1;
    public static final double  EPLISON_SPEED                      = 0.001;
    public static final double  ZERO_POSITION                      = 0.0;
    public static final double  ZERO_SPEED                         = 0.0;
    public static final double  ZERO_DISTANCE                      = 0.0;

    private CANSparkMax     armLift           = new CANSparkMax( ARM_LIFT_CAN_ID, MotorType.kBrushless );
    private RelativeEncoder armLiftEncoder    = armLift.getEncoder();

    public RobotArmLiftManipulator()
    {

        // Invert the motor so positive speed lowers the lift
        armLift.setInverted( true );

        armLiftEncoder.setPosition( ZERO_POSITION );
        armLiftEncoder.setPositionConversionFactor( ENCODER_POSITION_CONVERSION_FACTOR );
    }

    private int cnt = 1;
    public void lift( double speed )
    {
        double rotationDistance = armLiftEncoder.getPosition() * PULLY_CIRCUMFERENCE;
        double scaledSpeed      = speed * ARM_LIFT_SCALE_FACTOR;
        
        if ( ( cnt % 10 ) == 0 )
        {
            dataLog.publish( "position", armLiftEncoder.getPosition() );
            dataLog.publish( "velocity", armLiftEncoder.getVelocity() );
            dataLog.publish( "rotationDistance", rotationDistance );
            dataLog.publish( "speed", speed );
            dataLog.publish( "scaledSpeed", scaledSpeed );

            log.trace( "speed: " + speed + " rotationDistance: " + rotationDistance );
        }
        cnt++;

        if ( ENFORCE_LIMITS )
        {

            if ( ( Math.abs( speed ) - ZERO_SPEED ) > EPLISON_SPEED )
            {

                if ( speed < ZERO_SPEED )
                {
                    if ( Math.abs( MIN_ROTATION_DISTANCE - Math.abs( rotationDistance ) ) > EPLISON_DIST )
                    {
                        log.info( "Lift Back" );
                        armLift.set( scaledSpeed );
                    }
                    else
                    {
                        log.info( "Limit Lift Back" );
                        armLift.set( ZERO_SPEED );
                    }
                }
                else
                {
                    if ( Math.abs( MAX_ROTATION_DISTANCE - Math.abs( rotationDistance ) ) > EPLISON_DIST )
                    {
                        log.info( "Lift Forward" );
                        armLift.set( scaledSpeed );
                    }
                    else
                    {
                        log.info( "Limit Lift Forward" );
                        armLift.set( ZERO_SPEED );
                    }
                }

            }
            else
            {
                armLift.set( ZERO_SPEED );
            }

        }
        else
        {
            armLift.set( scaledSpeed );
        }

    }

    public void simulationInit()
    {
        log.info( "subsystem sim init" );
        REVPhysicsSim.getInstance().addSparkMax( armLift, DCMotor.getNEO( 1 ) );
    }

}
