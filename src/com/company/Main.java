package com.company;

import dbpackage.DatabaseHandler;
import ServiceFunc.UserRegister;

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
            System.out.println("- MAIN PAGE -");
            System.out.print("0. EXIT\n1.SIGN IN\n2.REGISTER\nYOUR OPTION: ");
            instr = Integer.parseInt(sc.next());

            switch (instr) {
                case 0:
                    break;

                case 2:
                    UserRegister.register(connection);
            }
        }

        connection.close();
    }
}

