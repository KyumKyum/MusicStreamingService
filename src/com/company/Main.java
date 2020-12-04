package com.company;

import AdminFunc.AdminFunc;
import dbpackage.DatabaseHandler;
import ServiceFunc.UserFunction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import static java.lang.System.exit;


public class Main {

    public static void main(String[] args) throws SQLException {
        //Set Database Connection
        Connection connection = DatabaseHandler.getConnect();

        //Intro
        System.out.println("******* Welcome to Kyum's Music Streaming Service! *******\n");

        //Input from user - get instruction
        int instr = -1;
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n- MAIN PAGE -");
            System.out.print("0. EXIT\n1.SIGN IN\n2.REGISTER\n3.ADMINISTRATOR MENU\nYOUR OPTION: ");
            instr = Integer.parseInt(sc.next());

            switch (instr) {
                case 0 -> {
                    System.out.println("Closing...");
                    connection.close();
                    exit(0);
                }
                case 1 -> UserFunction.signIn(connection);
                case 2 -> UserFunction.register(connection);
                case 3 -> AdminFunc.AdminAuth(connection);
                default -> {
                    System.out.println("ERROR: Invalid Input");
                }
            }
        }
    }
}

