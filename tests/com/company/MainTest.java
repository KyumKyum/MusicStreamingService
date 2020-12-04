package com.company;

import dbpackage.DatabaseHandler;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

class MainTest {

    @Test
    void main() throws SQLException {
        Connection connection = DatabaseHandler.getConnect();
    }
}