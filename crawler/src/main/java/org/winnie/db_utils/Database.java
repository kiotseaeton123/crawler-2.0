package org.winnie.db_utils;

import java.sql.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Database {
    // base directory, db url, db connection
    private String base;
    private String url;
    private Connection connection;

    /**
     * takes database name and establishes connection
     * 
     * @param db - db name
     */
    public Database(String db) {
        // project base directory
        this.base = System.getProperty("user.dir");
        // path to db file
        String path = this.base + File.separator + ".." + File.separator + "data" + File.separator + db;
        // db url
        this.url = "jdbc:sqlite:" + path;
        this.connection = connect();
    }

    /**
     * establish db connection
     * 
     * @return connection
     */
    private Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(this.url);
            System.out.println("database connection established");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    // get connection
    public Connection getConnection(){
        return this.connection;
    }
    
    /**
     * create db table
     * 
     * @param query
     */
    public void createTable(String query) {
        try {
            Statement statement = this.connection.createStatement();
            statement.execute(query);
            System.out.println("table created");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * insert data to table in db
     * 
     * @param table
     * @param column
     * @param value
     */
    public void insertData(String table, String column, String value) {

        String query = "INSERT INTO " + table + "(" + column + ") VALUES (" + value + ")";
        try {
            PreparedStatement statement = this.connection.prepareStatement(query);
            statement.execute();
            System.out.println(column + ":" + value + " inserted to " + table);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // insert 2 data to a table
    public void insertData(String table, String column1, String value1, String column2, String value2) {

        String query = "INSERT INTO " + table + "(" + column1 + "," + column2 + ") VALUES (" + value1 + "," + value2
                + ")";
        try {
            PreparedStatement statement = this.connection.prepareStatement(query);
            statement.execute();
            System.out.println(column1 + ":" + value1 + " and " + column2 + ":" + value2 + " inserted to " + table);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * query table
     * @return list of data
     */
    public String query(String query) {
        String data = "";
        try {
            PreparedStatement statement = this.connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                data = result.getString(1);
            } else {
                System.out.println("no data found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    public List<String> queryColumn(String table, String column) {
        List<String> data = new ArrayList<>();
        String query = "SELECT " + column + " FROM " + table;
        try {
            PreparedStatement statement = this.connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                data.add(result.getString(column));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    // query table with condition
    public String queryElement(String table, String column, String condition) {
        String data = "";
        String query = "SELECT " + column + " FROM " + table + " WHERE " + condition;
        try {
            PreparedStatement statement = this.connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                data = result.getString(column);
            } else {
                System.out.println("no data found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

    /**
     * close connection
     */
    public void close() {
        try {
            this.connection.close();
            System.out.println("database connection closed");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
