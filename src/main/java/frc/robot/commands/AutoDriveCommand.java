package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.RobotDrive;

public class AutoDriveCommand extends CommandBase {

    private final RobotDrive robotDrive;

    int cnt = 0;

    public AutoDriveCommand( RobotDrive robotDrive )
    {
        this.robotDrive = robotDrive;
        addRequirements( robotDrive );
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        System.out.println( "cnt: " + cnt );
        cnt++;
    }

    @Override
    public void end( boolean interrupted ) {
        System.out.println( "!!!!!!!!!!!!!!! HELLLLLOO" );
    }

    @Override
    public boolean isFinished() {
        return false;
    }

}