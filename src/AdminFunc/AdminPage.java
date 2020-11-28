package AdminFunc;

import dbpackage.GeneralQuery;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Scanner;

public class AdminPage {

    private final static String USER = "serviceuser";
    private final static String ACCOUNT = "account";
    private final static String ADMIN = "admin";
    private final static String PROFILE = "profile";

    public static Scanner sc = new Scanner(System.in);

    public static void openingAdminPage(Connection con) {
        Character instr = '\0';

        while (!instr.equals('0')) {

            System.out.print("OPTIONS:\nPress 1 to get all user info\nPress 2 to get specific user info\n" +
                    "Press 0 to Close the Admin Page.\nYour Option: ");
            instr = sc.nextLine().charAt(0);

            switch (instr) {
                case '0' -> {
                    System.out.println("Closing Admin Page...\n");
                }

                case '1' -> {
                    System.out.println("\n ID / Name / Nickname / Age / Email");
                    ResultSet rs = AdminFunc.queryAllUser(con);
                    if (!Objects.isNull(rs)) {
                        try {
                            while (rs.next()) {
                                String print = String.format("%s / %s / %s / %s / %s", rs.getString("ID"), rs.getString("name"), rs.getString("Nickname"),
                                        String.valueOf(rs.getInt("age")), rs.getString("Email"));
                                System.out.println(print);
                            }
                            rs.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }

                case '2' -> {
                    System.out.print("Search for...(ID): ");
                    String target = sc.nextLine();


                    ResultSet rs = GeneralQuery.generalCheck(con, "ID,name,Nickname,age,Email", PROFILE, "ID = " + "'" + target + "'");
                    try {
                        if (!Objects.isNull(rs) && rs.next()) {
                            System.out.println("Account Found!");

                            System.out.println("\n ID / Name / Nickname / Age / Email");
                            rs.beforeFirst();
                            while (rs.next()) {
                                String print = String.format("%s / %s / %s / %s / %s", rs.getString("ID"), rs.getString("name"), rs.getString("Nickname"),
                                        String.valueOf(rs.getInt("age")), rs.getString("Email"));
                                System.out.println(print);
                            }
                            rs.close();
                        } else {
                            System.out.println("ERROR: There is no such account with id '" + target + ".");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
