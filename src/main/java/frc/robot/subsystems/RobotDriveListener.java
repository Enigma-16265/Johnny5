package frc.robot.subsystems;

import frc.robot.subsystems.RobotDrive.DriveMode;

public interface RobotDriveListener
{
    void driveModeUpdate( DriveMode mode );
}
