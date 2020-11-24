package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Logger;


public class Main {

    private final static Logger LOG = Logger.getGlobal();

    public static Connection getConnect() throws SQLException{
        String serverHost = "localhost:3307";
        String databaseName = "dbProj";
        String userName = "root";
        String password = "Cheesepasta613!";

        try{
            Class.forName("org.mariadb.jdbc.Driver");

        }catch(ClassNotFoundException e){
            LOG.severe("CRITICAL ERROR - Database is not loadable. ERROR MESSAGE: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
        LOG.fine("DATABASE CONNECTED");
        return DriverManager.getConnection("jdbc:mysql://"+serverHost+"/"+databaseName,userName,password);
    }
    public static void main(String[] args) throws SQLException {
        //Set Database Connection
        Connection connection = getConnect();
    }
}
