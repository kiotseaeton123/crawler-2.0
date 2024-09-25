package org.winnie.geolocation_utils;

import org.winnie.db_utils.Database;
import org.winnie.utils.Pair;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;

/**
 * this class resolves ipv4 and ipv6 to geolocation
 * 
 * @author winnie
 */
public class IP2GeoResolver {
    private Connection connection;

    /**
     * constructor stores database connection
     * 
     * @param db - database instance
     */
    public IP2GeoResolver(Database db) {
        this.connection = db.getConnection();
    }

    /**
     * method checks if address is ipv4
     * 
     * @param ip - ip address
     * @return - boolean is ipv4
     */
    public boolean isIPv4(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address instanceof java.net.Inet4Address;

        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * method checks if ipv6
     * 
     * @param ip - address
     * @return - boolean is ipv6
     */
    public boolean isIPv6(String ip) {
        try {
            InetAddress address = InetAddress.getByName(ip);
            return address instanceof java.net.Inet4Address;
            
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * method resolves ipv4 to geolocation
     * 
     * @param ip - ipv4 address
     * @return continent, country pair
     */
    public Pair<String, String> resolveIPv4(String ip) {
        try {
            byte[] addressbytes = InetAddress.getByName(ip).getAddress();
            BigInteger ipinteger = new BigInteger(1, addressbytes);

            String query = "SELECT geolocation.continent_code, geolocation.country_iso_code FROM ipv4 JOIN geolocation ON ipv4.geoname_id=geolocation.geoname_id WHERE ? BETWEEN ipv4.ipstart AND ipv4.ipend;";

            PreparedStatement statement = this.connection.prepareStatement(query);
            statement.setLong(1, ipinteger.longValue());
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return new Pair<>(result.getString(1), result.getString(2));
            } else {
                System.out.println("no geolocation found for ipv4 " + ip);
            }
        } catch (SQLException | UnknownHostException e) {
            System.out.println("could not resolve ipv4 " + ip + ": " + e.getMessage());
        }
        return null;
    }

    /**
     * method resolves ipv6 to geolocation
     * 
     * @param ip - ipv6 address
     * @return continent, country pair
     */
    public Pair<String, String> resolveIPv6(String ip) {
        try {
            byte[] addressbytes = InetAddress.getByName(ip).getAddress();
            String query = "SELECT geolocation.continent_code, geolocation.country_iso_code FROM ipv6 JOIN geolocation ON ipv6.geoname_id=geolocation.geoname_id WHERE ? BETWEEN ipv6.ipstart AND ipv6.ipend;";

            PreparedStatement statement = this.connection.prepareStatement(query);
            statement.setBytes(1, addressbytes);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                return new Pair<>(result.getString(1), result.getString(2));
            } else {
                System.out.println("no geolocation found for ipv6 " + ip);
            }
        } catch (SQLException | UnknownHostException e) {
            System.out.println("could not resolve ipv6 " + ip + ": " + e.getMessage());
        }
        return null;
    }
}
