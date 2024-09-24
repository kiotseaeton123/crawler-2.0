package org.winnie.db_utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Table {
    public static void createTables(){
        String geolocationtable="CREATE TABLE IF NOT EXISTS geolocation (geoname_id INTEGER PRIMARY KEY, continent_code TEXT, country_iso_code TEXT);";

        String ipv4table="CREATE TABLE IF NOT EXISTS ipv4 (ip_start INTEGER PRIMARY KEY AUTOINCREMENT, network TEXT NOT NULL, ipstart INTEGER NOT NULL, ipend INTEGER NOT NULL, geoname_id INTEGER, FOREIGN KEY(geoname_id) REFERENCES geolocation(geoname_id));";


        try(Connection connection=Connector.getConnection(); Statement statement=connection.createStatement()){
            statement.execute(geolocationtable);
            statement.execute(ipv4table);
            System.out.println("created tables");
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
