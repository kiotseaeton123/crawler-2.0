package org.winnie.utils;

import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.HttpStatusException;
import java.io.IOException;
import java.util.List;
import java.util.HashSet;

/**
 * class represents a web page (in a web site)
 * @author winnie 
 */
public class Webpage {

    private String url;
    private String html;
    private List<String> links;
    private long responsetime;

    /**
     * constructor with url parameter, css query for extracting links defaults to generic link a[href]
     * @param url - web page url
     */
    public Webpage(String url) {
        this(url, "", "a[href]");
    }

    /**
     * constructor with url and path parameter to web page, css query for extracting links defaults to generic link a[href]
     * @param url - web page url
     * @param path - web page path
     */
    public Webpage(String url, String path) {
        this(url, path, "a[href]");
    }

    /**
     * webpage constructor fetches html and links according to css selector, use
     * generic link selector if none provided
     * 
     * @param url - web page url
     * @param path  - url path
     * @param query - cssQuery, use css selector to extract elements from document
     * 
     */
    public Webpage(String url, String path, String query) {
        // get robots txt disallowed links
        HashSet<String> robots = RobotsCache.getInstance().getRobots(url);
        this.url = url + path;

        try {
            // get web document from url, save redirected url, and record response time
            long timestamp = System.currentTimeMillis();

            Connection connection = Jsoup.connect(this.url).followRedirects(true);
            Document document = connection.get();
            this.url = connection.response().url().toString();

            this.responsetime = System.currentTimeMillis() - timestamp;

            // extract html and links, filter robots txt links
            this.html = document.html();
            this.links = document.select(query).stream()
                    .map(link -> link.attr("href"))
                    .filter(link -> !robots.contains(link))
                    .toList();

        } catch (HttpStatusException e) {
            System.out
                    .println("\n\n----------http error code: " + e.getStatusCode() + " " + this.url + "----------\n\n");

        } catch (IOException e) {
            System.out.println("\n\n----------io exception: " + e.getMessage() + " " + this.url + "----------\n\n");
        }
    }

    /**
     * link getter
     * 
     * @return attributes or html info
     */
    public List<String> getLinks() throws NoLinksFoundException{
        if (this.links.isEmpty()) {
            throw new NoLinksFoundException("----------no links found!----------");
        }
        return this.links;
    }

    /**
     * title getter
     * @return html title
     */
    public String getTitle() {
        if (!this.html.isEmpty()) {
            Document document = Jsoup.parse(this.html);
            return document.title();
        }
        return "\n\n----------no title found in " + this.url + "----------\n\n";
    }

    /**
     * paragraphs getter
     * @return paragraphs in html
     */
    public String getParagraphs() {
        if (!this.html.isEmpty()) {
            Document document = Jsoup.parse(this.html);
            String paragraphs = document.select("p").text();
            return paragraphs;
        }
        return "\n\n----------no paragraphs found in " + this.url + "----------\n\n";
    }

    /**
     * @return summary string
     */
    public String toString() {
        return "---" + this.url + " in " + this.responsetime + " ms";
    }

}