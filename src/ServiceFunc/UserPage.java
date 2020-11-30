package ServiceFunc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Vector;

import dbpackage.DatabaseHandler;
import dbpackage.DatabaseQuery;
import dbpackage.GeneralQuery;

public class UserPage {

    public static Scanner sc = new Scanner(System.in);

    //userInfo - 0: Nickname, 1: ID, 2: PW 3: UserIndex
    public static void openUserPage(Connection con, ArrayList<String> userInfo) {
        try {
            System.out.println("\nWELCOME! " + userInfo.get(0));
            inputUserPageMenu(con, userInfo);
            System.out.println("See you later, " + userInfo.get(0) + "!");
        } catch (SQLException e) {
            e.getStackTrace();
        }
    }

    private static void inputUserPageMenu(Connection con, ArrayList<String> userInfo) throws SQLException {
        int instr = 1;

        while (instr != 0) {
            System.out.println("\n-" + userInfo.get(0) + "'s Page-");
            System.out.print("Press 1 to search music.\nPress 2 to see my playlist\n" +
                "Press 3 to configure your profile.\npress 4 to sign out\nYour Option: ");
            instr = parse(sc.nextLine());

            if (instr == -1) {
                System.out.print("ERROR: WRONG INPUT");
                continue;
            }

            if (instr == 1) {
                searchMusic(con);
            }

            if (instr == 2) {
                findPlayList(con, userInfo);
            }

            if (instr == 3) {
                userProfile(con, userInfo);
            }

            if (instr == 4) {
                if (signOut()) {
                    break;
                }
            }
        }
    }

    private static void searchMusic(Connection con) throws SQLException {
        int option = 1;

        while (option != 0) {
            System.out.print("\nPress 1 to search music by title.\nPress 0 to exit.\nYour Choice");
            option = parse(sc.nextLine());

            if (option == -1) {
                continue;
            }

            if (option == 0) {
                System.out.println("Canceled.\n");
            }

            if (option == 1) {
                System.out.print("Enter Title: ");
                String title = sc.nextLine();

                ArrayList<ResultSet> resultSets = UserFunction.searchMusic(
                    con, "Title", "'" + title + "%'");
                ResultSet rs = resultSets.get(0);
                ResultSet queryResult = resultSets.get(1);
                List<MusicAttribute> musicAttributes = MusicAttribute.getNoneTypes();

                System.out.print("Index Number | ");
                printMusicAttributes(queryResult, musicAttributes);

                while (queryResult.next()) {
                    System.out.print(queryResult.getInt("IDX") + " | ");
                    printMusicAttributes(queryResult, musicAttributes);
                }
                playMusic(con, rs);
            }
        }
    }

    private static void findPlayList(Connection con, ArrayList<String> userInfo) throws SQLException {
        int userIndex = Integer.parseInt(userInfo.get(3));
        ArrayList<ResultSet> rs = UserFunction.searchPlaylist(con, userIndex);
        ResultSet num = rs.get(0);
        ResultSet playList = rs.get(1);

        boolean noResult = hasResult(num);

        if (noResult) {
            System.out.println("No Playlist Found. Create new playlist? (Y: Yes/N: No): ");
            Character createNew = sc.nextLine().charAt(0);

            if (createNew.equals('y') || createNew.equals('Y')) {
                UserFunction.createPlaylist(con, userIndex);
            } else {
                System.out.println("Returning to user page...");
            }

        } else {
            String plTitle = null;

            do {
                System.out.println("PLAYLIST NAME (NUMBER OF MUSICS)");
                int pidx = -1;
                while (playList.next()) {

                    pidx = playList.getInt("PIDX");

                    System.out.println(playList.getString("Playlist_name") +
                        " (" + UserFunction.checkPLMusicNum(con, pidx) + ")");
                }

                System.out.println("\nEnter playlist name you want to listen. (Press * to cancel)");

                //                                while (Objects.isNull(plTitle))
                plTitle = sc.nextLine();

                Character option = '\0';
                while (!option.equals('0')) {
                    System.out.println("Playlist '" + plTitle + "'");

                    ResultSet musics = UserFunction.getPlaylist(con, plTitle, userIndex);

                    if (!Objects.isNull(musics)) {
                        musics.beforeFirst();
                        while (musics.next()) {
                            System.out.println(
                                musics.getInt("MUSICIDX") + " | " + musics.getString("Title")
                                    + " - " + musics.getString("Artist"));
                        }

                        System.out.println(
                            "Playlist Options: \nPress 1 to listen music in playlist.\nPress 2 to add music to current playlist\n"
                                +
                                "Press 3 to delete music in the playlist\nPress 0 to Return\nYour Option: ");

                        option = sc.nextLine().charAt(0);

                        switch (option) {
                            case '0' -> {
                                System.out.println("Returning...");
                            }

                            case '1' -> {

                            }

                            case '2' -> {
                                System.out.println("Write an index number of a music: ");
                                int midx = sc.nextInt();
                                sc.nextLine();

                                Integer result = UserFunction.addMusicToPlaylist(con, midx, pidx);

                                if (result.equals(0)) {
                                    System.out.println("Added!");
                                } else if (result.equals(-1))
                                    System.out.println("No music Found");
                                else if (result.equals(-2))
                                    System.out.println("Duplicate music exists in the same playlist!");
                            }
                        }
                    } else {
                        System.out.println("ERROR: There is no such playlist exists.");

                        break;
                    }
                }

            } while (!plTitle.equals("*"));
        }
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

                System.out.print(
                    "\nPROFILE OPTIONS:\nPress 1 to see my profile.\nPress 2 to change personal information(name,age)."
                        +
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

                        DatabaseQuery.updateProfile(con, DatabaseHandler.USER, "name", "'" + newName + "'",
                            "SSN = " + curSsn);
                        DatabaseQuery.updateProfile(con, DatabaseHandler.USER, "age", newAge, "SSN = " + curSsn);

                        System.out.println("Change Applied!\n");
                        instr = '0';
                    }

                    case '3' -> {
                        String newEmail = null;
                        System.out.print("Your new Email: ");
                        newEmail = sc.nextLine();

                        ResultSet checkedResult = GeneralQuery.generalCheck(con, "email", DatabaseHandler.USER,
                            "email = '" + newEmail + "'");

                        if (!checkedResult.next()) {
                            DatabaseQuery.updateProfile(con, DatabaseHandler.USER, "email", "'" + newEmail + "'",
                                "SSN = " + curSsn);
                            System.out.println("\nChange Applied!\n");
                        } else {
                            System.out.println(
                                "\nRequest Denied: The email " + newEmail + " is currently used by other user\n");
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
                                DatabaseQuery.updateProfile(con, DatabaseHandler.ACCOUNT, "PW", "'" + newPassword + "'",
                                    "Ussn = " + curSsn);
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

                        System.out.print(
                            "Your new Nickname is " + newNickName + ". Am I right? (Enter y to say Yes): ");
                        answer = sc.nextLine().charAt(0);
                        if (answer.equals('y') || answer.equals('Y')) {
                            DatabaseQuery.updateProfile(con, DatabaseHandler.ACCOUNT, "Nickname",
                                "'" + newNickName + "'", "Ussn = " + curSsn);
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

    private static void printMusicAttributes(ResultSet queryResult, List<MusicAttribute> musicAttributes) throws
        SQLException {
        for (MusicAttribute musicAttribute : musicAttributes) {
            System.out.print(queryResult.getString(musicAttribute.getAttribute()) + " | ");
        }
        System.out.println("\n");
    }

    private static void playMusic(Connection con, ResultSet rs) throws SQLException {
        if (hasResult(rs)) {
            System.out.println(
                "Enter 'index number' of music you want to play (Enter * to return): ");
            String target = sc.nextLine();

            if (target.equals("*")) {
                System.out.print("Return to music search page...\n");
                return;
            }
            UserFunction.playMusic(con, target);
        }
    }

    private static boolean hasResult(ResultSet num) throws SQLException {
        boolean result = false;

        while (num.next()) {
            Integer number = num.getInt("number");
            System.out.println(number + " result(s) found.");

            result = number.equals(0);
        }
        return !result;
    }

    private static boolean signOut() {
        System.out.print("Are you Sure? (Enter Yes or y to confirm.)");
        Character YorN = sc.nextLine().charAt(0);

        if (YorN.equals('y') || YorN.equals('Y')) {
            return true;
        } else {
            System.out.println("Return to the user page...");
        }
        return false;
    }

    private static int parse(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("숫자만 입력해주세요.");
        }
        return -1;
    }
}
