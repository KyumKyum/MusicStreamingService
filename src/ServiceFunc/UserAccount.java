package ServiceFunc;

import dbpackage.DatabaseQuery;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

public class UserAccount {
    public static Scanner sc = new Scanner(System.in);

    public static void findAccount(Connection con) {

        Integer instr = -1;
        String SSN = null;
        String ID = null;

        do {
            ResultSet queryResult = null;
            System.out.println("\n- FIND MY ACCOUNT -");
            System.out.println("0. RETURN\n1.FIND MY ID\n2.FIND MY PASSWORD\nYOUR OPTION: ");
            instr = Integer.parseInt(sc.next());
            sc.nextLine();

            switch (instr) {
                case 0:
                    break;

                case 1:
                    System.out.print("Find my ID- Enter your Social Security Number (SSN): ");
                    SSN = sc.nextLine();
                    queryResult = DatabaseQuery.findAccount(con, "ID", SSN);

                    try {
                        while (queryResult.next()) {
                            ID = queryResult.getString("ID");
                        }

                        if (!(Objects.isNull(ID))) {
                            System.out.println("ID Found: " + ID);
                        } else {
                            System.out.println("Sorry, but your SSN is not available.");
                        }
                        queryResult.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    String queryPassword = null;
                    System.out.println("Find my password- Enter your Social Security Number (SSN): ");
                    SSN = sc.nextLine();
                    System.out.println("Enter your ID: ");
                    ID = sc.nextLine();

                    queryResult = DatabaseQuery.findAccount(con,"*",SSN);

                    try{
                        String queryId = null;
                        while(queryResult.next()){
                            queryId = queryResult.getString("ID");
                            queryPassword = queryResult.getString("PW");
                        }

                        if(!(Objects.isNull(queryId))){
                            if(queryId.equals(ID))
                                System.out.println("Password Found: " + queryPassword);
                            else
                                System.out.println("Sorry, but your SSN or ID is not available.");
                        }else{
                            System.out.println("Sorry, but your SSN or ID is not available.");
                        }
                        queryResult.close();
                    }catch (SQLException e){
                        e.printStackTrace();
                    }
            }
        } while (!instr.equals(0));
        System.out.println();
    }
}
