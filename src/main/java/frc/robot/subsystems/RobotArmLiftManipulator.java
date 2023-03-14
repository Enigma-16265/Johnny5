package frc.robot.subsystems;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revrobotics.CANSparkMax;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class RobotArmLiftManipulator extends SubsystemBase {

    private static final Logger log = LogManager.getLogger( RobotArmLiftManipulator.class );

    public static final int    ARM_LIFT_CAN_ID                    = 16;
    public static final double ARM_LIFT_SCALE_FACTOR              = 0.10;
    public static final double ENCODER_POSITION_CONVERSION_FACTOR = 0.01;
    public static final double PULLY_CIRCUMFERENCE                = Math.PI * 4.25; // inches

    private CANSparkMax     armLift           = new CANSparkMax( ARM_LIFT_CAN_ID, MotorType.kBrushless );
    private RelativeEncoder armLiftEncoder    = armLift.getEncoder();

    public RobotArmLiftManipulator()
    {
        armLiftEncoder.setPosition( 0 );
        armLiftEncoder.setPositionConversionFactor( ENCODER_POSITION_CONVERSION_FACTOR );
    }

    public void lift( double speed )
    {
        double rotationDistance = armLiftEncoder.getPosition() * PULLY_CIRCUMFERENCE;

        log.debug( "Lift position: {} velocity: {}", armLiftEncoder.getPosition(), armLiftEncoder.getVelocity()  );
        log.debug( "rotationDistance: {}", rotationDistance );

        double scaledSpeed = speed * ARM_LIFT_SCALE_FACTOR;

        log.trace( "Setting Speed: " + scaledSpeed );

        armLift.set( scaledSpeed );
    }

    public void simulationInit()
    {
        log.info( "subsystem sim init" );
        REVPhysicsSim.getInstance().addSparkMax( armLift, DCMotor.getNEO( 1 ) );
    }

}
