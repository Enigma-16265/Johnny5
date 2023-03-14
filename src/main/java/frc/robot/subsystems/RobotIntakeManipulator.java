package frc.robot.subsystems;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revrobotics.CANSparkMax;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class RobotIntakeManipulator extends SubsystemBase
{
    private static final Logger log = LogManager.getLogger( RobotIntakeManipulator.class );

    public static final int INTAKE_CAN_ID = 18;

    private CANSparkMax armIntake = new CANSparkMax( INTAKE_CAN_ID, MotorType.kBrushed );

    public RobotIntakeManipulator()
    {

    }

    public void intake( double speed ) {
        armIntake.set(speed);
    }

    public void simulationInit()
    {
        log.info( "subsystem sim init" );
        //REVPhysicsSim.getInstance().addSparkMax( armIntake, DCMotor.getNEO( 1 ) );
    }

}
