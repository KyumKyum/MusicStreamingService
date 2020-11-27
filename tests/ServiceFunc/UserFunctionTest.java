package ServiceFunc;

import dbpackage.DatabaseHandler;
import dbpackage.DatabaseQuery;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserFunctionTest {

    @Test
    void register() {
        try{
            ResultSet rs = DatabaseQuery.checkAccount(DatabaseHandler.getConnect(),"ID,PW","rfrf","Passwordfordummies");
            if(rs.next()){
                System.out.print("WELCOME!");
            }else{
                System.out.print("NO ID");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}