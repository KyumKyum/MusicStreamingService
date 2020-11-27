package ServiceFunc;

import dbpackage.DatabaseQuery;
import dbpackage.GeneralQuery;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserPage {
    private final static String USER = "serviceuser";
    private final static String ACCOUNT = "account";
    private final static String ADMIN = "admin";
    private final static String PROFILE = "profile";

    public static Scanner sc = new Scanner(System.in);

    //userInfo - 0: Nickname, 1: ID, 2: PW
    public static void openingUserPage(Connection con, ArrayList<String> userInfo) {
        System.out.println("\nWELCOME! " + userInfo.get(0));

        Character instr = '\0';

        while (!instr.equals('0')) {
            System.out.println("\n-" + userInfo.get(0) + "'s Page-");
            System.out.print("Press 1 to search music.\nPress 2 to configure your profile.\npress 3 to sign out\nYour Option: ");
            instr = sc.nextLine().charAt(0);

            switch (instr) {

                case '2' -> {
                    userProfile(con, userInfo);
                }

                case '3' -> {
                    System.out.print("Are you Sure? (Enter Yes or y to confirm.)");
                    instr = sc.nextLine().charAt(0);

                    if (instr.equals('y') || instr.equals('Y'))
                        instr = '0';
                    else
                        System.out.println("Return to the user page...");
                }

                default -> {
                    System.out.print("ERROR: WRONG INPUT");
                    instr = '\0';
                }
            }
        }
        System.out.println("See you later, " + userInfo.get(0) + "!");
    }

    private static void userProfile(Connection con, ArrayList<String> userInfo) {
        HashMap<String, String> profile = new LinkedHashMap<>();
        Vector<String> vec = new Vector<>();
        vec.add(0, "SSN");
        vec.add(1, "name");
        vec.add(2, "age");
        vec.add(3, "email");
        vec.add(4, "ID");
        vec.add(5, "PW");
        vec.add(6, "Nickname");

        try {
            ResultSet rs = DatabaseQuery.findProfile(con, userInfo.get(1));
            while (rs.next()) {

                for (int i = 0; i < vec.size(); i++) {
                    if (i != 2) {
                        profile.put(vec.get(i), rs.getString(vec.get(i)));
                    } else
                        profile.put(vec.get(i), String.valueOf(rs.getInt(vec.get(i))));
                }
            }
            rs.close();

            String curSsn = "'" + profile.get("SSN") + "'";

            Character instr = '\0';
            while (!instr.equals('0')) {

                System.out.print("\nPROFILE OPTIONS:\nPress 1 to see my profile.\nPress 2 to change personal information(name,age)." +
                        "\nPress 3 tp change email address." +
                        "\nPress 4 to change Password.\nPress 5 to change Nickname.\nPress 0 to return.\nYour Choice: ");

                instr = sc.nextLine().charAt(0);

                switch (instr) {
                    case '0' -> {
                        System.out.println("Return to user page...");
                    }

                    case '1' -> {
                        System.out.println("\n- PROFILE -");
                        profile.forEach((key, value) -> System.out.println(key + ": " + value));
                        instr = '0';
                    }

                    case '2' -> {
                        String newName = null;
                        int newAge = 0;
                        System.out.print("Your new name: ");
                        newName = sc.nextLine();
                        System.out.print("Your new age: ");
                        newAge = Integer.parseInt(sc.nextLine());

                        DatabaseQuery.updateProfile(con, USER, "name", "'" + newName + "'", "SSN = " + curSsn);
                        DatabaseQuery.updateProfile(con, USER, "age", newAge, "SSN = " + curSsn);

                        System.out.println("Change Applied!\n");
                        instr = '0';
                    }

                    case '3' -> {
                        String newEmail = null;
                        System.out.print("Your new Email: ");
                        newEmail = sc.nextLine();

                        ResultSet checkedResult = GeneralQuery.generalCheck(con, "email", USER, "email = '" + newEmail + "'");

                        if (!checkedResult.next()) {
                            DatabaseQuery.updateProfile(con, USER, "email", "'" + newEmail + "'", "SSN = " + curSsn);
                            System.out.println("\nChange Applied!\n");
                        } else {
                            System.out.println("\nRequest Denied: The email " + newEmail + " is currently used by other user\n");
                        }
                        instr = '0';
                    }

                    case '4' -> {
                        String oldPassword = null;
                        String newPassword = null;

                        System.out.print("Your current password: ");
                        oldPassword = sc.nextLine();

                        if (!(oldPassword.equals(profile.get("PW"))))
                            System.out.println("Request Denied: Didn't match with original password\n");
                        else{
                            String same = null;
                            System.out.print("Your new password: ");
                            newPassword = sc.nextLine();
                            System.out.print("Your new password (confirm): ");
                            same = sc.nextLine();

                            if(newPassword.equals(same)){
                                DatabaseQuery.updateProfile(con,ACCOUNT, "PW","'"+newPassword+"'","Ussn = " + curSsn);
                                System.out.println("\nChange Applied!\n");
                            }else{
                                System.out.println("ERROR: New password didn't match.\n");
                            }
                        }

                        instr = '0';
                    }

                    case '5' -> {
                        String newNickName = null;
                        Character answer = '\0';
                        System.out.print("Your new Nickname: ");
                        newNickName = sc.nextLine();

                        System.out.print("Your new Nickname is " + newNickName + ". Am I right? (Enter y to say Yes): ");
                        answer = sc.nextLine().charAt(0);
                        if(answer.equals('y')||answer.equals('Y')){
                            DatabaseQuery.updateProfile(con, ACCOUNT, "Nickname", "'"+newNickName+"'","Ussn = " + curSsn);
                            System.out.println("\nChange Applied!\n");
                        }else{
                            System.out.println("Cancel the changes...");
                        }
                        instr = '0';
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
