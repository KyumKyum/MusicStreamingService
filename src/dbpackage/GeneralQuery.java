package dbpackage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GeneralQuery {

    public static ResultSet generalCheck(Connection con, String colNames, String from, String condition){
        ResultSet rs = null;
        try{
            String query = String.format("SELECT %s FROM %s WHERE %s",colNames,from,condition);
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return rs;
    }

    public static boolean generalUpdate(Connection con, String tableName, String setValue, String condition){
        boolean res = false;
        try{
            String query = String.format("UPDATE %s SET %s WHERE %s",tableName,setValue,condition);
            Statement stmt = con.createStatement();
            res = stmt.execute(query);
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return res;
    }

}
