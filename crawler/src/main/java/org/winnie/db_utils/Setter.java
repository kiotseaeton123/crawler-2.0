package org.winnie.db_utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Setter {
    public static void setData(String table, String feature, String value){
        String sql = "INSERT INTO " + table + " (" + feature + ") VALUES (?)";

        try(Connection connection=Connector.getConnection(); PreparedStatement statement=connection.prepareStatement(sql)){
            statement.setString(1, value);
            statement.executeUpdate();
            System.out.println(feature + ": " + value + " inserted into " + table);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
