package org.winnie.geolocation_utils;

import org.winnie.db_utils.Database;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;


public class IP2GeoResolver {
    private Connection connection;

    public IP2GeoResolver(Database db){
        this.connection=db.getConnection();
    }
    
    public String resolveIPv4(String ip){
        try{
            byte[] addressbytes=InetAddress.getByName(ip).getAddress();
            BigInteger ipinteger=new BigInteger(1,addressbytes);

            String query="SELECT geolocation.continent_code, geolocation.country_iso_code FROM ipv4 JOIN geolocation ON ipv4.geoname_id=geolocation.geoname_id WHERE ? BETWEEN ipv4.ipstart AND ipv4.ipend;";

            PreparedStatement statement=this.connection.prepareStatement(query);
            statement.setLong(1,ipinteger.longValue());
            ResultSet result=statement.executeQuery();

            if(result.next()){
                return result.getString(1)+","+result.getString(2);
            }else{
                return "no geolocation found for ipv4 " + ip;
            }
        }catch(SQLException | UnknownHostException e){
            System.out.println(e.getMessage());
            return "could not resolve ipv4 " + ip;
        }   
    }

    public String resolveIPv6(String ip){
        try{
            byte[] addressbytes=InetAddress.getByName(ip).getAddress();

            String query="SELECT geolocation.continent_code, geolocation.country_iso_code FROM ipv6 JOIN geolocation ON ipv6.geoname_id=geolocation.geoname_id WHERE ? BETWEEN ipv6.ipstart AND ipv6.ipend;";

            PreparedStatement statement=this.connection.prepareStatement(query);
            statement.setBytes(1, addressbytes);
            ResultSet result=statement.executeQuery();

            if(result.next()){
                return result.getString(1)+","+result.getString(2);
            }else{
                return "no geolocation found for ipv6 " + ip;
            }
        }catch(SQLException | UnknownHostException e){
            System.out.println(e.getMessage());
            return "could not resolve ipv6 " + ip;
        }
    }   
}
