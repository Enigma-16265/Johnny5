package frc.robot.subsystems;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revrobotics.CANSparkMax;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.logging.DataNetworkTableLog;

public class RobotIntakeManipulator extends SubsystemBase
{
    private static final Logger log = LogManager.getLogger( RobotIntakeManipulator.class );

    private static final DataNetworkTableLog dataLog =
    new DataNetworkTableLog( 
        "subsystems.Intake",
        Map.of( "speed", DataNetworkTableLog.COLUMN_TYPE.DOUBLE ) );

    public static final int CAN_ID = 18;

    private CANSparkMax armIntake = new CANSparkMax( CAN_ID, MotorType.kBrushed );

    public RobotIntakeManipulator()
    {

    }

    public void intake( double speed )
    {
        dataLog.publish( "speed", speed );
        armIntake.set( speed );
    }

    public void simulationInit()
    {
        log.info( "subsystem sim init" );
        //REVPhysicsSim.getInstance().addSparkMax( armIntake, DCMotor.getNEO( 1 ) );
    }

}
