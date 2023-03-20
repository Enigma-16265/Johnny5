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

public class RobotTurretSpinManipulator extends SubsystemBase {

    private static final Log log = LogManager.getLogger( LogManager.Type.NETWORK_TABLES, "subsystems.TurretSpin" );

    private static final DataNetworkTableLog dataLog =
        new DataNetworkTableLog( 
            "subsystems.TurretSpin",
            Map.of( "position",         DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "velocity",         DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "speed",            DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                    "scaledSpeed",      DataNetworkTableLog.COLUMN_TYPE.DOUBLE ) );

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
    
    private int cnt = 1;
    public void spin( double speed )
    {
        double scaledSpeed = speed * TURRET_SPIN_SCALE_FACTOR;
        
        if ( ( cnt % 10 ) == 0 )
        {
            dataLog.publish( "position",         turretSpinEncoder.getPosition() );
            dataLog.publish( "velocity",         turretSpinEncoder.getVelocity() );
            dataLog.publish( "speed",            speed );
            dataLog.publish( "scaledSpeed",      scaledSpeed );
        }
        cnt++;

        turretSpin.set( scaledSpeed );
    }

    public void simulationInit()
    {
        log.info( "subsystem sim init" );
        REVPhysicsSim.getInstance().addSparkMax( turretSpin, DCMotor.getNEO( 1 ) );
    }

}
