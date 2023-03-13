package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import frc.robot.RobotDrive.DriveMode;

public class RobotContainer {

    public static class JOYSTICK_0
    {
        public static final int PORT               = 0;
        public static final int LEFT_X_AXIS_PORT   = 0;
        public static final int LEFT_Y_AXIS_PORT   = 1;
        public static final int RIGHT_X_AXIS_PORT  = 4;
        public static final int RIGHT_Y_AXIS_PORT  = 5;
        public static final int LEFT_TRIGGER_PORT  = 2;
        public static final int RIGHT_TRIGGER_PORT = 3;
        public static final int A_BUTTON_ID        = 1;
        public static final int B_BUTTON_ID        = 2;
        public static final int X_BUTTON_ID        = 3;
        public static final int Y_BUTTON_ID        = 4;
    }

    public static class JOYSTICK_1
    {
        public static final int PORT               = 1;
        public static final int LEFT_X_AXIS_PORT   = 0;
        public static final int LEFT_Y_AXIS_PORT   = 1;
        public static final int RIGHT_X_AXIS_PORT  = 4;
        public static final int RIGHT_Y_AXIS_PORT  = 5;
        public static final int LEFT_TRIGGER_PORT  = 2;
        public static final int RIGHT_TRIGGER_PORT = 3;
        public static final int A_BUTTON_ID        = 1;
        public static final int B_BUTTON_ID        = 2;
        public static final int X_BUTTON_ID        = 3;
        public static final int Y_BUTTON_ID        = 4;
    }

    private final Joystick       gamepad      = new Joystick( JOYSTICK_0.PORT );
    private final RobotDrive     robotDrive   = new RobotDrive();
    private final GamepadCommand gamepadCommand;
    
    private final Joystick                   gamepadManipulator         = new Joystick( JOYSTICK_1.PORT );

    private final RobotArmLiftManipulator    robotArmLiftManipulator    = new RobotArmLiftManipulator();
    private final ArmLiftCommand             armLiftCommand;

    private final RobotArmSlideManipulator   robotArmSlideManipulator   = new RobotArmSlideManipulator();
    private final ArmSlideCommand            armSlideCommand;

    private final RobotTurretSpinManipulator robotTurretSpinManipulator = new RobotTurretSpinManipulator();
    private final TurretSpinCommand          turretSpinCommand;

    private final RobotIntakeManipulator     robotIntakeManipulator     = new RobotIntakeManipulator();
    private final IntakeCommand              intakeCommand;

    public RobotContainer() {

        gamepadCommand = new GamepadCommand( robotDrive,
                                             () -> gamepad.getRawAxis( JOYSTICK_0.LEFT_X_AXIS_PORT  ),
                                             () -> gamepad.getRawAxis( JOYSTICK_0.LEFT_Y_AXIS_PORT  ),
                                             () -> gamepad.getRawAxis( JOYSTICK_0.RIGHT_X_AXIS_PORT ),
                                             () -> gamepad.getRawAxis( JOYSTICK_0.RIGHT_Y_AXIS_PORT ) );       
        robotDrive.setDefaultCommand( gamepadCommand );
        
        armLiftCommand = new ArmLiftCommand( robotArmLiftManipulator,
                                             () -> gamepadManipulator.getRawAxis( JOYSTICK_1.LEFT_Y_AXIS_PORT ) );
        robotArmLiftManipulator.setDefaultCommand( armLiftCommand );

        armSlideCommand = new ArmSlideCommand( robotArmSlideManipulator,
                                               () -> gamepadManipulator.getRawAxis( JOYSTICK_1.RIGHT_Y_AXIS_PORT ) );
        robotArmSlideManipulator.setDefaultCommand( armSlideCommand );

        turretSpinCommand = new TurretSpinCommand( robotTurretSpinManipulator,
                                                   () -> gamepadManipulator.getRawAxis( JOYSTICK_1.RIGHT_X_AXIS_PORT ) );
        robotTurretSpinManipulator.setDefaultCommand( turretSpinCommand );

        intakeCommand = new IntakeCommand( robotIntakeManipulator,
                                           () ->
        {
            double ltrigger = gamepadManipulator.getRawAxis( JOYSTICK_1.LEFT_TRIGGER_PORT );
            double rtrigger = gamepadManipulator.getRawAxis( JOYSTICK_1.RIGHT_TRIGGER_PORT );
            double speed    = ltrigger + -rtrigger;
            return speed;
        } );
        robotIntakeManipulator.setDefaultCommand( intakeCommand );

        //configureButtonBindings();
    }

    public void modeCheck() {
    
        if (gamepad.getRawButtonPressed( JOYSTICK_0.B_BUTTON_ID )) {
            robotDrive.setMode( DriveMode.ARCADE );
        } else if (gamepad.getRawButtonPressed( JOYSTICK_0.A_BUTTON_ID )) {
            robotDrive.setMode( DriveMode.TANK );
        } else if (gamepad.getRawButtonPressed( JOYSTICK_0.X_BUTTON_ID )) {
            robotDrive.setMode( DriveMode.CURVATURE );
        }
       
      }
}
