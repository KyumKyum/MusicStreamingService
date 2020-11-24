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
