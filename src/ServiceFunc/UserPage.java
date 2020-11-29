package ServiceFunc;

import dbpackage.DatabaseHandler;
import dbpackage.DatabaseQuery;
import dbpackage.GeneralQuery;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserPage {

    public static Scanner sc = new Scanner(System.in);

    //userInfo - 0: Nickname, 1: ID, 2: PW 3: UserIndex
    public static void openingUserPage(Connection con, ArrayList<String> userInfo) {
        System.out.println("\nWELCOME! " + userInfo.get(0));

        Character instr = '\0';

        while (!instr.equals('0')) {
            Vector<String> vec = new Vector<>(8);
            vec.add(0, "Title");
            vec.add(1, "Artist");
            vec.add(2, "Prod");
            vec.add(3, "Playtime");
            vec.add(4, "Genre");
            vec.add(5, "Released");
            vec.add(6, "T_played");
            vec.add(7, "URL");

            System.out.println("\n-" + userInfo.get(0) + "'s Page-");
            System.out.print("Press 1 to search music.\nPress 2 to see my playlist\n" +
                    "Press 3 to configure your profile.\npress 4 to sign out\nYour Option: ");
            instr = sc.nextLine().charAt(0);

            switch (instr) {

                case '1' -> {

                    Character option = '\0';

                    while (!option.equals('0')) {
                        System.out.print("\nPress 1 to search music by title.\nPress 0 to exit.\nYour Choice");
                        option = sc.nextLine().charAt(0);

                        switch (option) {
                            case '0' -> {
                                System.out.println("Canceled.\n");
                            }

                            case '1' -> {
                                System.out.print("Enter Title: ");
                                String title = sc.nextLine();
                                ArrayList<ResultSet> resultSets = UserFunction.searchMusic(con, "Title", "'" + title + "%'");
                                ResultSet num = resultSets.get(0);
                                ResultSet queryResult = resultSets.get(1);
                                boolean noResult = false;

                                try {
                                    while (num.next()) {
                                        Integer number = num.getInt("number");
                                        System.out.println(number + " result(s) found.");

                                        noResult = number.equals(0);
                                    }

                                    System.out.print("Index Number | ");
                                    for (int i = 0; i < vec.size() - 2; i++) {
                                        System.out.print(vec.get(i) + " | ");
                                    }
                                    System.out.println("\n");

                                    while (queryResult.next()) {
                                        System.out.print(queryResult.getInt("IDX") + " | ");
                                        for (int i = 0; i < vec.size() - 2; i++) {
                                            System.out.print(queryResult.getString(vec.get(i)) + " | ");
                                        }
                                        System.out.print("\n");
                                    }

                                    if (!noResult) {
                                        System.out.println("Enter 'index number' of music you want to play (Enter * to return): ");
                                        String target = sc.nextLine();

                                        if (target.equals("*"))
                                            System.out.print("Return to music search page...\n");

                                        else {
                                            UserFunction.playMusic(con, target);
                                        }
                                    }

                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

                case '2' -> {
                    try {
                        ArrayList<ResultSet> rs = UserFunction.searchPlaylist(con, Integer.parseInt(userInfo.get(3)));
                        ResultSet num = rs.get(0);
                        ResultSet playList = rs.get(1);

                        boolean noResult = false;


                        while (num.next()) {
                            Integer number = num.getInt("number");
                            System.out.println(number + " result(s) found.");

                            noResult = number.equals(0);
                        }

                        if (noResult) {
                            System.out.println("No Playlist Found. Create new playlist? (Y: Yes/N: No): ");
                            Character createNew = sc.nextLine().charAt(0);

                            if (createNew.equals('y') || createNew.equals('Y')) {
                                UserFunction.createPlaylist(con, Integer.parseInt(userInfo.get(3)));
                            } else {
                                System.out.println("Returning to user page...");
                            }

                        } else {
                            System.out.println("PLAYLIST NAME (NUMBER OF MUSICS)");
                            while (playList.next()) {

                                int pidx = playList.getInt("PIDX");
                                System.out.println(playList.getString("Playlist_name") +
                                        " (" + UserFunction.checkPLMusicNum(con,pidx)  +")");
                            }
                            System.out.println("\nEnter playlist name you want to listen. (Press * to cancel)");
                            sc.nextLine();
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }

                case '3' -> {
                    userProfile(con, userInfo);
                }

                case '4' -> {
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

                        DatabaseQuery.updateProfile(con, DatabaseHandler.USER, "name", "'" + newName + "'", "SSN = " + curSsn);
                        DatabaseQuery.updateProfile(con, DatabaseHandler.USER, "age", newAge, "SSN = " + curSsn);

                        System.out.println("Change Applied!\n");
                        instr = '0';
                    }

                    case '3' -> {
                        String newEmail = null;
                        System.out.print("Your new Email: ");
                        newEmail = sc.nextLine();

                        ResultSet checkedResult = GeneralQuery.generalCheck(con, "email", DatabaseHandler.USER, "email = '" + newEmail + "'");

                        if (!checkedResult.next()) {
                            DatabaseQuery.updateProfile(con, DatabaseHandler.USER, "email", "'" + newEmail + "'", "SSN = " + curSsn);
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
                        else {
                            String same = null;
                            System.out.print("Your new password: ");
                            newPassword = sc.nextLine();
                            System.out.print("Your new password (confirm): ");
                            same = sc.nextLine();

                            if (newPassword.equals(same)) {
                                DatabaseQuery.updateProfile(con, DatabaseHandler.ACCOUNT, "PW", "'" + newPassword + "'", "Ussn = " + curSsn);
                                System.out.println("\nChange Applied!\n");
                            } else {
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
                        if (answer.equals('y') || answer.equals('Y')) {
                            DatabaseQuery.updateProfile(con, DatabaseHandler.ACCOUNT, "Nickname", "'" + newNickName + "'", "Ussn = " + curSsn);
                            System.out.println("\nChange Applied!\n");
                        } else {
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
