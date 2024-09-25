package org.winnie.wiki_utils;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.winnie.db_utils.Database;
import org.winnie.geolocation_utils.IP2GeoResolver;
import org.winnie.utils.NoLinksFoundException;
import org.winnie.utils.User;
import org.winnie.utils.Webpage;
import org.winnie.utils.Pair;

public class WikiCrawler {

    private static String wikiurl = "https://wikipedia.org";

    /**
     * constructor, create wikicrawler instance
     */
    public WikiCrawler() {
    }

    /**
     * example method: crawl wiki category namespace
     */
    public static void crawlCategory() {
        // path to special:random/category namespace
        String path = "/wiki/special:random/category";
        String query = ".mw-category-group a[href]";

        // encase in while loop if multiple attempts necessary
        int attempts = 3;
        while (attempts > 0) {
            try {
                // get category page and links
                Webpage wiki = new Webpage(wikiurl, path, query);

                List<String> links = wiki.getLinks();

                // summary info
                System.out.println(wiki.toString());

                // crawl category page topics
                parseTopics(links);

                // successfull connection, break while loop
                break;

            } catch (NoLinksFoundException e) {
                System.out.println(e.getMessage());
                attempts--;
            }
        }
    }

    /**
     * parse topic pages, parse its edit history page, parse contributing users
     * 
     * @param links - links to category page topics
     */
    public static void parseTopics(List<String> links) {
        // topic link, and hashset of contributing users
        HashMap<String, HashSet<User>> data = new HashMap<>();

        for (String link : links) {
            System.out.println("---category topic: " + link);

            try {
                // get topic page and its edit history link
                Webpage page = new Webpage(wikiurl, link, "li#ca-history a[href]");
                String historylink = page.getLinks().get(0);

                // get edit history page and parse users
                Webpage historypage = new Webpage(wikiurl, historylink, ".mw-userlink");
                List<String> userlinks = historypage.getLinks();

                // fetch user and anonymous user patterns
                Pattern userpattern = Pattern.compile("/wiki/User:(.*)");
                Pattern anompattern = Pattern.compile("/wiki/Special:Contributions/(.*)");

                // contributing users
                HashSet<User> contributors = new HashSet<>();

                // populate contributors
                for (String userlink : userlinks) {
                    Matcher usermatcher = userpattern.matcher(userlink);
                    Matcher anommatcher = anompattern.matcher(userlink);

                    if (usermatcher.find()) {
                        contributors.add(new User(usermatcher.group(1), userlink));
                    }
                    if (anommatcher.find()) {
                        contributors.add(new User(anommatcher.group(1), userlink, true));
                    }
                }
                // store topic link with contributors
                data.put(link, contributors);

            } catch (NoLinksFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        writeSummary(data);
    }

    /**
     * write output summary: links, contributing users/IPs
     * 
     * @param data - crawl data
     */
    public static void writeSummary(HashMap<String, HashSet<User>> data) {

        // anonymous users
        HashSet<User> anomusers = new HashSet<>();

        for (String topiclink : data.keySet()) {
            System.out.println("\n\n" + topiclink);
            HashSet<User> users = data.get(topiclink);

            for (User user : users) {
                System.out.println(user.getUserName() + ", ");
                if (user.isAnom()) {
                    anomusers.add(user);
                }
            }
            System.out.println("\n\n----------");
        }

        System.out.println("\n----------ANONYMOUS USERS----------");
        // connect db and get ip resolver
        Database db = new Database("geodata.db");
        IP2GeoResolver resolver = new IP2GeoResolver(db);

        // path to db file about to create
        String base = System.getProperty("user.dir");
        String path = base + File.separator + ".." + File.separator + "data" + File.separator + "anonusers.db";
        // delete if it already exists
        File dbFile = new File(path);
        if (dbFile.exists()) {
            dbFile.delete();
            System.out.println("-----existing db file deleted-----");
        }
        // create db file and write
        Database anon_db = new Database("anonusers.db");
        anon_db.createTable("user",
                "id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL, country TEXT");

        // parse anonymous users and resolve IP
        for (User user : anomusers) {
            String ip = user.getUserName();
            System.out.print(ip + ": ");

            if (resolver.isIPv4(ip)) {
                Pair<String, String> geolocation = resolver.resolveIPv4(ip);
                System.out.print(geolocation.getKey() + ", " + geolocation.getValue() + "\n");

                // insert to db
                anon_db.insertData("user", "username", ip, "country", geolocation.getValue());

            } else if (resolver.isIPv6(ip)) {
                Pair<String, String> geolocation = resolver.resolveIPv6(ip);
                System.out.print(geolocation.getKey() + ", " + geolocation.getValue() + "\n");

                // insert to db
                anon_db.insertData("user", "username", ip, "country", geolocation.getValue());

                // close db
                anon_db.close();

            } else {
                System.out.println("ip not found");
            }
        }
    }
}
