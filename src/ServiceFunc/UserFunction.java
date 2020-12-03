package ServiceFunc;

import MediaFunc.MusicPlayer;
import com.sun.javafx.image.impl.General;
import dbpackage.DatabaseHandler;
import dbpackage.DatabaseQuery;
import dbpackage.GeneralQuery;
import org.mariadb.jdbc.internal.com.read.dao.Results;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserFunction {
    public static Scanner sc = new Scanner(System.in);


    //User Function: Register
    public static void register(Connection con) {

        Character instr = null;
        String dump;

        System.out.println(" - REGISTER -\n");

        System.out.println("Hello new visitor! ");
        System.out.print("Are you ready to join our service?");

        try {
            registerNew(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void registerNew(Connection con) throws SQLException {
        Vector<String> vec = new Vector<>(4);
        vec.add("Social Security Number(SSN)");
        vec.add("Name");
        vec.add("Age");
        vec.add("Email");

        System.out.println("Enter your information to create account! <Step 1/2>");
        ArrayList<String> userInfo = new ArrayList<>(4);


        for (int i = 0; i < vec.size(); i++) { //Get mandatory information: name, ssn, age
            String userInput = null;
            System.out.print("Your " + vec.elementAt(i) + ": ");
            do {
                userInput = sc.nextLine();
                if (userInput.trim().isEmpty())
                    System.out.println("ERROR: Current Information is mandatory.");
                else
                    userInfo.add(i, userInput);
            } while (userInput.trim().isEmpty());
        }

        if (!(DatabaseQuery.findUser(con, userInfo.get(0)).next())) {
            DatabaseQuery.insertNewUser(con, userInfo);
            System.out.println("Enter your information to create account! <Step 2/2>");
            registerAccount(con, userInfo.get(0));
        } else { //User Already Exist.
            System.out.println("You are user of the service already! Do you want to find your Account?\n(Enter Yes:y/NO:n)");
            String input = sc.next();
            sc.nextLine();
            Character instr = input.charAt(0);

            if (instr.equals('y') || instr.equals('Y')) {
                UserAccount.findAccount(con);
            }
        }
    }

    private static void registerAccount(Connection con, String ssn) {
        Vector<String> vec = new Vector<>(3);
        vec.add("ID");
        vec.add("Password");
        vec.add("Nickname");

        ArrayList<String> accountInfo = new ArrayList<>(3);

        while (true) {
            for (int i = 0; i < vec.size(); i++) { //Get mandatory information: name, ssn, age
                String userInput = null;
                System.out.print("Your " + vec.elementAt(i) + ": ");
                do {
                    userInput = sc.nextLine();
                    if (userInput.trim().isEmpty())
                        System.out.println("ERROR: Current Information is mandatory.");
                    else
                        accountInfo.add(i, userInput);
                } while (userInput.trim().isEmpty());
            }

            try {
                if (!((DatabaseQuery.checkAccount(con, "ID", accountInfo.get(0))).next())) {
                    DatabaseQuery.insertNewAccount(con, accountInfo, ssn);
                    break;
                } else {
                    System.out.println("Sorry, but the id " + accountInfo.get(0) + " is already exists.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //User Function: Sign in
    public static void signIn(Connection con) {
        System.out.println(" - SIGN IN -\n");

        Character instr = '\0';

        while (!instr.equals('0')) {
            System.out.print("Press 1 to sign in.\nPress 2 to find my account\nPress 0 to return (Go to main menu).\nYour Option: ");
            instr = sc.nextLine().charAt(0);

            switch (instr) {
                case '0' -> System.out.println("\nReturn to the main menu...\n");
                case '1' -> {
                    ArrayList<String> userInfo = getAuth(con);
                    if (!Objects.isNull(userInfo)) {
                        System.out.print("Signing in...\n");
                        UserPage.openUserPage(con, userInfo);
                        instr = '0';
                    } else
                        System.out.println("\nSign in Failed.\nHint: Maybe your ID or password is wrong, or escape command is entered (*).");
                }
                case '2' -> {
                    UserAccount.findAccount(con);
                }
                default -> System.out.println("\nERROR: Invalid Input");
            }
        }
    }

    private static ArrayList<String> getAuth(Connection con) {
        String ID = null;
        String PW = null;
        ArrayList<String> userInfo = new ArrayList<>(4);

        System.out.print("\nYour ID (Enter * to return): ");
        ID = sc.nextLine();
        if(ID.equals("*")){
            return null;
        }
        System.out.print("Your Password (Enter * to return): ");
        PW = sc.nextLine();
        if(PW.equals("*")){
            return null;
        }

        ResultSet rs = DatabaseQuery.checkAccount(con, "UserIndex, Nickname", ID, PW);

        try {
            if (rs.next()) {
                userInfo.add(0, rs.getString("Nickname"));
                userInfo.add(1, ID);
                userInfo.add(2, PW);
                userInfo.add(3, String.valueOf(rs.getInt("UserIndex")));

                return userInfo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<ResultSet> searchMusic(Connection con, String searchFor, String target) {
        ArrayList<ResultSet> results = new ArrayList<>(2);

        results.add(0, GeneralQuery.generalCheck(con, "COUNT(*) AS number", DatabaseHandler.MUSIC, searchFor + " LIKE " + target));
        results.add(1, GeneralQuery.generalCheck(con, "*", DatabaseHandler.MUSIC, searchFor + " LIKE " + target));

        return results;
    }

    public static ArrayList<ResultSet> searchPlaylist(Connection con, int idx) {
        ArrayList<ResultSet> results = new ArrayList<>(2);

        results.add(0, GeneralQuery.generalCheck(con, "COUNT(*) AS number", DatabaseHandler.PLAYLIST, "Owner_idx = " + idx));
        results.add(1, GeneralQuery.generalCheck(con, "*", DatabaseHandler.PLAYLIST, "Owner_idx = " + idx));

        return results;
    }

    public static Integer checkPLMusicNum(Connection con, int pidx) throws SQLException {
        ResultSet rs = GeneralQuery.generalCheck(con, "COUNT(*) AS number", DatabaseHandler.PLAYLIST_MUSIC, "PIDX = " + pidx);
        Integer musicNum = null;
        while (rs.next()) {
            musicNum = rs.getInt("number");
        }

        return musicNum;
    }

    public static void createPlaylist(Connection con, int idx) {
        //boolean success = false;

        System.out.print("\nEnter new playlist name: ");
        String newName = sc.nextLine();
        if (checkPlaylistDuplicate(con, idx, newName)) {
            GeneralQuery.generalInsert(con, DatabaseHandler.PLAYLIST, "Owner_idx, Playlist_name", idx + ", '" + newName + "'");
            System.out.println("Playlist Created!");
        }
    }
    //return success;


    public static boolean checkPlaylistDuplicate(Connection con, int uidx, String newName) {
        ResultSet rs = GeneralQuery.generalCheck(con, "*", DatabaseHandler.PLAYLIST, "Owner_idx = " + uidx + " AND Playlist_name = '" + newName + "'");
        try {
            if (rs.next()) {
                System.out.println("Request Denied: Duplicated playlist name for one user is not allowed.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static void changePlaylistName(Connection con, int pidx, String newName) {
        GeneralQuery.generalUpdate(con, DatabaseHandler.PLAYLIST, "Playlist_name = '" + newName + "'", "PIDX = " + pidx);
    }

    public static Integer getPlaylist(Connection con, String playlistName, int ownerIdx) throws SQLException {
        ResultSet rs = GeneralQuery.generalCheck(con, "PIDX", DatabaseHandler.PLAYLIST,
                "Playlist_name = '" + playlistName + "' AND Owner_idx = " + ownerIdx);
        Integer playlistIdx = -1;
        while (rs.next())
            playlistIdx = rs.getInt("PIDX");

        if (playlistIdx.equals(-1))
            return null;
        else
            return playlistIdx;

//        return GeneralQuery.generalCheck(con, "*", DatabaseHandler.PLAYLIST_MUSIC +
//                ", " + DatabaseHandler.MUSIC, "playlist_music.MUSICIDX = music.IDX AND playlist_music.PIDX = " + playlistIdx);
    }

    public static ResultSet getMusicList(Connection con, int pidx) {
        return GeneralQuery.generalCheck(con, "MUSICIDX", DatabaseHandler.PLAYLIST_MUSIC, "PIDX = " + pidx);
    }

    public static int addMusicToPlaylist(Connection con, int midx, int pidx) throws SQLException {
        ResultSet rs = GeneralQuery.generalCheck(con, "*", DatabaseHandler.MUSIC, "IDX = " + midx);

        if (!rs.next())
            return -1;
        else {
            ResultSet qrs = GeneralQuery.generalCheck(con, "*", DatabaseHandler.PLAYLIST_MUSIC, "MUSICIDX = " + midx + " AND PIDX = " + pidx);

            if (qrs.next())
                return -2;


            GeneralQuery.generalInsert(con, DatabaseHandler.PLAYLIST_MUSIC, "PIDX,MUSICIDX", pidx + "," + midx);
        }
        return 0;
    }

    public static void deleteMusicFromPlaylist(Connection con, int midx, int pidx) {
        GeneralQuery.generalDelete(con, DatabaseHandler.PLAYLIST_MUSIC, "MUSICIDX = " + midx + " AND PIDX = " + pidx);
    }

    public static void deletePlaylist(Connection con, int pidx){
        GeneralQuery.generalDelete(con,DatabaseHandler.PLAYLIST,"PIDX = " + pidx);
    }

    public static void playMusic(Connection con, String musicIdx, Integer userIndex) {

        try {
            ResultSet rs = GeneralQuery.generalCheck(con, "*", DatabaseHandler.MUSIC, "IDX = " + Integer.parseInt(musicIdx));
            String url = null;
            while (rs.next()) {
                int numPlayed = rs.getInt("T_played");
                GeneralQuery.generalUpdate(con, DatabaseHandler.MUSIC,
                        "T_played = " + String.valueOf(++numPlayed), "IDX = " + Integer.parseInt(musicIdx));
                url = rs.getString("URL");
                System.out.println(rs.getString("Title") + " - " + rs.getString("Artist"));
                System.out.println("Playtime: " + rs.getString("Playtime"));
            }

            ResultSet userRs = GeneralQuery.generalCheck(con,"*",DatabaseHandler.ACCOUNT_MUSIC,
                    "midx = "+ musicIdx + " AND uidx = " + userIndex);
            boolean firstlyPlayed = true;

            while(userRs.next()){
                int numPlayed = userRs.getInt("numPlayed");
                GeneralQuery.generalUpdate(con,DatabaseHandler.ACCOUNT_MUSIC,
                        "numPlayed = " + String.valueOf(++numPlayed),"midx = "+ musicIdx + " AND uidx = " + userIndex);
                firstlyPlayed = false;
            }

            if(firstlyPlayed){
                GeneralQuery.generalInsert(con,DatabaseHandler.ACCOUNT_MUSIC,
                        "midx, uidx, numPlayed",musicIdx + ", " +userIndex + ", 1");
            }

            if (!Objects.isNull(url)) {
                MusicPlayer musicPlayer = new MusicPlayer();
                musicPlayer.initialize(url);
                Character option = '\0';
                boolean playing = false;

                while (!option.equals('0')) {
                    System.out.println("\nPress 1 to play & pause.\nPress 2 to stop.\nPress 3 to reload.\nPress 0 to return\nYour Option: ");
                    option = sc.nextLine().charAt(0);

                    switch (option) {

                        case '0' -> {
                            System.out.println("Returning...");
                            musicPlayer.stop();
                        }
                        case '1' -> {
                            playing = !playing;
                            if (playing)
                                System.out.println("Playing...");
                            else
                                System.out.println("Pausing...");

                            musicPlayer.play(playing);
                        }

                        case '2' -> {
                            System.out.println("Stopping...");
                            playing = false;
                            musicPlayer.stop();
                        }

                        case '3' -> {
                            System.out.println("Reloading...");
                            playing = true;
                            musicPlayer.reload();
                        }
                    }
                }

            } else
                System.out.print("ERROR: Music not exists.");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}