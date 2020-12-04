package ServiceFunc;

import dbpackage.DatabaseHandler;
import dbpackage.DatabaseQuery;
import dbpackage.GeneralQuery;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static Utils.StringUtils.parse;
import static Utils.StringUtils.stringTrim;

public class UserPage {

    public static Scanner sc = new Scanner(System.in);

    //userInfo - 0: Nickname, 1: ID, 2: PW 3: UserIndex
    public static void openUserPage(Connection con, ArrayList<String> userInfo) {
        try {
            System.out.println("\nWELCOME! " + userInfo.get(0));
            inputUserPageMenu(con, userInfo);
            System.out.println("See you later, " + userInfo.get(0) + "!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void inputUserPageMenu(Connection con, ArrayList<String> userInfo) throws SQLException {
        int instr = 1;

        while (true) {
            System.out.println("\n-" + userInfo.get(0) + "'(s) Page-");
            System.out.print("Press 1 to search music.\nPress 2 to see my playlist\n" +
                    "Press 3 to configure your profile.\npress 0 to sign out\nYour Option: ");
            instr = parse(sc.nextLine());

            if (instr == -1) {
                System.out.print("ERROR: WRONG INPUT");
                continue;
            }

            if (instr == 1) {
                searchMusic(con,Integer.parseInt(userInfo.get(3)));
            }

            if (instr == 2) {
                findPlayList(con, userInfo);
            }

            if (instr == 3) {
                boolean check = userProfile(con, userInfo);
                if(!check){
                    break;
                }
            }

            if (instr == 0) {
                if (signOut()) {
                    break;
                }
            }
        }
    }

    private static void searchMusic(Connection con, Integer userIndex) throws SQLException {
        Integer option = 1;

        while (!option.equals(0)) {
            System.out.print("\nPress 1 to see all music listed\nPress 2 to see recommended musics.\n" +
                    "Press 3 to search music by title.\nPress 4 to search music by Artist\n" +
                    "Press 5 to find by Genre\nPress 6 to search directly by index\nPress 0 to exit.\nYour Choice: ");
            option = parse(sc.nextLine());

            if (option.equals(-1)) {
                System.out.println("ERROR: Invalid Input");
            }
            else if (option.equals(0)) {
                System.out.println("Canceled.\n");
            }
            else if (option.equals(1)) {
                listAllMusic(con);
            }
            else if(option.equals(2)){
                recommendationMenu(con,userIndex);
            }
            else if (option.equals(3)) {
                System.out.print("Enter Title: ");
                String title = stringTrim(sc.nextLine());
                title = title.trim().replace("'","").replace("\"","");
                searchMusicByTitle(con,title,userIndex);
            }
            else if (option.equals(4)){
                System.out.println("Enter Artist Name: ");
                String artist = stringTrim(sc.nextLine());
                artist = artist.trim().replace("'","").replace("\"","");
                searchMusicByArtist(con,artist,userIndex);
            }
            else if(option.equals(5)){
                System.out.println("Enter Genre: ");
                String genre = stringTrim(sc.nextLine());
                searchMusicByGenre(con,genre,userIndex);
            }else if(option.equals(6)){
                playMusic(con,userIndex);
            }
        }
    }

    private static void recommendationMenu(Connection con,int userIndex) {
        System.out.println("\nPress 1 to see Mostly played music by other users\nPress 2 to see Kyum's pick\nPress 0 to return.");
        Integer option = parse(sc.nextLine());

        if (option.equals(-1)) {
            System.out.println("ERROR: Invalid Input");
        } else if (option.equals(0)) {
            System.out.println("Canceled.\n");
        }else if(option.equals(1)){
            listMostPlayed(con,userIndex);
        }else if(option.equals(2)){
            recommendMusic(con, userIndex);
        }
    }

    private static void recommendMusic(Connection con, int userIndex) {
        System.out.println("Kyum is analyzing your favorite....\n");
        ResultSet rs = GeneralQuery.generalCheck(con,"Genre",DatabaseHandler.MUSIC_MOSTLY_LISTENED,
                "uidx = "+ userIndex +
                        " AND numPlayed = (SELECT MAX(numPlayed) FROM account_music where uidx = "+ userIndex +" ) LIMIT 0,1");
        String result = null;
        try{
            while(rs.next()){
                result = rs.getString("Genre");
            }

            if(Objects.isNull(result)){
                System.out.println("You haven't listened music at all! I cannot analyze your favorite... :(\n");
            }else{
                System.out.println("A-ha! You like " + result + " musics! Then, I will show some related musics....\n");
                searchMusicByGenre(con,result,userIndex);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void listMostPlayed(Connection con, Integer userIndex) {
        ResultSet rs = GeneralQuery.generalCheck(con,"*",DatabaseHandler.MUSIC,
                "T_played = (SELECT MAX(T_played) FROM " + DatabaseHandler.MUSIC +")");

        System.out.println("\nThis is the list of music mostly played by users!");

        System.out.println("\nIndex Number | Title | Artist | Produced by | Playtime | Genre | Released");
        listMusic(rs);

        ResultSet topRs = GeneralQuery.generalCheckByOrder(con,"*",DatabaseHandler.MUSIC,"T_played DESC, Title ASC", "0,5");

        System.out.println("\nTop 5 popular music in the chart!");
        System.out.println("\nIndex Number | Title | Artist | Produced by | Playtime | Genre | Released");
        listMusic(topRs);
        try{
            playMusic(con,userIndex);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void listAllMusic(Connection con) {
        ResultSet rs = GeneralQuery.generalCheck(con, "*", DatabaseHandler.MUSIC);
        System.out.println("\nIndex Number | Title | Artist | Produced by | Playtime | Genre | Released");
        listMusic(rs);
    }

    private static void listMusic(ResultSet rs){
        try {
            List<MusicAttribute> list = MusicAttribute.getGeneralTypes();
            System.out.print("\n");
            printMusicAttributes(rs, list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void findPlayList(Connection con, ArrayList<String> userInfo) throws SQLException {
        int userIndex = Integer.parseInt(userInfo.get(3));
        ArrayList<ResultSet> rs = UserFunction.searchPlaylist(con, userIndex);
        ResultSet num = rs.get(0);
        ResultSet playList = rs.get(1);

        if (hasResult(num)) {
            playListMenu(con, userIndex);
        } else {
            System.out.println("No Playlist Found. Create new playlist? (Y: Yes/N: No): ");
            Character createNew = stringTrim(sc.nextLine()).charAt(0);

            if (createNew.equals('y') || createNew.equals('Y')) {
                UserFunction.createPlaylist(con, userIndex);
            } else {
                System.out.println("Returning to user page...");
            }

        }
    }

    private static void playListMenu(Connection con, int userIndex) throws SQLException {
        int option = -1;
        while (option != 0) {
            int pidx = -1;

            ArrayList<ResultSet> res = UserFunction.searchPlaylist(con, userIndex);
            ResultSet playList = res.get(1);

            System.out.println("\nYOUR PLAYLIST(S):");
            while (playList.next()) {
                pidx = playList.getInt("PIDX");
                System.out.println(playList.getString("Playlist_name") +
                        " (" + UserFunction.checkPLMusicNum(con, pidx) + ")");
            }

            System.out.println("\nOptions:\nPress 1 to create new playlist\nPress 2 to select playlist" +
                    "\nPress 0 to return");
            option = parse(sc.nextLine());

            switch (option) {
                case -1 -> {
                    System.out.println("ERROR: Invalid Input");
                }

                case 0 -> {
                    System.out.println("Returning...");
                }

                case 1 -> {
                    UserFunction.createPlaylist(con, userIndex);
                }

                case 2 -> {
                    System.out.print("Enter Playlist Title: ");
                    String title = stringTrim(sc.nextLine());


                    Integer playlistIdx = UserFunction.getPlaylist(con, title, userIndex);

                    if (!Objects.isNull(playlistIdx)) {

                        playListOptions(con, playlistIdx, userIndex);

                    } else {
                        System.out.println("ERROR: Wrong Title or Unauthorized Access");
                    }
                }
            }
        }
    }

    private static void playListOptions(Connection con, int playlistIndex, int userIndex) {
        int option = -1;
        while (option != 0) {
            try {

                ResultSet anyUpdates = GeneralQuery.generalCheck(con, "Playlist_name", DatabaseHandler.PLAYLIST, "Owner_idx = " + userIndex +
                        " AND PIDX = " + playlistIndex);
                String title = null;
                while (anyUpdates.next()) { //update Playlist Name
                    title = anyUpdates.getString("Playlist_name");
                }

                System.out.println("\nPlaylist '" + title + "'\nNumber of Music(s): "
                        + UserFunction.checkPLMusicNum(con, playlistIndex) + "\n");
                ResultSet rs = UserFunction.getMusicList(con, playlistIndex);
                showAllMusics(con, rs);

                System.out.println("\nOptions:\nPress 1 to add music\nPress 2 to play music\n" +
                        "Press 3 to delete music\nPress 4 to change playlist name\nPress 5 to delete current playlist\nPress 0 to Return");
                option = parse(sc.nextLine());

                switch (option) {
                    case -1 -> {
                        System.out.println("ERROR: INVALID INPUT");
                    }

                    case 0 -> {
                        System.out.println("Returning...");
                    }

                    case 1 -> {
                        addMusic(con, playlistIndex);
                    }

                    case 2 -> {
                        playPlaylistMusic(con, playlistIndex, userIndex);
                    }

                    case 3 -> {
                        deletePlaylistMusic(con, playlistIndex);
                    }

                    case 4 -> {
                        changePlaylistName(con, playlistIndex, userIndex);
                    }

                    case 5 -> {
                        option = deletePlaylist(con, playlistIndex);
                    }

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static Integer deletePlaylist(Connection con, int playlistIndex) {
        System.out.print("Are you sure to delete? (All music will be deleted and current operation is not recoverable! (Y to confirm): ");
        Character option = stringTrim(sc.nextLine()).charAt(0);

        if (option.equals('y') || option.equals('Y')) {
            UserFunction.deletePlaylist(con, playlistIndex);
            System.out.println("Deleted");
            return 0;
        } else {
            System.out.println("Delete Canceled\n");
        }
        return 5;
    }

    private static void changePlaylistName(Connection con, int playlistIndex, int userIndex) {
        System.out.print("Enter new Playlist name: ");
        String newName = stringTrim(sc.nextLine());

        if (UserFunction.checkPlaylistDuplicate(con, userIndex, newName)) {
            System.out.print("Are you sure to change this playlist name to " + newName + "? (Y to confirm): ");
            Character option = stringTrim(sc.nextLine()).charAt(0);

            if (option.equals('y') || option.equals('Y')) {
                UserFunction.changePlaylistName(con, playlistIndex, newName);
                System.out.println("Successfully Changed\n");
            } else {
                System.out.println("Change Canceled.");
            }
        }
    }

    private static void deletePlaylistMusic(Connection con, int playlistIndex) throws SQLException {
        System.out.print("Enter Music Index number: ");
        Integer midx = parse(sc.nextLine());

        if (midx.equals(-1)) {
            System.out.println("ERROR: Invalid Index");
        } else {
            ResultSet rs = GeneralQuery.generalCheck(con, "*", DatabaseHandler.PLAYLIST_MUSIC, "MUSICIDX = " + midx + " AND " +
                    "PIDX = " + playlistIndex);

            if (rs.next()) {
                System.out.println("Are you sure to delete this music from your playlist? (Y to confim: )");
                Character option = stringTrim(sc.nextLine()).charAt(0);

                if (option.equals('y') || option.equals('Y')) {
                    UserFunction.deleteMusicFromPlaylist(con, midx, playlistIndex);
                    System.out.println("Delete Complete \n");
                } else {
                    System.out.println("Delete Canceled.");
                }
            } else {
                System.out.println("ERROR: Not available for current playlist");
            }
        }
    }

    private static void playPlaylistMusic(Connection con, int playlistIndex, int userIndex) throws SQLException {
        System.out.print("Enter Music Index number: ");
        Integer midx = parse(sc.nextLine());

        if (midx.equals(-1)) {
            System.out.println("ERROR: Invalid Index");
        } else {
            ResultSet rs = GeneralQuery.generalCheck(con, "*", DatabaseHandler.PLAYLIST_MUSIC, "MUSICIDX = " + midx + " AND " +
                    "PIDX = " + playlistIndex);

            if (rs.next()) {
                UserFunction.playMusic(con, String.valueOf(midx),userIndex);
            } else {
                System.out.println("ERROR: Not available for current playlist");
            }
        }
    }

    private static void showAllMusics(Connection con, ResultSet rs) throws SQLException {
        List<MusicAttribute> list = MusicAttribute.getGeneralTypes();
        while (rs.next()) {
            ResultSet queryResult = GeneralQuery.generalCheck(con, "*", DatabaseHandler.MUSIC, "IDX = "
                    + rs.getInt("MUSICIDX"));
            printMusicAttributes(queryResult, list);
        }
    }

    private static void addMusic(Connection con, int pidx) throws SQLException {
        System.out.println("Write an index number of a music: ");
        int midx = sc.nextInt();
        sc.nextLine();

        Integer result = UserFunction.addMusicToPlaylist(con, midx, pidx);

        if (result.equals(0)) {
            System.out.println("Added!");
        }
        if (result.equals(-1)) {
            System.out.println("No music Found");
        }
        if (result.equals(-2)) {
            System.out.println("Duplicate music exists in the same playlist!");
        }
    }


    private static boolean userProfile(Connection con, ArrayList<String> userInfo) throws SQLException {
        ResultSet rs = DatabaseQuery.findProfile(con, userInfo.get(1));
        User user = getUser(rs);

        String curSsn = "'" + user.getSSN() + "'";

        int instr = -1;
        while (instr == -1) {
            System.out.print(
                    "\nPROFILE OPTIONS:\nPress 1 to see my profile.\nPress 2 to change personal information(name,age)."
                            +
                            "\nPress 3 tp change email address." +
                            "\nPress 4 to change Password.\nPress 5 to change Nickname.\nPress 6 to Leave an account\nPress 0 to return.\nYour Choice: ");

            instr = parse(sc.nextLine());

            if (instr == 0) {
                System.out.println("Return to user page...");
            }

            if (instr == 1) {
                printProfile(user);
            }

            if (instr == 2) {
                updateNameAndAge(con, curSsn);
            }

            if (instr == 3) {
                updateEmail(con, curSsn);
            }

            if (instr == 4) {
                updatePassword(con, curSsn, user);
            }

            if (instr == 5) {
                updateNickName(con, curSsn);
            }

            if (instr == 6){
                instr = leaveAccount(con, curSsn, user.getPassword());
                if(instr == 0){
                    return false;
                }
            }
        }
        return true;
    }


    private static Integer leaveAccount(Connection con,String curSsn, String pw) {
        if(checkPassword(pw)){
            System.out.println("Are you sure? Your decision would not be recoverable! (Enter 'Confirm' COMPLETELY to leave your account - Capital sensitive. ): ");
            String input = stringTrim(sc.nextLine());
            if(input.equals("Confirm")){
                GeneralQuery.generalDelete(con,DatabaseHandler.USER,"SSN = " + curSsn);
                return 0;
            }else{
                System.out.println("Request Denied: The input string didn't match with the workd 'Confirm'.\n");
            }
        }else{
            System.out.println("Access Denied: Password didn't match");
        }
        return 6;
    }

    private static boolean checkPassword(String pw) {
        System.out.print("Enter your password: ");
        String input = stringTrim(sc.nextLine());

        return input.equals(pw);
    }

    private static void searchMusicByTitle(Connection con, String title, Integer userIndex) throws SQLException {
        ArrayList<ResultSet> resultSets = UserFunction.searchMusic(
                con, "Title", "'%" + title + "%'");
        ResultSet rs = resultSets.get(0);
        ResultSet queryResult = resultSets.get(1);
        listResult(queryResult);
        playMusic(con, rs, userIndex);
    }

    private static void searchMusicByGenre(Connection con, String genre,int userIndex) throws SQLException{
        ArrayList<ResultSet> resultSets = UserFunction.searchMusic(con,"Genre","'%" + genre + "%'");
        ResultSet rs = resultSets.get(0);
        ResultSet queryResult = resultSets.get(1);
        listResult(queryResult);
        playMusic(con, rs, userIndex);
    }

    private static void searchMusicByArtist(Connection con, String artist, Integer userIndex) throws SQLException {
        ArrayList<ResultSet> resultSets = UserFunction.searchMusic(con,"Artist","'%" + artist + "%'");
        ResultSet rs = resultSets.get(0);
        ResultSet queryResult = resultSets.get(1);
        listResult(queryResult);
        playMusic(con, rs, userIndex);
    }

    private static void listResult(ResultSet queryResult) throws SQLException {
        List<MusicAttribute> musicAttributes = MusicAttribute.getGeneralTypes();

        System.out.println("Index Number | Title | Artist | Produced by | Playtime | Genre | Released");
        printMusicAttributes(queryResult, musicAttributes);

        while (queryResult.next()) {
            System.out.print(queryResult.getInt("IDX") + " | ");
            printMusicAttributes(queryResult, musicAttributes);
        }
    }


    private static void updateNickName(Connection con, String curSsn) {
        System.out.print("Your new Nickname: ");
        String newNickName = stringTrim(sc.nextLine());

        System.out.print(
                "Your new Nickname is " + newNickName + ". Am I right? (Enter y to say Yes): ");
        Character answer = stringTrim(sc.nextLine()).charAt(0);

        if (answer.equals('y') || answer.equals('Y')) {
            DatabaseQuery.updateProfile(con, DatabaseHandler.ACCOUNT, "Nickname",
                    "'" + newNickName + "'", "Ussn = " + curSsn);
            System.out.println("\nChange Applied!\n");
            return;
        }

        System.out.println("Cancel the changes...");
    }

    private static void updatePassword(Connection con, String curSsn, User user) {
        System.out.print("Your current password: ");
        String oldPassword = stringTrim(sc.nextLine());

        if (!oldPassword.equals(user.getPassword())) {
            System.out.println("Request Denied: Didn't match with original password\n");
            return;
        }

        System.out.print("Your new password: ");
        String newPassword = stringTrim(sc.nextLine());
        System.out.print("Your new password (confirm): ");
        String matchNewPassword = stringTrim(sc.nextLine());

        if (newPassword.equals(matchNewPassword)) {
            DatabaseQuery.updateProfile(con, DatabaseHandler.ACCOUNT, "PW", "'" + newPassword + "'",
                    "Ussn = " + curSsn);
            System.out.println("\nChange Applied!\n");
            return;
        }

        System.out.println("ERROR: New password didn't match.\n");
    }

    private static void updateEmail(Connection con, String curSsn) throws SQLException {
        System.out.print("Your new Email: ");
        String newEmail = stringTrim(sc.nextLine());

        ResultSet checkedResult = GeneralQuery.generalCheck(con, "email", DatabaseHandler.USER,
                "email = '" + newEmail + "'");

        if (checkedResult.next()) {
            System.out.println(
                    "\nRequest Denied: The email " + newEmail + " is currently used by other user\n");
            return;
        }
        DatabaseQuery.updateProfile(con, DatabaseHandler.USER, "email", "'" + newEmail + "'",
                "SSN = " + curSsn);
        System.out.println("\nChange Applied!\n");
    }

    private static void updateNameAndAge(Connection con, String curSsn) {
        System.out.print("Your new name: ");
        String newName = stringTrim(sc.nextLine());
        System.out.print("Your new age: ");
        int newAge = Integer.parseInt(sc.nextLine());

        DatabaseQuery.updateProfile(con, DatabaseHandler.USER, "name", "'" + newName + "'",
                "SSN = " + curSsn);
        DatabaseQuery.updateProfile(con, DatabaseHandler.USER, "age", newAge, "SSN = " + curSsn);

        System.out.println("Change Applied!\n");
    }

    private static void printProfile(User user) {
        System.out.println("\n- PROFILE -");
        System.out.println(user.toString());
    }

    private static User getUser(ResultSet rs) throws
            SQLException {
        User user = null;
        if (rs.next()) {
            user = new User(rs);
        }
        rs.close();
        return user;
    }

    private static void printMusicAttributes(ResultSet queryResult, List<MusicAttribute> musicAttributes) throws
            SQLException {
        while (queryResult.next()) {
            for (MusicAttribute musicAttribute : musicAttributes) {
                System.out.print(queryResult.getString(musicAttribute.getAttribute()) + " | ");
            }
            System.out.print("\n");
        }
    }

    private static void playMusic(Connection con, ResultSet rs, Integer userIndex) throws SQLException {
        if (hasResult(rs)) {
            System.out.println(
                    "Enter 'index number' of music you want to play (Enter * to return): ");
            String target = stringTrim(sc.nextLine());

            if (target.equals("*")) {
                System.out.print("Return to music search page...\n");
                return;
            }
            UserFunction.playMusic(con, target, userIndex);
        }
    }

    private static void playMusic(Connection con, Integer userIndex) throws SQLException{
        System.out.println(
                "Enter 'index number' of music you want to play (Enter * to return): ");
        String target = stringTrim(sc.nextLine());

        if (target.equals("*")||target.equals("0")) {
            System.out.print("Return to music search page...\n");
            return;
        }
        UserFunction.playMusic(con, target, userIndex);
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
        Character YorN = stringTrim(sc.nextLine()).charAt(0);

        if (YorN.equals('y') || YorN.equals('Y')) {
            return true;
        } else {
            System.out.println("Return to the user page...");
        }
        return false;
    }
}