package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.DoubleTopic;

import com.revrobotics.CANSparkMax;
import com.revrobotics.REVPhysicsSim;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.logging.Log;
import frc.robot.logging.LogManager;

public class RobotArmLiftManipulator extends SubsystemBase {

    private static final Log log = LogManager.getLogger( RobotArmLiftManipulator.class );

    final DoublePublisher positionPublisher;
    final DoublePublisher velocityPublisher;

    public static final int     ARM_LIFT_CAN_ID                    = 16;
    public static final double  ARM_LIFT_SCALE_FACTOR              = 0.60;
    public static final double  ENCODER_POSITION_CONVERSION_FACTOR = 0.01;
    public static final double  PULLY_CIRCUMFERENCE                = Math.PI * 4.25; // inches
    public static final double  MAX_ROTATION_DISTANCE              = PULLY_CIRCUMFERENCE * 3;
    public static final boolean ENFORCE_LIMITS                     = false;

    private CANSparkMax     armLift           = new CANSparkMax( ARM_LIFT_CAN_ID, MotorType.kBrushless );
    private RelativeEncoder armLiftEncoder    = armLift.getEncoder();

    public RobotArmLiftManipulator()
    {
        armLiftEncoder.setPosition( 0 );
        armLiftEncoder.setPositionConversionFactor( ENCODER_POSITION_CONVERSION_FACTOR );

        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        positionPublisher = inst.getDoubleTopic("/subsystems/ArmLift/position").publish();
        velocityPublisher = inst.getDoubleTopic("/subsystems/ArmLift/velocity").publish();
    }

    private int cnt = 1;
    public void lift( double speed )
    {
        double rotationDistance = armLiftEncoder.getPosition() * PULLY_CIRCUMFERENCE;
        double scaledSpeed      = speed * ARM_LIFT_SCALE_FACTOR;
        
        if ( ( cnt % 50 ) == 0 )
        {
          log.fatal( "\n"+
                     "position: %f\n" + 
                     "velocity: %f\n" +
                     "rotationDistance: %f\n" +
                     "speed: %f\n"+
                     "scaledSpeed: %f", 
                     armLiftEncoder.getPosition(), 
                     armLiftEncoder.getVelocity(),
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
            armLift.set( scaledSpeed );
        }

    }

    public void simulationInit()
    {
        log.info( "subsystem sim init" );
        REVPhysicsSim.getInstance().addSparkMax( armLift, DCMotor.getNEO( 1 ) );
    }

}
