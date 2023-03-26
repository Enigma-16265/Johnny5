package frc.robot.config;

import frc.robot.subsystems.RobotDrive.DriveMode;

public class RobotConfig
{
    public static double JOYSTICK_DEAD_BAND = 0.065;
    public static double ZERO_SPEED         = 0.0;
    public static double ZERO_POSITION      = 0.0;
    public static double EPLISON_DIST       = 3.0;

    public static class DriverInputMapping
    {
        public static int PORT               = 0;
        public static int LEFT_X_AXIS_PORT   = 0;
        public static int LEFT_Y_AXIS_PORT   = 1;
        public static int RIGHT_X_AXIS_PORT  = 4;
        public static int RIGHT_Y_AXIS_PORT  = 5;
        public static int LEFT_TRIGGER_PORT  = 2;
        public static int RIGHT_TRIGGER_PORT = 3;
        public static int A_BUTTON_ID        = 1;
        public static int B_BUTTON_ID        = 2;
        public static int X_BUTTON_ID        = 3;
        public static int Y_BUTTON_ID        = 4;
    }

    public static class ManipulatorInputMapping
    {
        public static int PORT               = 1;
        public static int LEFT_X_AXIS_PORT   = 0;
        public static int LEFT_Y_AXIS_PORT   = 1;
        public static int RIGHT_X_AXIS_PORT  = 4;
        public static int RIGHT_Y_AXIS_PORT  = 5;
        public static int LEFT_TRIGGER_PORT  = 2;
        public static int RIGHT_TRIGGER_PORT = 3;
        public static int A_BUTTON_ID        = 1;
        public static int B_BUTTON_ID        = 2;
        public static int X_BUTTON_ID        = 3;
        public static int Y_BUTTON_ID        = 4;
    }

    public static class DriveConfig
    {
        public static DriveMode DEFAULT_DRIVE_MODE       = DriveMode.CURVATURE;
        public static double    ARCADE_X_SCALE_FACTOR    = 0.75;
        public static double    ARCADE_Y_SCALE_FACTOR    = 0.90;
        public static double    TANK_SCALE_FACTOR        = 0.90;
        public static double    CURVATURE_X_SCALE_FACTOR = 0.75;
        public static double    CURVATURE_Y_SCALE_FACTOR = 0.75;
    }
    public static class DriveCommandConfig
    {
        public static double ARCADE_X_SPEED_ACCEL_LIMIT_UNITS_PER_SEC    = 0.1;
        public static double ARCADE_Y_SPEED_ACCEL_LIMIT_UNITS_PER_SEC    = 0.1;
        public static double TANK_SPEED_ACCEL_LIMIT_UNITS_PER_SEC        = 0.1;
        public static double CURVATURE_X_SPEED_ACCEL_LIMIT_UNITS_PER_SEC = 0.1;
        public static double CURVATURE_Y_SPEED_ACCEL_LIMIT_UNITS_PER_SEC = 0.1;
    }

    public static class ArmLiftConfig
    {
        public static double  SPEED_SCALE_FACTOR                 = 1.00;
        public static double  ENCODER_POSITION_CONVERSION_FACTOR = 0.01;
        public static double  PULLY_CIRCUMFERENCE                = Math.PI * 4.25; // inches
        public static double  MIN_ROTATION_DISTANCE              = 0.0;
        public static double  MAX_DISTANCE_BACKOFF               = 5.0;
        public static double  MAX_ROTATION_DISTANCE              = ( PULLY_CIRCUMFERENCE * 3.127 ) - MAX_DISTANCE_BACKOFF; // Target: 41.752739 Circ: 13.351769
        public static boolean ENFORCE_LIMITS_DEFAULT             = true;
    }

    public static class ArmLiftCommandConfig
    {
        public static double  SPEED_ACCEL_LIMIT_UNITS_PER_SEC = 0.1;
    }

    public static class ArmSlideConfig
    {
        public static double  SPEED_SCALE_FACTOR                 = 0.50;
        public static double  ENCODER_POSITION_CONVERSION_FACTOR = 0.0625;
        public static double  PULLY_CIRCUMFERENCE                = Math.PI * 1.5; //inches
        public static double  MIN_ROTATION_DISTANCE              = 0.0;
        public static double  MAX_DISTANCE_BACKOFF               = 5.0;
        public static double  MAX_ROTATION_DISTANCE              = ( PULLY_CIRCUMFERENCE * 6 ) - MAX_DISTANCE_BACKOFF;
        public static boolean ENFORCE_LIMITS_DEFAULT             = true;
    }

    public static class ArmSlideCommandConfig
    {
        public static double  SPEED_ACCEL_LIMIT_UNITS_PER_SEC = 0.1;
    }

    public static class TurretConfig
    {
        public static double  TURRET_SPIN_SCALE_FACTOR           = 0.20;
        public static double  ENCODER_POSITION_CONVERSION_FACTOR = 0.25;
        public static double  PULLY_CIRCUMFERENCE                = Math.PI * 1.0; // inches
        public static double  MIN_ROTATION_DISTANCE              = -1.0;
        public static double  MAX_ROTATION_DISTANCE              = 1.0;
        public static boolean ENFORCE_LIMITS_DEFAULT             = true;
    }

    public static class TurretSpinCommandConfig
    {
        public static double  SPEED_ACCEL_LIMIT_UNITS_PER_SEC = 0.1;
    }
    
}
