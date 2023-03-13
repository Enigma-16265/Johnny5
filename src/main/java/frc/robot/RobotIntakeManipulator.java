package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class RobotIntakeManipulator extends SubsystemBase {

    public static final int INTAKE_CAN_ID = 18;

    private CANSparkMax armIntake = new CANSparkMax( INTAKE_CAN_ID, MotorType.kBrushed );

    public RobotIntakeManipulator()
    {

    }

    public void intake( double speed ) {
        armIntake.set(speed);
    }

}
