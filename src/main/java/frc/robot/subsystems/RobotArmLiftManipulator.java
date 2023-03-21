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
    
    public static final double  EPLISON                            = 0.01;

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

        if ( ENFORCE_LIMITS )
        {

            if ( speed < 0.0 )
            {
                if ( Math.abs( rotationDistance - 0.0 ) > EPLISON )
                {
                    armLift.set( scaledSpeed );
                }
                else
                {
                    armLift.set( 0.0 );
                }
            }
            else
            {
                if ( Math.abs( MAX_ROTATION_DISTANCE - rotationDistance ) < EPLISON )
                {
                    armLift.set( scaledSpeed );
                }
                else
                {
                    armLift.set( 0 );
                }
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
