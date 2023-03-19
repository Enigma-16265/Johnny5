package frc.robot.logging;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StringArrayPublisher;

public class NetworkTablesLog extends Log {

    public static final String LOG_PUBLISHER_NAME_FORMAT = "%s/log";

    private NetworkTableInstance networkTableInstance;
    private String               name;
    private StringArrayPublisher logPublisher;

    public NetworkTablesLog( String name )
    {
        networkTableInstance = NetworkTableInstance.getDefault();
    
        this.name = name.replaceAll( "\\.", "/" );
        logPublisher = 
            networkTableInstance.getStringArrayTopic(
               String.format( LOG_PUBLISHER_NAME_FORMAT, this.name ) ).publish();
    }

    @Override
    public void uncheckedLog( String message )
    {
        logPublisher.set( message.split( "\\R" ) );
    }

}
