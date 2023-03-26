package frc.robot.config;

import frc.robot.subsystems.RobotDrive.DriveMode;

public class RobotConfig
{
    public static double JOYSTICK_DEAD_BAND = 0.065;
    public static double ZERO_SPEED         = 0.0;
    public static double ZERO_POSITION      = 0.0;
    public static double EPLISON_DIST       = 3.0;
    public static double ZERO_RATE_LIMIT    = 0.0;
    public static double ZERO_SLEW_RATE     = 0.0;

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
        public static DriveMode DEFAULT_DRIVE_MODE              = DriveMode.CURVATURE;
        public static double    ARCADE_SPEED_SCALE_FACTOR       = 0.75;
        public static double    ARCADE_ROTATION_SCALE_FACTOR    = 0.90;
        public static double    TANK_SPEED_SCALE_FACTOR         = 0.90;
        public static double    CURVATURE_SPEED_SCALE_FACTOR    = 0.75;
        public static double    CURVATURE_ROTATION_SCALE_FACTOR = 0.75;
        public static boolean   CURVATURE_TURN_IN_PLACE         = true;
    }
    public static class DriveCommandConfig
    {
        public static final byte ARCADE_ORIENTATION_CLEAR                   = 0x00;
        public static final byte ARCADE_ORIENTATION_INVERT_ROTATION_MASK    = 0x01;
        public static final byte ARCADE_ORIENTATION_INVERT_SPEED_MASK       = 0x02;

        public static final byte TANK_ORIENTATION_CLEAR                     = 0x00;
        public static final byte TANK_ORIENTATION_INVERT_RIGHT_SPEED_MASK   = 0x01;
        public static final byte TANK_ORIENTATION_INVERT_LEFT_SPEED_MASK    = 0x02;

        public static final byte CURVATURE_ORIENTATION_CLEAR                = 0x00;
        public static final byte CURVATURE_ORIENTATION_INVERT_ROTATION_MASK = 0x01;
        public static final byte CURVATURE_ORIENTATION_INVERT_SPEED_MASK    = 0x02;

        public static double  ARCADE_SPEED_ACCEL_LIMIT_UNITS_PER_SEC             = 3.0;
        public static double  ARCADE_ROTATION_SPEED_ACCEL_LIMIT_UNITS_PER_SEC    = 3.0;
        public static byte    ARCADE_ORIENTATION                                 = ARCADE_ORIENTATION_INVERT_SPEED_MASK;

        public static double  TANK_SPEED_ACCEL_LIMIT_UNITS_PER_SEC               = 3.0;
        public static byte    TANK_ORIENTATION                                   = TANK_ORIENTATION_INVERT_LEFT_SPEED_MASK |
                                                                                   TANK_ORIENTATION_INVERT_RIGHT_SPEED_MASK ;

        public static double  CURVATURE_SPEED_ACCEL_LIMIT_UNITS_PER_SEC          = 3.0;
        public static double  CURVATURE_ROTATION_SPEED_ACCEL_LIMIT_UNITS_PER_SEC = 3.0;
        public static byte    CURVATURE_ORIENTATION                              = CURVATURE_ORIENTATION_INVERT_SPEED_MASK |
                                                                                   CURVATURE_ORIENTATION_INVERT_ROTATION_MASK;
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
        public static double  SPEED_ACCEL_LIMIT_UNITS_PER_SEC = 3.0;
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
        public static double  SPEED_ACCEL_LIMIT_UNITS_PER_SEC = 3.0;
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
        public static double  SPEED_ACCEL_LIMIT_UNITS_PER_SEC = 3.0;
    }
    
}
