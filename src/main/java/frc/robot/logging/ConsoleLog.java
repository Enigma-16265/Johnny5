package frc.robot.logging;

public class ConsoleLog extends Log
{
    public ConsoleLog()
    {
        
    }

    @Override
    public void uncheckedLog( String message )
    {
        System.out.println( message );
    }
    
}
