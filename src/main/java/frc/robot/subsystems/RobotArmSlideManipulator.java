package frc.robot.subsystems;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revrobotics.CANSparkMax;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class RobotArmSlideManipulator extends SubsystemBase
{
    private static final Logger log = LogManager.getLogger( RobotArmSlideManipulator.class );

    public static final int    ARM_SLIDE_CAN_ID                   = 17;
    public static final double ARM_SLIDE_SCALE_FACTOR             = 0.50;
    public static final double ENCODER_POSITION_CONVERSION_FACTOR = 0.0625;
    public static final double PULLY_CIRCUMFERENCE                = Math.PI * 1.5; //inches
    public static final double  MAX_ROTATION_DISTANCE             = PULLY_CIRCUMFERENCE * 6;
    public static final boolean ENFORCE_LIMITS                    = false;

    private CANSparkMax     armSlide          = new CANSparkMax( ARM_SLIDE_CAN_ID, MotorType.kBrushless );
    private RelativeEncoder armSlideEncoder   = armSlide.getEncoder();
 
    public RobotArmSlideManipulator()
    {
        armSlideEncoder.setPosition( 0 );
        armSlideEncoder.setPositionConversionFactor( ENCODER_POSITION_CONVERSION_FACTOR );
    }

    private int cnt = 1;
    public void slide( double speed ) {
        double rotationDistance = armSlideEncoder.getPosition() * PULLY_CIRCUMFERENCE;
        double scaledSpeed      = speed * ARM_SLIDE_SCALE_FACTOR;
        
        if ( ( cnt % 50 ) == 0 )
        {
          log.debug( "\n"+
                     "position: {}\n" + 
                     "velocity: {}\n" +
                     "rotationDistance: {}\n" +
                     "speed: {}\n"+
                     "scaledSpeed: {}", 
                     armSlideEncoder.getPosition(), 
                     armSlideEncoder.getVelocity(),
                     rotationDistance,
                     speed,
                     scaledSpeed );
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
            armSlide.set( scaledSpeed );
        }

    }

    public void simulationInit()
    {
        log.info( "subsystem sim init" );
        REVPhysicsSim.getInstance().addSparkMax( armSlide, DCMotor.getNEO( 1 ) );
    }

}
