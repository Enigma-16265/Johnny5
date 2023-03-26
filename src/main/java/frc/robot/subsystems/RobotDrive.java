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
 
  private static final Log log = LogManager.getLogger( LogManager.Type.NETWORK_TABLES, "subsystems.RobotDrive" );

  private static final DataNetworkTableLog dataLog =
      new DataNetworkTableLog( 
          "subsystems.RobotDrive",
          Map.of( "leftCount",  DataNetworkTableLog.COLUMN_TYPE.INTEGER,
                  "rightCount", DataNetworkTableLog.COLUMN_TYPE.INTEGER ) );

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

    private DriveMode mode = DriveConfig.DEFAULT_DRIVE_MODE;

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
    public void drive( double leftXAxisSpeed,
                       double leftYAxisSpeed,
                       double rightXAxisSpeed,
                       double rightYAxisSpeed )
    {

      if ( ( cnt % 10 ) == 0 )
        {
            dataLog.publish( "leftCount",  (long) leftEncoder.get() );
            dataLog.publish( "rightCOunt", (long) rightEncoder.get() );
        }
        cnt++;

        switch( mode )
        {
          case ARCADE:
          default:{

            double scaledYSpeed = leftYAxisSpeed * DriveConfig.ARCADE_X_SCALE_FACTOR;
            double scaledXSpeed = rightXAxisSpeed * DriveConfig.ARCADE_Y_SCALE_FACTOR;

            diffDrive.arcadeDrive( -scaledYSpeed, scaledXSpeed );
          }
          break;
    
          case TANK: {

            double scaledYSpeed = leftYAxisSpeed * DriveConfig.TANK_SCALE_FACTOR;
            double scaledXSpeed = rightYAxisSpeed * DriveConfig.TANK_SCALE_FACTOR;

            diffDrive.tankDrive( -scaledYSpeed, -scaledXSpeed );
          }
          break;
    
          case CURVATURE:
          {

            double scaledYSpeed = leftYAxisSpeed * DriveConfig.CURVATURE_X_SCALE_FACTOR;
            double scaledXSpeed = rightXAxisSpeed * DriveConfig.CURVATURE_Y_SCALE_FACTOR;

            diffDrive.curvatureDrive( -scaledYSpeed, -scaledXSpeed, true );
          }
          break;
        }
    }

    public void setMode( DriveMode mode )
    {
        if ( this.mode != mode )
        {
            this.mode = mode;
            log.info( "Set Drive Mode: " + mode );
        }
    }

    public void setDefaultMode()
    {
      this.mode = DriveConfig.DEFAULT_DRIVE_MODE;
    }
}
