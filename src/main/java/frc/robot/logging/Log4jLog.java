package frc.robot.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4jLog extends Log {
    
    private final Logger log;

    public Log4jLog( String name )
    {
        log = LogManager.getLogger( name );
    }

    @Override
    public void uncheckedLog( String message )
    {
        log.log( org.apache.logging.log4j.Level.ALL , message );
    }

}
