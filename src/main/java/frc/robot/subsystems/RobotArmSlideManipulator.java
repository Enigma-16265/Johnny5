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
    public static final double ARM_SLIDE_SCALE_FACTOR             = 0.10;
    public static final double ENCODER_POSITION_CONVERSION_FACTOR = 0.0625;
    public static final double PULLY_CIRCUMFERENCE                = Math.PI * 1.5; //inches

    private CANSparkMax     armSlide          = new CANSparkMax( ARM_SLIDE_CAN_ID, MotorType.kBrushless );
    private RelativeEncoder armSlideEncoder   = armSlide.getEncoder();
 
    public RobotArmSlideManipulator()
    {
        armSlideEncoder.setPosition( 0 );
        armSlideEncoder.setPositionConversionFactor( ENCODER_POSITION_CONVERSION_FACTOR );
    }

    public void slide( double speed ) {

        double rotationDistance = armSlideEncoder.getPosition() * PULLY_CIRCUMFERENCE;
        
        log.trace( "Slide position: {} velocity: {}", armSlideEncoder.getPosition(), armSlideEncoder.getVelocity()  );
        log.trace( "rotationDistance: {}", rotationDistance );

        double scaledSpeed = speed * ARM_SLIDE_SCALE_FACTOR;

        log.trace( "Setting Speed: " + scaledSpeed );

        armSlide.set( scaledSpeed );
    }

    public void simulationInit()
    {
        log.info( "subsystem sim init" );
        REVPhysicsSim.getInstance().addSparkMax( armSlide, DCMotor.getNEO( 1 ) );
    }

}
