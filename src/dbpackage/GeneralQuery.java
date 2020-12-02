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

    public static ResultSet generalCheck(Connection con, String colNames, String from){
        ResultSet rs = null;
        try{
            String query = String.format("SELECT %s FROM %s",colNames,from);
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return rs;
    }

    public static ResultSet generalCheckByOrder(Connection con, String colNames, String from, String orderCondition, String limit){
        ResultSet rs = null;
        try{
            String query = String.format("SELECT %s FROM %s ORDER BY %s LIMIT %s",colNames,from,orderCondition,limit);
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
            stmt.execute(query);
            res = true;
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    public static boolean generalInsert(Connection con, String tableName, String values){
        boolean res = false;
        try{
            String query = String.format("INSERT INTO %s VALUES(%s)",tableName,values);
            Statement stmt = con.createStatement();
            res = stmt.execute(query);
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    public static boolean generalInsert(Connection con, String tableName, String columns, String values){
        boolean res = false;
        try{
            String query = String.format("INSERT INTO %s(%s) VALUES(%s)",tableName,columns,values);
            Statement stmt = con.createStatement();
            stmt.execute(query);
            res = true;
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return res;
    }

    public static void generalDelete(Connection con, String tableName, String condition){
        try{
            String query = String.format("DELETE FROM %s WHERE %s",tableName,condition);
            Statement stmt = con.createStatement();
            stmt.execute(query);
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

}
