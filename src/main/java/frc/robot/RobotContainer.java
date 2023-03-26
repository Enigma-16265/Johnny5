package frc.robot;

import com.revrobotics.REVPhysicsSim;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.commands.ArmLiftCommand;
import frc.robot.commands.ArmSlideCommand;
import frc.robot.commands.AutoDriveCommand;
import frc.robot.commands.DriveCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.TurretSpinCommand;
import frc.robot.config.RobotConfig.DriverInputMapping;
import frc.robot.config.RobotConfig.ManipulatorInputMapping;
import frc.robot.subsystems.RobotArmLiftManipulator;
import frc.robot.subsystems.RobotArmSlideManipulator;
import frc.robot.subsystems.RobotDrive;
import frc.robot.subsystems.RobotIntakeManipulator;
import frc.robot.subsystems.RobotTurretSpinManipulator;
import frc.robot.subsystems.RobotDrive.DriveMode;

//Nav x2 mxp

public class RobotContainer
{

    // Drive Train Command and Control
    private final Joystick                   driverGamepad              = new Joystick( DriverInputMapping.PORT );
    private final RobotDrive                 robotDrive                 = new RobotDrive();
    private final DriveCommand               driveCommand;
    
    // Manipulator Command and Control
    private final Joystick                   manipulatorGamepad         = new Joystick( ManipulatorInputMapping.PORT );

    private final RobotArmLiftManipulator    robotArmLiftManipulator    = new RobotArmLiftManipulator();
    private final ArmLiftCommand             armLiftCommand;

    private final RobotArmSlideManipulator   robotArmSlideManipulator   = new RobotArmSlideManipulator();
    private final ArmSlideCommand            armSlideCommand;

    private final RobotTurretSpinManipulator robotTurretSpinManipulator = new RobotTurretSpinManipulator();
    private final TurretSpinCommand          turretSpinCommand;

    private final RobotIntakeManipulator     robotIntakeManipulator     = new RobotIntakeManipulator();
    private final IntakeCommand              intakeCommand;

    private final AutoDriveCommand           autoDriveCommand;

    public RobotContainer() {

        driveCommand = new DriveCommand( robotDrive,
                                         () -> driverGamepad.getRawAxis( DriverInputMapping.LEFT_X_AXIS_PORT  ),
                                         () -> driverGamepad.getRawAxis( DriverInputMapping.LEFT_Y_AXIS_PORT  ),
                                         () -> driverGamepad.getRawAxis( DriverInputMapping.RIGHT_X_AXIS_PORT ),
                                         () -> driverGamepad.getRawAxis( DriverInputMapping.RIGHT_Y_AXIS_PORT ) );       
        robotDrive.setDefaultCommand( driveCommand );
        
        armLiftCommand = new ArmLiftCommand( robotArmLiftManipulator,
                                             () -> manipulatorGamepad.getRawAxis( ManipulatorInputMapping.LEFT_Y_AXIS_PORT ) );
        robotArmLiftManipulator.setDefaultCommand( armLiftCommand );

        armSlideCommand = new ArmSlideCommand( robotArmSlideManipulator,
                                               () -> -manipulatorGamepad.getRawAxis( ManipulatorInputMapping.RIGHT_Y_AXIS_PORT ) );
        robotArmSlideManipulator.setDefaultCommand( armSlideCommand );

        turretSpinCommand = new TurretSpinCommand( robotTurretSpinManipulator,
                                                   () -> - manipulatorGamepad.getRawAxis( ManipulatorInputMapping.RIGHT_X_AXIS_PORT ) );
        robotTurretSpinManipulator.setDefaultCommand( turretSpinCommand );

        intakeCommand = new IntakeCommand( robotIntakeManipulator,
                                           () ->
        {
            double ltrigger = manipulatorGamepad.getRawAxis( ManipulatorInputMapping.LEFT_TRIGGER_PORT );
            double rtrigger = manipulatorGamepad.getRawAxis( ManipulatorInputMapping.RIGHT_TRIGGER_PORT );
            double speed    = ltrigger + -rtrigger;
            return speed;
        } );

        robotIntakeManipulator.setDefaultCommand( intakeCommand );

        autoDriveCommand = new AutoDriveCommand( robotDrive );

        //configureButtonBindings();
    }

    public void modeCheck()
    {
    
        if ( driverGamepad.getRawButtonPressed( DriverInputMapping.B_BUTTON_ID ) )
        {
            robotDrive.setMode( DriveMode.ARCADE );
        } else
        if ( driverGamepad.getRawButtonPressed( DriverInputMapping.A_BUTTON_ID ) )
        {
            robotDrive.setMode( DriveMode.TANK );
        } else
        if ( driverGamepad.getRawButtonPressed( DriverInputMapping.X_BUTTON_ID ) )
        {
            robotDrive.setMode( DriveMode.CURVATURE );
        }
        

        if ( manipulatorGamepad.getRawButtonPressed( ManipulatorInputMapping.Y_BUTTON_ID ) )
        {
            robotArmLiftManipulator.enforceLimitToggle();
            robotArmSlideManipulator.enforceLimitToggle();
            robotTurretSpinManipulator.enforceLimitToggle();
        }

        if ( manipulatorGamepad.getRawButtonPressed( ManipulatorInputMapping.B_BUTTON_ID ) )
        {
            robotArmLiftManipulator.resetEncoder();
            robotArmSlideManipulator.resetEncoder();
            robotTurretSpinManipulator.resetEncoder();
        }
    }

    public void simulationInit()
    {
        robotArmLiftManipulator.simulationInit();
        robotArmSlideManipulator.simulationInit();
        robotTurretSpinManipulator.simulationInit();
        robotIntakeManipulator.simulationInit();
    }

    public void simulationPeriodic()
    {
        REVPhysicsSim.getInstance().run();
    }

    public void autonomousInit()
    {
        autoDriveCommand.schedule();
    }

    public void autonomousCancel()
    {
        if ( autoDriveCommand.isScheduled() )
        {
            autoDriveCommand.cancel();
        }
    }

    public void close()
    {

    }
}
