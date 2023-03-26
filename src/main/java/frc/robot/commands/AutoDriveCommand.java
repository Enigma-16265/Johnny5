package frc.robot.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.RobotDrive;

public class AutoDriveCommand extends CommandBase {

    private static final Logger log = LogManager.getLogger(AutoDriveCommand.class);

    static private final RobotDrive.DriveMode DRIVE_MODE = RobotDrive.DriveMode.TANK;

    // Speeds are scaled in sybsystems.RobotDrive according to Drive Mode scale
    // factors
    static private final double DRIVE_FORWARD_SPEED               = 0.75;
    static private final double DRIVE_FORWARD_COMPLETE_MARK_SECS  = 0.55;
    static private final double TRANSITION_SPEED                  = 0.0;
    static private final double TRANSITION_COMPLETE_MARK_SECS     = DRIVE_FORWARD_COMPLETE_MARK_SECS + 0.2;
    static private final double DRIVE_BACKWARD_SPEED              = -0.75;
    static private final double DRIVE_BACKWARD_COMPLETE_MARK_SECS = TRANSITION_COMPLETE_MARK_SECS + 2.10;

    private final Timer timer = new Timer();
    private final RobotDrive robotDrive;

    int cnt = 0;

    public AutoDriveCommand( RobotDrive robotDrive )
    {
        this.robotDrive = robotDrive;
        
        addRequirements(robotDrive);
    }

    @Override
    public void initialize() {
        robotDrive.setMode(DRIVE_MODE);
        timer.reset();

        log.debug( "AutoDrive begin" );
        log.debug( "\n" +
                   "DRIVE_FORWARD_SPEED:               {}\n" +
                   "DRIVE_FORWARD_COMPLETE_MARK_SECS:  {}\n" +
                   "TRANSITION_SPEED:                  {}\n" +
                   "TRANSITION_COMPLETE_MARK_SECS:     {}\n" +
                   "DRIVE_BACKWARD_SPEED:              {}\n" +
                   "DRIVE_BACKWARD_COMPLETE_MARK_SECS: {}",
                   DRIVE_FORWARD_SPEED,
                   DRIVE_FORWARD_COMPLETE_MARK_SECS,
                   TRANSITION_SPEED,
                   TRANSITION_COMPLETE_MARK_SECS,
                   DRIVE_BACKWARD_SPEED,
                   DRIVE_BACKWARD_COMPLETE_MARK_SECS);

        timer.start();

    }

    @Override
    public void execute() {
        
        // Drive speed inputs are set to mimic joystick assigned inputs in subsystem.RobotDrive TANK mode
        if ( timer.get() < DRIVE_FORWARD_COMPLETE_MARK_SECS )
        {
            robotDrive.drive( 0.0,
                              DRIVE_FORWARD_SPEED,
                              0.0,
                              DRIVE_FORWARD_SPEED );
        } else
        if ( timer.get() < TRANSITION_COMPLETE_MARK_SECS )
        {
            robotDrive.drive( 0.0,
                              TRANSITION_SPEED,
                              0.0,
                              TRANSITION_SPEED );
        } else
        if ( timer.get() < DRIVE_BACKWARD_COMPLETE_MARK_SECS )
        {
            robotDrive.drive( 0.0,
                              DRIVE_BACKWARD_SPEED,
                              0.0,
                              DRIVE_BACKWARD_SPEED );
        }
    }

    @Override
    public void end(boolean interrupted) {
        robotDrive.setDefaultMode();
        timer.stop();
        log.debug( "AutoDrive end" );
    }

    @Override
    public boolean isFinished() {
        return ( timer.get() >= DRIVE_BACKWARD_COMPLETE_MARK_SECS );
    }

}