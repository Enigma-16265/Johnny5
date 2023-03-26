package frc.robot.subsystems;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.config.RobotConfig.DriveConfig;
import frc.robot.logging.DataNetworkTableLog;
import frc.robot.logging.Log;
import frc.robot.logging.LogManager;

public class RobotDrive extends SubsystemBase{
 
  private static final Log log = LogManager.getLogger( "subsystems.RobotDrive" );

  private static final DataNetworkTableLog dataLog =
      new DataNetworkTableLog( 
          "subsystems.RobotDrive",
          Map.of( "mode",  DataNetworkTableLog.COLUMN_TYPE.STRING,
                  "speed", DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                  "rotation", DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                  "scaledSpeed", DataNetworkTableLog.COLUMN_TYPE.DOUBLE,
                  "scaledRotation", DataNetworkTableLog.COLUMN_TYPE.DOUBLE ) );

    public enum DriveMode {
        ARCADE,
        TANK,
        CURVATURE
    };

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

    private DriveMode          mode     = DriveConfig.DEFAULT_DRIVE_MODE;
    private RobotDriveListener listener = null;

    public RobotDrive() {
        rightMotors.setInverted( true );

        leftEncoder.setDistancePerPulse( ( Math.PI * WHEEL_DIAMATER_IN ) / COUNTS_PER_REV );
        rightEncoder.setDistancePerPulse( ( Math.PI * WHEEL_DIAMATER_IN ) / COUNTS_PER_REV );
        resetEncoders();

        log.info( "Inital Drive Mode: " + mode );
    }

    public void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    private int cnt = 1;
    public void drive( double speed,
                       double rotation )
    {

        if ( ( cnt % 10 ) == 0 )
        {
            log.trace( "speed: " + speed + " rotation: " + rotation );
        }
        cnt++;

        dataLog.publish( "mode", mode.name() );
        dataLog.publish( "speed", speed );
        dataLog.publish( "rotation", rotation );

        switch( mode )
        {
          case ARCADE:
          default:{

            double scaledSpeed    = speed * DriveConfig.ARCADE_SPEED_SCALE_FACTOR;
            double scaledRotation = rotation * DriveConfig.ARCADE_ROTATION_SCALE_FACTOR;

            dataLog.publish( "scaledSpeed", scaledSpeed );
            dataLog.publish( "scaledRotation", scaledRotation );

            diffDrive.arcadeDrive( scaledSpeed, scaledRotation );
          }
          break;
    
          case TANK: {

            // Speed corresponds to left track, Rotation corresponds to right track
            double scaledSpeed    = speed * DriveConfig.TANK_SPEED_SCALE_FACTOR;
            double scaledRotation = rotation * DriveConfig.TANK_SPEED_SCALE_FACTOR;

            dataLog.publish( "scaledSpeed", scaledSpeed );
            dataLog.publish( "scaledRotation", scaledRotation );

            diffDrive.tankDrive( scaledSpeed, scaledRotation );
          }
          break;
    
          case CURVATURE:
          {

            double scaledSpeed    = speed * DriveConfig.CURVATURE_SPEED_SCALE_FACTOR;
            double scaledRotation = rotation * DriveConfig.CURVATURE_ROTATION_SCALE_FACTOR;

            dataLog.publish( "scaledSpeed", scaledSpeed );
            dataLog.publish( "scaledRotation", scaledRotation );

            diffDrive.curvatureDrive( scaledSpeed, scaledRotation, DriveConfig.CURVATURE_TURN_IN_PLACE );
          }
          break;
        }
    }

    public DriveMode getMode()
    {
        return mode;
    }

    public void setMode( DriveMode mode )
    {
        if ( this.mode != mode )
        {
            this.mode = mode;
            log.info( "Set Drive Mode: " + mode );

            if ( listener != null )
            {
                listener.driveModeUpdate( this.mode );
            }
        }
    }

    public void setDefaultMode()
    {
        mode = DriveConfig.DEFAULT_DRIVE_MODE;
    }

    public void setListener( RobotDriveListener listener )
    {
        this.listener = listener;
    }

}
