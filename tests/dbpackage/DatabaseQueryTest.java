package dbpackage;


import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class DatabaseQueryTest {

    @Test
    void findAccount() {
        ResultSet rs = null;
        try{
            String query = String.format("SELECT %s FROM %s WHERE AID = %s AND APW = %s AND SerialNum = %s","Nickname","admin","'adminDebug'","'1234'","'3224'");
            Statement stmt = DatabaseHandler.getConnect().prepareStatement(query);
            rs = stmt.executeQuery(query);
            while(rs.next()){
                System.out.print(rs.getString("Nickname"));
            }
            stmt.close();
            //rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}