package org.winnie.dbutils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {
    private static final String BASE=System.getProperty("user.dir");
    private static final String DBPATH=BASE+File.separator+".."+File.separator+"data"+File.separator+"geodata.db"; 
    private static final String DBURL="jdbc:sqlite:"+DBPATH;

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBURL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }
}
