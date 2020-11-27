package dbpackage;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseQuery {
    private final static String USER = "serviceuser";
    private final static String ACCOUNT = "account";
    private final static String ADMIN = "admin";
    private final static String PROFILE = "profile";


    //FIND
    public static ResultSet findUser (Connection con, String ssn) {
        ResultSet rs = null;
        try{
            String query = String.format("SELECT * FROM %s WHERE SSN = %s",USER,"'"+ssn+"'");
            Statement stmt = con.prepareStatement(query);
            rs = stmt.executeQuery(query);
            stmt.close();
            //rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static ResultSet findAccount (Connection con, String colNames, String ssn) {//User
        ResultSet rs = null;
        try{
            String query = String.format("SELECT %s FROM %s WHERE Ussn = %s",colNames,ACCOUNT,"'"+ssn+"'");
            Statement stmt = con.prepareStatement(query);
            rs = stmt.executeQuery(query);
            stmt.close();
            //rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static ResultSet findAccount (Connection con, String colNames, String ID, String PW, String SerialNum) {//Admin
        ResultSet rs = null;
        try{
            String query = String.format("SELECT %s FROM %s WHERE AID = %s AND APW = %s AND SerialNum = %s",colNames,ADMIN,ID,PW,SerialNum);
            Statement stmt = con.prepareStatement(query);
            rs = stmt.executeQuery(query);
            stmt.close();
            //rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public static ResultSet findProfile (Connection con, String ID){
        ResultSet rs = null;

        try{
            ID = "'"+ID+"'";
            String query = String.format("SELECT * from %s where ID = %s",PROFILE,ID);
            Statement stmt = con.prepareStatement(query);
            rs = stmt.executeQuery(query);
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

        return rs;
    }

    //Check
    public static ResultSet checkAccount(Connection con,String colNames, String id){
        //boolean returnVal = false;
        ResultSet rs = null;
        try{
            String query = String.format("SELECT %s FROM %s WHERE ID = %s",colNames,ACCOUNT,"'"+id+"'");
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            //returnVal = !(rs.next()); //If the id exist, returns false
            stmt.close();
            //rs.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return rs;
    }

    public static ResultSet checkAccount(Connection con,String colNames, String id, String password){
        //boolean returnVal = false;
        ResultSet rs = null;
        try{
            String query = String.format("SELECT %s FROM %s WHERE ID = %s AND PW = %s",colNames,ACCOUNT,"'"+id+"'","'"+password+"'");
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery(query);
            //returnVal = !(rs.next()); //If the id exist, returns false
            stmt.close();
            //rs.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return rs;
    }

    //Insert
    public static void insertNewUser(Connection con, ArrayList<String> userInfo){
       try{
           String query = String.format("INSERT INTO %s VALUES (?,?,?,?)",USER);
           PreparedStatement pstmt = con.prepareStatement(query);
           for (int i = 1; i <= 4; i++){
               if(i != 3)
                   pstmt.setString(i,userInfo.get(i-1));
               else //Age is integer information
                   pstmt.setInt(i,Integer.parseInt(userInfo.get(i-1)));
           }

           pstmt.execute();
           pstmt.close();

       }catch (SQLException e){
           e.printStackTrace();
       }
    }

    public static void insertNewAccount(Connection con, ArrayList<String> accountInfo, String ssn){
        try{
            String query = String.format("INSERT INTO %s(ID,PW,Nickname,Ussn) VALUES (?,?,?,?)",ACCOUNT);
            PreparedStatement pstmt = con.prepareStatement(query);

            for(int i = 1; i <= 3; i++){
                    pstmt.setString(i,accountInfo.get(i-1));
            }
            pstmt.setString(4,ssn);

            pstmt.execute();
            pstmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            System.out.println("Success! New Account Generated! :)");
        }
    }

    //Update
    public static void updateProfile(Connection con, String from, String target, String info, String ssn){
        try{
            String query = String.format("UPDATE %s SET %s = %s WHERE %s",from,target,info,ssn);
            Statement stmt = con.createStatement();
            stmt.execute(query);
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void updateProfile(Connection con, String from, String target, int info, String ssn){
        try{
            String query = String.format("UPDATE %s SET %s = %d WHERE %s",from,target,info,ssn);
            Statement stmt = con.createStatement();
            stmt.execute(query);
            stmt.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
