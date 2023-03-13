package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class RobotTurretSpinManipulator extends SubsystemBase {

    public static final int    TURRET_SPIN_CAN_ID       = 15;
    public static final double TURRET_SPIN_SCALE_FACTOR = 0.15;

    private CANSparkMax     turretSpin        = new CANSparkMax( TURRET_SPIN_CAN_ID, MotorType.kBrushless );
    private RelativeEncoder turretSpinEncoder = turretSpin.getEncoder();

    public RobotTurretSpinManipulator()
    {

    }
    
    public void spin( double speed ) {
        turretSpin.set( speed * TURRET_SPIN_SCALE_FACTOR );
    }

}
