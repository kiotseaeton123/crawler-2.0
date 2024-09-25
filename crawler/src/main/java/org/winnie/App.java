package org.winnie;

import org.winnie.wiki_utils.WikiCrawler;

/**
 * main class
 * 
 * @author winnie
 */
public class App {

    /**
     * main function
     * 
     * @param args - cmd line args
     */
    public static void main(String[] args) {
        
        WikiCrawler.crawlCategory();
    }

}
