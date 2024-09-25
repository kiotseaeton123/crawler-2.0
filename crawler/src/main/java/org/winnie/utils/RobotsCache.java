package org.winnie.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;

/**
 * singleton robots txt cache manager
 * prevent duplicate robots txt parsing for webpages in same website
 * @author winnie
 */
public class RobotsCache{
    
    private static RobotsCache instance;
    // cached robots txt
    private static HashMap<String, HashSet<String>> robotscache = new HashMap<>();

    /**
     * private instantiation, constructor
     */
    private RobotsCache(){}

    /**
     * enable method call before instantiation, ensure only a single instance, lazy instantiation of singleton instance
     * @return RobotsCache instance
     */
    public static synchronized RobotsCache getInstance(){
        if(instance==null){
            instance=new RobotsCache();
        }
        return instance;
    }

    /**
     * returns disallowed links in website's robots txt
     * @param url - website url
     * @return - hashset of disallowed links
     */
    public synchronized HashSet<String> getRobots(String url) {
        if (!robotscache.containsKey(url)) {
            putRobots(url);
        }
        return robotscache.get(url);
    }

    /**
     * parse robots txt file for new websites and store disallowed links to cache
     * @param url - website url
     */
    private void putRobots(String url) {
        // robots txt file and disallowed links
        String robotsfile = "";
        HashSet<String> links = new HashSet<>();

        // return empty hashset if there is no robots file
        try {
            robotsfile = Jsoup.connect(url + "/robots.txt").get().wholeText();
        } catch (Exception e) {
            System.out.println("\n\n----------" + url + " has no robots file----------\n\n");
            robotscache.put(url, links);
        }
   
        // match disallowed url paths
        Pattern pattern = Pattern.compile("^Disallow: (.*)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(robotsfile);

        while (matcher.find()) {
            links.add(matcher.group(1));
        }

        // store disallowed url paths
        robotscache.put(url, links);
    }

}