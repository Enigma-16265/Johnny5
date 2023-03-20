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

    private static final Log log = LogManager.getLogger( LogManager.Type.NETWORK_TABLES, "subsystems.ArmLift" );

    private static final DataNetworkTableLog dataLog =
        new DataNetworkTableLog( 
            "subsystems.ArmLift",
            Map.of( "position",         DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "velocity",         DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "rotationDistance", DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "speed",            DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "scaledSpeed",      DataNetworkTableLog.COLUMN_TYPE.DOUBLE ) );

    public static final int     ARM_LIFT_CAN_ID                    = 16;
    public static final double  ARM_LIFT_SCALE_FACTOR              = 0.60;
    public static final double  ENCODER_POSITION_CONVERSION_FACTOR = 0.01;
    public static final double  PULLY_CIRCUMFERENCE                = Math.PI * 4.25; // inches
    public static final double  MAX_ROTATION_DISTANCE              = PULLY_CIRCUMFERENCE * 3;
    public static final boolean ENFORCE_LIMITS                     = false;

    private CANSparkMax     armLift           = new CANSparkMax( ARM_LIFT_CAN_ID, MotorType.kBrushless );
    private RelativeEncoder armLiftEncoder    = armLift.getEncoder();

    public RobotArmLiftManipulator()
    {
        armLiftEncoder.setPosition( 0 );
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
        }
        cnt++;

        boolean allowSet = true;
        if ( ENFORCE_LIMITS )
        {

            allowSet = false;
            if ( speed < 0.0 )
            {
                if ( rotationDistance >= 0 )
                {
                    allowSet = true;
                }
                else
                {
                    log.debug( "Negative set Disallowed" );
                }
            }
            else
            {
                if ( rotationDistance <= MAX_ROTATION_DISTANCE )
                {
                    allowSet = true;
                }
                else
                {
                    log.debug( "Positive set Disallowed" );
                }
            }

        }

        if ( allowSet )
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
