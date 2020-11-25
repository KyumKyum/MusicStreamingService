package dbpackage;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseQuery {
    private final static String USER = "serviceuser";
    private final static String ACCOUNT = "account";

    public static ResultSet findUser (Connection con, String ssn) {
        ResultSet rs = null;
        try{
            String query = String.format("SELECT * FROM %s WHERE SSN = %s",USER,"'"+ssn+"'");
            Statement stmt = con.prepareStatement(query);
            rs = stmt.executeQuery(query);
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

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

        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            System.out.println("Success! New Account Generated! :)");
        }
    }

}
