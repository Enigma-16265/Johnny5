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

    public static final int     ARM_SLIDE_CAN_ID                   = 17;
    public static final double  ARM_SLIDE_SCALE_FACTOR             = 0.50;
    public static final double  ENCODER_POSITION_CONVERSION_FACTOR = 0.0625;
    public static final double  PULLY_CIRCUMFERENCE                = Math.PI * 1.5; //inches
    public static final double  MIN_ROTATION_DISTANCE              = 0.0;
    public static final double  MAX_ROTATION_DISTANCE              = PULLY_CIRCUMFERENCE * 6;
    public static final boolean ENFORCE_LIMITS                     = false;
    
    public static final double  EPLISON_DIST                       = 0.1;
    public static final double  EPLISON_SPEED                      = 0.001;
    public static final double  ZERO_POSITION                      = 0.0;
    public static final double  ZERO_SPEED                         = 0.0;
    public static final double  ZERO_DISTANCE                      = 0.0;

    private CANSparkMax     armSlide          = new CANSparkMax( ARM_SLIDE_CAN_ID, MotorType.kBrushless );
    private RelativeEncoder armSlideEncoder   = armSlide.getEncoder();
 
    public RobotArmSlideManipulator()
    {
        armSlideEncoder.setPosition( ZERO_POSITION );
        armSlideEncoder.setPositionConversionFactor( ENCODER_POSITION_CONVERSION_FACTOR );
    }

    private int cnt = 1;
    public void slide( double speed )
    {
        double rotationDistance = armSlideEncoder.getPosition() * PULLY_CIRCUMFERENCE;
        double scaledSpeed      = speed * ARM_SLIDE_SCALE_FACTOR;
        
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

        if ( ENFORCE_LIMITS )
        {

            if ( ( Math.abs( speed ) - ZERO_SPEED ) > EPLISON_SPEED )
            {

                if ( speed < ZERO_SPEED )
                {
                    if ( Math.abs( MIN_ROTATION_DISTANCE - Math.abs( rotationDistance ) ) > EPLISON_DIST )
                    {
                        log.trace( "Slide In" );
                        dataLog.publish( "slideState", SLIDE_STATE.IN.name() );
                        armSlide.set( scaledSpeed );
                    }
                    else
                    {
                        log.trace( "Limit Slide In" );
                        dataLog.publish( "slideState", SLIDE_STATE.IN_LIMIT.name() );
                        armSlide.set( ZERO_SPEED );
                    }
                }
                else
                {
                    if ( Math.abs( MAX_ROTATION_DISTANCE - Math.abs( rotationDistance ) ) > EPLISON_DIST )
                    {
                        log.trace( "Slide Out" );
                        dataLog.publish( "slideState", SLIDE_STATE.OUT.name() );
                        armSlide.set( scaledSpeed );
                    }
                    else
                    {
                        log.trace( "Limit Slide Out" );
                        dataLog.publish( "slideState", SLIDE_STATE.OUT_LIMIT.name() );
                        armSlide.set( ZERO_SPEED );
                    }
                }

            }
            else
            {
                dataLog.publish( "slideState", SLIDE_STATE.NEUTRAL.name() );
                armSlide.set( ZERO_SPEED );
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

}
