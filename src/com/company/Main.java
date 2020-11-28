package com.company;

import AdminFunc.AdminFunc;
import ServiceFunc.UserAccount;
import dbpackage.DatabaseHandler;
import ServiceFunc.UserFunction;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws SQLException {
        //Set Database Connection
        Connection connection = DatabaseHandler.getConnect();

        //Intro
        System.out.println("******* Welcome to Kyum's Music Streaming Service! *******\n");

        //Input from user - get instruction
        Integer instr = -1;
        Scanner sc = new Scanner(System.in);
        while (!instr.equals(0)) {
            System.out.println("\n- MAIN PAGE -");
            System.out.print("0. EXIT\n1.SIGN IN\n2.REGISTER\n3.FIND MY ACCOUNT\n4.ADMINISTRATOR MENU\nYOUR OPTION: ");
            instr = Integer.parseInt(sc.next());

            switch (instr) {
                case 0:
                    break;

                case 1:
                    UserFunction.signIn(connection);
                    break;

                case 2:
                    UserFunction.register(connection);
                    break;

                case 3:
                    UserAccount.findAccount(connection);
                    break;

                case 4:
                    AdminFunc.AdminAuth(connection);
                    break;

                default:
                    System.out.println("ERROR: Invalid Input");
                    instr = -1;
            }
        }

        connection.close();
    }
}

