package frc.robot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class RobotDrive extends SubsystemBase{
 
    private static final Logger log = LogManager.getLogger( RobotDrive.class );

    enum DriveMode {
        ARCADE,
        TANK,
        CURVATURE
    };

    public static final DriveMode DEFAULT_DRIVE_MODE  = DriveMode.CURVATURE;
    
    public static final int LEFT_MOTOR_FRONT_CAN_ID   = 11;
    public static final int LEFT_MOTOR_REAR_CAN_ID    = 12;
    public static final int LEFT_ENCODER_CHANNEL_A    = 4;
    public static final int LEFT_ENCODER_CHANNEL_B    = 5;
    
    public static final int RIGHT_MOTOR_FRONT_CAN_ID  = 13;
    public static final int RIGHT_MOTOR_REAR_CAN_ID   = 14;
    public static final int RIGHT_ENCODER_CHANNEL_A   = 6;
    public static final int RIGHT_ENCODER_CHANNEL_B   = 7;

    private static final double COUNTS_PER_REV        = 1440.0;
    private static final double WHEEL_DIAMATER_IN     = 6.5;

    private MotorControllerGroup leftMotors = 
        new MotorControllerGroup( new WPI_VictorSPX( LEFT_MOTOR_FRONT_CAN_ID ),
                                  new WPI_VictorSPX( LEFT_MOTOR_REAR_CAN_ID  ) );

    private Encoder leftEncoder = new Encoder( LEFT_ENCODER_CHANNEL_A, LEFT_ENCODER_CHANNEL_B );

    private MotorControllerGroup rightMotors = 
        new MotorControllerGroup( new WPI_VictorSPX( RIGHT_MOTOR_FRONT_CAN_ID ),
                                  new WPI_VictorSPX( RIGHT_MOTOR_REAR_CAN_ID   ) );
    
    private Encoder rightEncoder = new Encoder( RIGHT_ENCODER_CHANNEL_A, RIGHT_ENCODER_CHANNEL_B );


    private DifferentialDrive diffDrive = new DifferentialDrive( leftMotors, rightMotors );

    private DriveMode mode = DEFAULT_DRIVE_MODE;

    public RobotDrive() {
        rightMotors.setInverted( true );

        leftEncoder.setDistancePerPulse( ( Math.PI * WHEEL_DIAMATER_IN ) / COUNTS_PER_REV );
        rightEncoder.setDistancePerPulse( ( Math.PI * WHEEL_DIAMATER_IN ) / COUNTS_PER_REV );
        resetEncoders();

        log.info( "Inital Drive Mode: " + mode );
    };

    public void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
    };

    public void drive( double leftXAxisSpeed,
                       double leftYAxisSpeed,
                       double rightXAxisSpeed,
                       double rightYAxisSpeed ) {
        switch( mode ) {
          case ARCADE:
          default:
          diffDrive.arcadeDrive( -leftYAxisSpeed, leftXAxisSpeed );
          break;
    
          case TANK:
          diffDrive.tankDrive( -leftYAxisSpeed, -rightYAxisSpeed );
          break;
    
          case CURVATURE:
          diffDrive.curvatureDrive( -leftYAxisSpeed, -rightXAxisSpeed, false );
          break;
        }
    }

    void setMode( DriveMode mode )
    {
        if ( this.mode != mode )
        {
            this.mode = mode;
            log.info( "Set Drive Mode: " + mode );
        }
    }
}
