package dbpackage;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UserRegister {

    private final static String USER = "serviceuser";

    public static void register(Connection con) throws SQLException {
        Statement stmt = con.createStatement();
        stmt.execute("INSERT INTO" + USER + "VALUES ('123456-1234567','LKM',18,'wrwehrjehk','dummyaddr')");

        stmt.close();
    }
}
