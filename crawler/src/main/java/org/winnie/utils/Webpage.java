package org.winnie.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.HttpStatusException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

public class Webpage {
    private String url;
    private String html;
    private List<String> links;
    private long responsetime;

    /**
     * webpage constructor fetches html and links according to css selector
     * 
     * @param url
     * @param path  - url path
     * @param query - cssQuery, use css selector to extract elements from document
     * 
     */
    public Webpage(String url) {
        this(url, "", "a[href]");
    }

    public Webpage(String url, String path) {
        this(url, path, "a[href]");
    }

    public Webpage(String url, String path, String query) {
        HashSet<String> robots = parseRobots(url);
        this.url = url + path;

        try {
            long timestamp = System.currentTimeMillis();
            Document document = Jsoup.connect(this.url).get();
            this.responsetime = System.currentTimeMillis() - timestamp;

            // extract html and links
            this.html = document.html();
            this.links = document.select(query).stream()
                    .map(link -> link.attr("href"))
                    .filter(link -> !robots.contains(link))
                    .toList();

        } catch (HttpStatusException e) {
            System.out.println("----------http error code: " + e.getStatusCode() + " " + this.url + "----------");

        } catch (IOException e) {
            System.out.println("----------io exception: " + e.getMessage() + " " + this.url + "----------");
        }
    }

    private HashSet<String> parseRobots(String url) {
        // robots txt file and disallowed links
        String robotsfile;
        HashSet<String> links = new HashSet<>();

        // return empty hashset if there is no robots file
        try {
            robotsfile = Jsoup.connect(url + "/robots.txt").get().text();
        } catch (Exception e) {
            System.out.println("----------" + url + "no robots file----------");
            return links;
        }

        Pattern pattern = Pattern.compile("^Disallow: (/wiki/\\w+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(robotsfile);

        while (matcher.find()) {
            links.add(matcher.group(1));
        }

        return links;
    }

    /**
     * getters and setters
     * 
     * @return attributes or html info
     */
    public List<String> getLinks() {
        return this.links;
    }

    public String getParagraphs() {
        if (!this.html.isEmpty()) {
            Document document = Jsoup.parse(this.html);
            String paragraphs = document.select("p").text();
            return paragraphs;
        }
        return "no paragraphs found";
    }

    /**
     * @return summary string
     */
    public String toString() {
        return "----------" + this.url + " in " + this.responsetime + " ms ----------";
    }

}