package frc.robot.config;

public class RobotConfig
{
    public static double JOYSTICK_DEAD_BAND = 0.05;
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
    
}
