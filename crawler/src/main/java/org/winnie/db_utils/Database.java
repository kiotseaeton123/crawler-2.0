package org.winnie.db_utils;

import java.sql.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * this class represents a sqlite database
 * 
 * @author winnie
 */
public class Database {
    // base directory, db url, db connection
    private String base;
    private String url;
    private Connection connection;

    /**
     * constructor takes database name and establishes connection
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
        this.connection = connect(db);
    }

    /**
     * method establishes db connection
     * 
     * @param name - db name
     * @return connection
     */
    private Connection connect(String db) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(this.url);
            System.out.println("-----" + db + " connection established-----");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    /**
     * method returns database connection
     * 
     * @return Connection
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * method creates db table
     * 
     * @param query   - a sql query
     * @param columns - column definitions, eg. id INT PRIMARY KEY, name TEXT
     */
    public void createTable(String table, String columns) {
        String query = "CREATE TABLE IF NOT EXISTS " + table + " (" + columns + ");";
        try {
            Statement statement = this.connection.createStatement();
            statement.execute(query);
            System.out.println("-----" + table + " table created-----");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * method inserts 2 fields to a table
     * 
     * @param table   - db table
     * @param column1 - first column
     * @param value1  - first data
     * @param column2 - second column
     * @param value2  - second data
     */
    public void insertData(String table, String column1, String value1, String column2, String value2) {

        String query = "INSERT INTO " + table + "(" + column1 + ", " + column2 + ") VALUES (?, ?)";

        try {
            PreparedStatement statement = this.connection.prepareStatement(query);
            statement.setString(1, value1);
            statement.setString(2, value2);
            statement.execute();
            System.out.println("-----" + column1 + ": " + value1 + " and " + column2 + ": " + value2 + " inserted to "
                    + table + "-----");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * method to query table field, returns all rows in field
     * 
     * @param table  - db table
     * @param column - db column
     * @return return list of strings
     */
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

    /**
     * this method queries table field with condition
     * 
     * @param table     - db table
     * @param column    - db column
     * @param condition - WHERE condition
     * @return String
     */
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
     * close database connection
     */
    public void close() {
        try {
            this.connection.close();
            System.out.println("-----database connection closed-----");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
