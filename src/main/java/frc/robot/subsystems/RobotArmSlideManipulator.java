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

public class RobotArmSlideManipulator extends SubsystemBase
{
    private static final Log log = LogManager.getLogger( LogManager.Type.NETWORK_TABLES, "subsystems.ArmSlide" );

    private static final DataNetworkTableLog dataLog =
        new DataNetworkTableLog( 
            "subsystems.ArmSlide",
            Map.of( "position",         DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "velocity",         DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "rotationDistance", DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "speed",            DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "scaledSpeed",      DataNetworkTableLog.COLUMN_TYPE.DOUBLE ) );

    public static final int     ARM_SLIDE_CAN_ID                   = 17;
    public static final double  ARM_SLIDE_SCALE_FACTOR             = 0.50;
    public static final double  ENCODER_POSITION_CONVERSION_FACTOR = 0.0625;
    public static final double  PULLY_CIRCUMFERENCE                = Math.PI * 1.5; //inches
    public static final double  MAX_ROTATION_DISTANCE              = PULLY_CIRCUMFERENCE * 6;
    public static final boolean ENFORCE_LIMITS                     = false;

    public static final double  EPLISON                            = 0.01;

    private CANSparkMax     armSlide          = new CANSparkMax( ARM_SLIDE_CAN_ID, MotorType.kBrushless );
    private RelativeEncoder armSlideEncoder   = armSlide.getEncoder();
 
    public RobotArmSlideManipulator()
    {
        armSlideEncoder.setPosition( 0 );
        armSlideEncoder.setPositionConversionFactor( ENCODER_POSITION_CONVERSION_FACTOR );
    }

    private int cnt = 1;
    public void slide( double speed )
    {
        double rotationDistance = armSlideEncoder.getPosition() * PULLY_CIRCUMFERENCE;
        double scaledSpeed      = speed * ARM_SLIDE_SCALE_FACTOR;
        
        if ( ( cnt % 10 ) == 0 )
        {
            dataLog.publish( "position",         armSlideEncoder.getPosition() );
            dataLog.publish( "velocity",         armSlideEncoder.getVelocity() );
            dataLog.publish( "rotationDistance", rotationDistance );
            dataLog.publish( "speed",            speed );
            dataLog.publish( "scaledSpeed",      scaledSpeed );
        }
        cnt++;

        if ( ENFORCE_LIMITS )
        {

            if ( speed < 0.0 )
            {
                if ( Math.abs( rotationDistance - 0.0 ) > EPLISON )
                {
                    armSlide.set( scaledSpeed );
                }
                else
                {
                    armSlide.set( 0.0 );
                }
            }
            else
            {
                if ( Math.abs( MAX_ROTATION_DISTANCE - rotationDistance ) < EPLISON )
                {
                    armSlide.set( scaledSpeed );
                }
                else
                {
                    armSlide.set( 0 );
                }
            }

        }
        else
        {
            armSlide.set( scaledSpeed );
        }

    }

    public void simulationInit()
    {
        log.info( "subsystem sim init" );
        REVPhysicsSim.getInstance().addSparkMax( armSlide, DCMotor.getNEO( 1 ) );
    }

}
