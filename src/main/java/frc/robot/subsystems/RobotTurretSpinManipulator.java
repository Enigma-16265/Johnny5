package frc.robot.subsystems;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revrobotics.CANSparkMax;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class RobotTurretSpinManipulator extends SubsystemBase {

    private static final Logger log = LogManager.getLogger( RobotTurretSpinManipulator.class );

    public static final int    TURRET_SPIN_CAN_ID       = 15;
    public static final double TURRET_SPIN_SCALE_FACTOR = 0.08;
    public static final double ENCODER_POSITION_CONVERSION_FACTOR = 0.25;

    private CANSparkMax     turretSpin        = new CANSparkMax( TURRET_SPIN_CAN_ID, MotorType.kBrushless );
    private RelativeEncoder turretSpinEncoder = turretSpin.getEncoder();

    public RobotTurretSpinManipulator()
    {
        turretSpinEncoder.setPosition( 0 );
        turretSpinEncoder.setPositionConversionFactor( ENCODER_POSITION_CONVERSION_FACTOR );
    }
    
    public void spin( double speed ) {

        log.trace( "Turret position: {} velocity: {}", turretSpinEncoder.getPosition(), turretSpinEncoder.getVelocity()  );

        double scaledSpeed = speed * TURRET_SPIN_SCALE_FACTOR;

        log.trace( "Setting Speed: " + scaledSpeed );

        turretSpin.set( scaledSpeed );
    }

    public void simulationInit()
    {
        log.info( "subsystem sim init" );
        REVPhysicsSim.getInstance().addSparkMax( turretSpin, DCMotor.getNEO( 1 ) );
    }

}
