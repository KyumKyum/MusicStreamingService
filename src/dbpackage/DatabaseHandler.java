package dbpackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DatabaseHandler {

    private final static Logger LOG = Logger.getGlobal();

    private final static String PASSWORD = "Cheesepasta613!";
    private final static String USERNAME = "root";
    private final static String DATABASE_NAME = "dbProj";
    private final static String SERVER_HOST = "localhost:3307";

    public final static String USER = "serviceuser";
    public final static String ACCOUNT = "account";
    public final static String ADMIN = "admin";
    public final static String PROFILE = "profile";
    public final static String MUSIC = "music";
    public final static String PLAYLIST = "playlist";
    public final static String PLAYLIST_MUSIC = "playlist_music";
    public final static String ACCOUNT_MUSIC = "account_music";
    public final static String MUSIC_MOSTLY_LISTENED = "music_mostlylistened";

    public static Connection getConnect() throws SQLException {

        try{
            Class.forName("org.mariadb.jdbc.Driver");

        }catch(ClassNotFoundException e){
            LOG.severe("CRITICAL ERROR - Database is not loadable. ERROR MESSAGE: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
        LOG.fine("DATABASE CONNECTED");
        return DriverManager.getConnection("jdbc:mysql://"+SERVER_HOST+"/"+ DATABASE_NAME,USERNAME,PASSWORD);
    }

}
