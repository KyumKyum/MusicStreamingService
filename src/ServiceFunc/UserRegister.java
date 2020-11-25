package ServiceFunc;

import dbpackage.DatabaseQuery;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

public class UserRegister {
    public static Scanner sc = new Scanner(System.in);
    public static void register(Connection con) {

        Character instr = null;
        String dump;

        System.out.println("You selected the option \"REGISTER\".\n");
        System.out.print("Are You New? (Enter Yes:y/NO:n/Return to main Page: 0)  ");
        String input = sc.next();
        sc.nextLine();
        instr = input.charAt(0);

        while(true){
            if(instr.equals('y') || instr.equals('Y')){ //new registration
                try{
                    registerNew(con);
                }catch (SQLException e){
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private static void registerNew(Connection con) throws SQLException {
        Vector<String> vec = new Vector<>(4);
        vec.add("Social Security Number(SSN)");
        vec.add("Name");
        vec.add("Age");
        vec.add("Email");

        System.out.println("Hello new visitor! ");
        System.out.println("Enter your information to create account! <Step 1/2>");
        ArrayList<String> userInfo = new ArrayList<>(4);


        for(int i = 0; i < vec.size(); i++){ //Get mandatory information: name, ssn, age
            String userInput = null;
            System.out.print("Your "+vec.elementAt(i)+": ");
            do {
                userInput = sc.nextLine();
                if (userInput.trim().isEmpty())
                    System.out.println("ERROR: Current Information is mandatory.");
                else
                    userInfo.add(i, userInput);
            } while (userInput.trim().isEmpty());
        }

        if(!(DatabaseQuery.findUser(con,userInfo.get(0)).next())){
            DatabaseQuery.insertNewUser(con,userInfo);
            System.out.println("Enter your information to create account! <Step 2/2>");
            registerAccount(con,userInfo.get(0));
        }else{ //User Already Exist.
            System.out.println("You are user of the service already! Want to create new Account?\n(Enter Yes:y/NO:n)");
            String input = sc.next();
            sc.nextLine();
            Character instr = input.charAt(0);

            if(instr.equals('y') || instr.equals('Y')){
                registerAccount(con,userInfo.get(0));
            }
        }
    }

    private static void registerAccount(Connection con, String ssn){
        Vector<String> vec = new Vector<>(3);
        vec.add("ID");
        vec.add("Password");
        vec.add("Nickname");

        ArrayList<String> accountInfo = new ArrayList<>(3);

        for(int i = 0; i < vec.size(); i++){ //Get mandatory information: name, ssn, age
            String userInput = null;
            System.out.print("Your "+vec.elementAt(i)+": ");
            do {
                userInput = sc.nextLine();
                if (userInput.trim().isEmpty())
                    System.out.println("ERROR: Current Information is mandatory.");
                else
                    accountInfo.add(i, userInput);
            } while (userInput.trim().isEmpty());
        }

        DatabaseQuery.insertNewAccount(con,accountInfo,ssn);
    }

}
