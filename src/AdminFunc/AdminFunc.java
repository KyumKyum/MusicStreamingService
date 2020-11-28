package AdminFunc;

import dbpackage.DatabaseQuery;

import java.io.Serial;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

public class AdminFunc {

    public static Scanner sc = new Scanner(System.in);
    private final static String PROFILE = "profile";


    public static void AdminAuth(Connection con){
        String AID = null;
        String APW = null;
        String serialNum = null;

        System.out.println("\n- ADMINISTRATOR MENU -\nAUTHENTICATION REQUIRED");

        System.out.print("Your ID: ");
        AID = sc.nextLine();

        System.out.print("Your Password: ");
        APW = sc.nextLine();

        System.out.print("Your Serial Code: ");
        serialNum = sc.nextLine();

        AID = "'" + AID + "'";
        APW = "'" + APW + "'";
        serialNum = "'" + serialNum + "'";

        try{
            String nickname = null;
            ResultSet rs = DatabaseQuery.findAccount(con,"Nickname", AID,APW,serialNum);
            while(rs.next()){
                nickname = rs.getString("Nickname");
            }

            if(!Objects.isNull(nickname)) {
                System.out.println("\nRESULT: AUTHORIZATION SUCCEED\nOPENING THE ADMIN PAGE...\nWELCOME " + nickname + "!\n");//Admin login succedded
                AdminPage.openingAdminPage(con);
            }
            else
                System.out.print("RESULT: AUTHORIZATION DENIED\nRETURNING TO THE MAIN PAGE...\n\n");

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static ResultSet queryAllUser(Connection con){
        try{
            String query = String.format("SELECT * FROM %s",PROFILE);
            Statement stmt = con.createStatement();
            return stmt.executeQuery(query);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}