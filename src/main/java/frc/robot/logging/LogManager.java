package frc.robot.logging;

import java.util.HashMap;
import java.util.Map;

public class LogManager {

    public enum Type {
        CONSOLE,
        LOG4J,
        NETWORK_TABLES
    }

    private static Type      defaultType     = Type.CONSOLE;
    private static Log.Level defaultLogLevel = Log.Level.OFF;

    private static Map< Type, Map< String, Log > > logInstances = new HashMap< Type, Map< String, Log > >();

    public static synchronized Type getDefaultType()
    {
        return defaultType;
    }

    public static synchronized void setDefaultType( Type type )
    {
        defaultType = type;
    }

    public static synchronized Log.Level getDefaultLogLevel()
    {
        return defaultLogLevel;
    }

    public static synchronized void setDefaultLogLevel( Log.Level level )
    {
        defaultLogLevel = level;
    }

    public static synchronized void setAllLogLevels( Log.Level level )
    {
        defaultLogLevel = level;

        for ( Map< String, Log > typeInstances : logInstances.values() ) {
            for ( Log log : typeInstances.values() ) {
                log.setLevel( defaultLogLevel );
            }    
        }

    }

    public static synchronized Log getLogger( String name )
    {
        return getLogger( defaultType, name );
    }

    public static synchronized Log getLogger( Class<?> clazz )
    {
        return getLogger( defaultType, clazz.getCanonicalName() );
    }

    public static synchronized Log getLogger( Type type, String name )
    {
        Log log = null;

        if ( !logInstances.containsKey( type ) )
        {
            logInstances.put( type, new HashMap< String, Log >() );
        }

        switch ( type )
        {
            case CONSOLE:
            default:
            {
                if ( !logInstances.get( Type.CONSOLE ).containsKey( "consoleLog" ) )
                {
                    Log newLogInstance = new ConsoleLog();
                    newLogInstance.setLevel( defaultLogLevel ) ;

                    logInstances.get( Type.CONSOLE ).put( "consoleLog", newLogInstance );
                }
                
                log = logInstances.get( Type.CONSOLE ).get( "consoleLog" );
            }
                break;
            case LOG4J:
            {
                if ( !logInstances.get( Type.LOG4J ).containsKey( name ) )
                {
                    Log newLogInstance = new Log4jLog( name );
                    newLogInstance.setLevel( defaultLogLevel ) ;

                    logInstances.get( Type.LOG4J ).put( name, newLogInstance );
                }
                
                log = logInstances.get( Type.LOG4J ).get( name );
            }
                break;
            case NETWORK_TABLES:
            {
                if ( !logInstances.get( Type.NETWORK_TABLES ).containsKey( name ) )
                {
                    Log newLogInstance = new NetworkTablesLog( name );
                    newLogInstance.setLevel( defaultLogLevel ) ;

                    logInstances.get( Type.NETWORK_TABLES ).put( name, newLogInstance );
                }
                
                log = logInstances.get( Type.NETWORK_TABLES ).get( name );
            }
                break;
        }

        return log;
    }

    public static synchronized Log getLogger( Type type, Class<?> clazz )
    {
        return getLogger( type, clazz.getCanonicalName() );
    }
    
}
