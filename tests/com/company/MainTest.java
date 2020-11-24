package com.company;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void getConnect() throws SQLException {
        String serverHost = "localhost:3307";
        String databaseName = "dbProj";
        String userName = "root";
        String password = "Cheesepasta613!";

        try{
            Class.forName("org.mariadb.jdbc.Driver");

        }catch(ClassNotFoundException e){

            e.printStackTrace();
            throw new RuntimeException();
        }

        Connection con =  DriverManager.getConnection("jdbc:mysql://"+serverHost+"/"+databaseName,userName,password);
        assertNotNull(con);
    }

    @Test
    void main() throws SQLException{
        getConnect();
    }
}