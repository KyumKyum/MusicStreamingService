package AdminFunc;

import ServiceFunc.MusicAttribute;
import dbpackage.DatabaseHandler;
import dbpackage.GeneralQuery;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static Utils.StringUtils.parse;

public class AdminPage {

    public static Scanner sc = new Scanner(System.in);

    public static void openingAdminPage(Connection con) {
        int instr = 1;

        while (instr != 0) {

            System.out.print("OPTIONS:" +
                    "\nPress 1 to user menu\nPress 2 to music menu\n" +
                    "Press 0 to Close the Admin Page.\nYour Option: ");

            instr = parse(sc.nextLine());

            switch (instr) {

                case -1 -> {
                    System.out.println("ERROR: WRONG INPUT\n");
                }

                case 0 -> {
                    System.out.println("Closing Admin Page...\n");
                }

                case 1 -> {
                    openUserOption(con);
                }

                case 2 -> {
                    openMusicOption(con);
                }
            }
        }
    }

    private static void openMusicOption(Connection con) {
        int option = 1;
        while (option != 0) {
            System.out.println("\nPress 1 to see all music list.\nPress 2 to search music for title.\n" +
                    "Press 3 to add music\nPress 4 to delete music\n" +
                    "Press 5 to modify music information\nPress 0 to return\nYour Option: ");
            option = parse(sc.nextLine());

            switch (option) {

                case -1 -> {
                    System.out.println("ERROR: WRONG OPTION");
                }

                case 0 -> {
                    System.out.println("Returning...");
                }

                case 1 -> {
                    seeAllMusicList(con);
                }

                case 2 -> {
                    seeMusicTitle(con);
                }

                case 3 -> {
                    addMusic(con);
                }

                case 4 -> {
                    deleteMusic(con);
                }

                case 5 -> {
                    modifyMusicInfo(con);
                }
            }
        }
    }

    private static void seeAllMusicList(Connection con) {
        try {
            ResultSet rs = GeneralQuery.generalCheck(con, "*", DatabaseHandler.MUSIC);
            System.out.println();

            List<MusicAttribute> musicAttributes = MusicAttribute.getAllTypes();

            while (rs.next()) {
                for (MusicAttribute musicAttribute : musicAttributes) {
                    System.out.print(rs.getString(musicAttribute.getAttribute()) + " | ");
                }
                System.out.print("\n");
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void seeMusicTitle(Connection con) {

        System.out.print("Search For...(Title): ");
        String title = sc.nextLine();

        try {
            ResultSet rs = GeneralQuery.generalCheck(con, "*", DatabaseHandler.MUSIC, "Title LIKE '%" + title + "%'");
            System.out.println();

            List<MusicAttribute> musicAttributes = MusicAttribute.getAllTypes();

            while (rs.next()) {
                for (MusicAttribute musicAttribute : musicAttributes) {
                    System.out.print(rs.getString(musicAttribute.getAttribute()) + " | ");
                }
                System.out.print("\n");
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addMusic(Connection con) {
        System.out.println("\nWrite necessary information to add new music(* to cancel): ");

        List<MusicAttribute> musicAttributes = MusicAttribute.getInputTypes();
        ArrayList<String> musicInfo = new ArrayList<>(7);
        boolean canceled = false;

        for (MusicAttribute musicAttribute : musicAttributes) {
            System.out.print(musicAttribute.getAttribute() + ": ");
            String data = sc.nextLine();

            if (data.equals("*")) {
                canceled = true;
                break;
            } else {
                musicInfo.add(data);
            }
        }

        String insertValue = "";

        if (canceled) {
            System.out.println("Process Canceled.\n");
        } else {
            System.out.println("New Music Information");
            for (String info : musicInfo) {
                System.out.print(info + " | ");
                insertValue = insertValue.concat("'" + info + "', ");
            }
            insertValue = insertValue.trim().substring(0, insertValue.lastIndexOf(","));
            confirmMusicAddition(con, insertValue, musicInfo.get(6));
        }
    }

    private static void confirmMusicAddition(Connection con, String musicInfo, String url) {
        //System.out.println(musicInfo);

        System.out.println("\nAre the information valid? It is okay to add? (Y to confirm): ");
        Character option = sc.nextLine().charAt(0);

        if (option.equals('y') || option.equals('Y')) {
            boolean res = GeneralQuery.generalInsert(con, DatabaseHandler.MUSIC, "Title, Artist, Prod, Playtime, Genre, Released, URL", musicInfo);
            ResultSet rs = GeneralQuery.generalCheck(con, "IDX", DatabaseHandler.MUSIC, "URL = '" + url + "'");

            try {
                if (res && rs.next()) {
                    int idx = rs.getInt("IDX");
                    System.out.println("SUCCESSFULLY ADDED\nThe index for new music is " + idx + ".\n");
                }

                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("RESULT: ADDITION CANCELED");
        }
    }

    private static void deleteMusic(Connection con) {
        System.out.println("Write index number to delete: ");
        int idx = sc.nextInt();
        sc.nextLine();

        ResultSet rs = GeneralQuery.generalCheck(con, "*", DatabaseHandler.MUSIC, "IDX = " + idx);
        List<MusicAttribute> musicAttributes = MusicAttribute.getGeneralTypes();

        try {
            if (rs.next()) {
                System.out.println("Music Information: ");
                for (MusicAttribute musicAttribute : musicAttributes) {
                    System.out.print(rs.getString(musicAttribute.getAttribute()) + " | ");
                }
                confirmDeleteMusic(con, idx);
            } else {
                System.out.println("REQUEST ABORTED: NO SUCH ID EXISTS.\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void confirmDeleteMusic(Connection con, int idx) {
        System.out.print("Are you sure? Current decision cannot be undone! (Y: Yes/N: No): ");
        Character decision = sc.nextLine().charAt(0);
        if (decision.equals('y') || decision.equals('Y')) {
            GeneralQuery.generalDelete(con, DatabaseHandler.MUSIC, "IDX = " + idx);
            System.out.println("Deleted");
        } else {
            System.out.println("REQUEST ABORTED: CANCELED\n");
        }
    }

    private static void modifyMusicInfo(Connection con) {
        System.out.print("Enter target index: ");

        Integer target = parse(sc.nextLine());

        if (target.equals(-1)) {
            System.out.println("RESULT:DENIED - Please enter integer index.");
        } else {
            try {
                ResultSet rs = GeneralQuery.generalCheck(con, "*", DatabaseHandler.MUSIC, "IDX = " + target);
                List<MusicAttribute> musicAttributes = MusicAttribute.getAllTypes();

                while (rs.next()) {
                    for (MusicAttribute musicAttribute : musicAttributes) {
                        System.out.print(rs.getString(musicAttribute.getAttribute()) + " | ");
                    }
                    System.out.print("\n");
                }
                modify(con, target);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    private static void modify(Connection con, Integer target) {
        String option = "";
        String changeTo = "";

        List<MusicAttribute> list = MusicAttribute.getGeneralTypes();

        System.out.println("Attribute List:");
        for (MusicAttribute musicAttribute : list) {
            System.out.print(musicAttribute.getAttribute() + " | ");
        }
        System.out.println();

        boolean exists = false;
        boolean cancel = false;

        System.out.println("Write Correct attribute name to change (Enter * to cancel.)");
        option = sc.nextLine();

        if (option.equals("*")) {
            cancel = true;
        } else {
            for (MusicAttribute musicAttribute : list) {
                if (option.equals(musicAttribute.getAttribute())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                System.out.println("ERROR: Not Available Attribute");
                cancel = true;
            } else {
                System.out.print("Write new value for corresponding Attribute: ");
                changeTo = sc.nextLine();
            }
        }

        if (cancel) {
            System.out.println("RESULT: DENIED - Process Canceled");
        } else {
            confirmMusicChange(con,changeTo,option,target);
        }
    }

    private static void confirmMusicChange(Connection con, String changeTo, String option, Integer target) {
        System.out.println("Confirm the changes? (Y to confirm): ");
        Character confirm = sc.nextLine().charAt(0);

        if(confirm.equals('y') || confirm.equals('Y')){
            boolean result = GeneralQuery.generalUpdate(con, DatabaseHandler.MUSIC,
                    option + " = '" + changeTo + "'", "IDX = " + target);

            if (result) {
                System.out.println("Successful!");
            }
        }else{
            System.out.println("Process Canceled");
        }
    }

    private static void openUserOption(Connection con) {
        int option = 1;
        while (option != 0) {
            System.out.println("Press 1 to get all user info\nPress 2 to get specific user info\n" +
                    "Press 3 to modify user's nickname\nPress 4 to delete user\nPress 0 to return\nYour Option: ");
            option = parse(sc.nextLine());

            switch (option) {
                case -1 -> {
                    System.out.println("ERROR: WRONG OPTION");
                }

                case 0 -> {
                    System.out.println("Returning...");
                }

                case 1 -> {
                    getAllUserInfo(con);
                }

                case 2 -> {
                    getUserInfo(con);
                }

                case 3 -> {
                    modifyUserInfo(con);
                }

                case 4 -> {
                    deleteUser(con);
                }
            }
        }
    }

    private static void deleteUser(Connection con) {
        System.out.print("Target User ID: ");
        String id = sc.nextLine();

        ResultSet rs = GeneralQuery.generalCheck(con, "*", DatabaseHandler.ACCOUNT, "ID = " + "'" + id + "'");

        try {
            if (rs.next()) {
                confirmDeleteUser(con, id);
            } else {
                System.out.println("REQUEST ABORTED: NO SUCH ID EXISTS.\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void confirmDeleteUser(Connection con, String id) {
        System.out.print("Are you sure? Current decision cannot be undone! (Y: Yes/N: No): ");
        Character decision = sc.nextLine().charAt(0);
        if (decision.equals('y') || decision.equals('Y')) {
            GeneralQuery.generalDelete(con, DatabaseHandler.ACCOUNT, "ID = " + "'" + id + "'");
            System.out.println("Deleted");
        } else {
            System.out.println("REQUEST ABORTED: CANCELED\n");
        }
    }

    private static void modifyUserInfo(Connection con) {
        System.out.print("Target User ID: ");
        String id = sc.nextLine();

        ResultSet rs = GeneralQuery.generalCheck(con, "*", DatabaseHandler.ACCOUNT, "ID = " + "'" + id + "'");

        try {
            if (rs.next()) {
                System.out.print("New User ID: ");
                String newId = sc.nextLine();
                ResultSet checkDup = GeneralQuery.generalCheck(con, "*", DatabaseHandler.ACCOUNT, "ID = " + "'" + newId + "'");
                confirmModification(con, checkDup, id, newId);
            } else {
                System.out.println("REQUEST ABORTED: NO SUCH ID EXISTS.\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void confirmModification(Connection con, ResultSet checkDup, String id, String newId) {
        try {
            if (!checkDup.next()) {
                System.out.print("Are you sure to change ID '" + id + "' to '" + newId + "'? (Y to confirm.): ");
                Character confirm = sc.nextLine().charAt(0);
                if (confirm.equals('y') || confirm.equals('Y')) {
                    GeneralQuery.generalUpdate(con, DatabaseHandler.ACCOUNT, "ID = " + "'" + newId + "'", "ID = " + "'" + id + "'");
                    System.out.println("REQUEST SUCCESS: UPDATED\n");
                } else {
                    System.out.println("Canceled.");
                }
            } else {
                System.out.println("REQUEST ABORTED: SAME ID EXISTS\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void getUserInfo(Connection con) {
        System.out.print("Search for...(ID): ");
        String target = sc.nextLine();

        ResultSet rs = GeneralQuery.generalCheck(con, "ID,name,Nickname,age,Email", DatabaseHandler.PROFILE, "ID = " + "'" + target + "'");
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

    private static void getAllUserInfo(Connection con) {
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

}
