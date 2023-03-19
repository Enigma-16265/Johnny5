package frc.robot.logging;

import java.util.Map;

public abstract class Log {
    
    public enum Level {
        ALL,
        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR,
        FATAL,
        OFF
    }

    private static Map< Level, Byte > LEVEL_VALUE_MAP =
        Map.of( Level.ALL,   (byte) 0,
                Level.TRACE, (byte) 1,
                Level.DEBUG, (byte) 2,
                Level.INFO,  (byte) 3,
                Level.WARN,  (byte) 4,
                Level.ERROR, (byte) 5,
                Level.FATAL, (byte) 6,
                Level.OFF,   (byte) 7 );

    protected Level level = Level.OFF;
    
    public void setLevel( Level level )
    {
        this.level = level;
    }

    public Level getLevel()
    {
        return level;
    }

    public boolean enabled( Level level )
    {
        return ( LEVEL_VALUE_MAP.get( this.level ) <= LEVEL_VALUE_MAP.get( level ) ); 
    }

    public void trace( String message )
    {
        log( Level.TRACE, message );
    }

    public void debug( String message )
    {
        log( Level.DEBUG, message );
    }

    public void info( String message )
    {
        log( Level.INFO, message );
    }

    public void warn( String message )
    {
        log( Level.WARN, message );
    }

    public void error( String message )
    {
        log( Level.ERROR, message );
    }

    public void fatal( String message )
    {
        log( Level.FATAL, message );
    }

    public void log( Level level, String message )
    {
        if ( enabled( level ) )
        {
            uncheckedLog( message );
        }
    }

    public void trace( String format, Object... args )
    {
        log( Level.TRACE, format, args );
    }

    public void debug( String format, Object... args )
    {
        log( Level.DEBUG, format, args );
    }

    public void info( String format, Object... args )
    {
        log( Level.INFO, format, args );
    }

    public void warn( String format, Object... args )
    {
        log( Level.WARN, format, args );
    }

    public void error( String format, Object... args )
    {
        log( Level.ERROR, format, args );
    }

    public void fatal( String format, Object... args )
    {
        log( Level.FATAL, format, args );
    }

    public void log( Level level, String format, Object... args )
    {
        if ( enabled( level ) )
        {
            uncheckedLog( String.format( format, args ) );
        }
    }

    public abstract void uncheckedLog( String message );

}
