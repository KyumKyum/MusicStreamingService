package dbpackage;

import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class FindUserTest {

    @Test
    void findUser() throws SQLException {
        String query = String.format("SELECT * FROM %s WHERE SSN = ?","serviceuser");
        System.out.println(query);
        PreparedStatement pstmt = DatabaseHandler.getConnect().prepareStatement(query);
        pstmt.setString(1,"123456-1234567");
        ResultSet rs = pstmt.executeQuery(query);
        System.out.println(rs.next());
    }
}