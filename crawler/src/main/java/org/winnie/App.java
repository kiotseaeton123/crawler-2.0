package org.winnie;

import org.winnie.utils.Webpage;
import java.util.List;

public class App {

    static String wikiurl = "https://wikipedia.org";

    public static void main(String[] args) {
        // path to special:random/category namespace
        String path = "/wiki/special:random/category";
        String query = ".mw-category-group a[href]";

        // get category page and links of interest
        Webpage wiki = new Webpage(wikiurl, path, query);
        List<String> links = wiki.getLinks();

        // if category page empty, get another one
        if (links.isEmpty()){
            wiki = new Webpage(wikiurl, path, query);
        }

        // summary info
        System.out.println(wiki.toString());

        // crawl edit history
        parseHistory(links);
    }

    public static void parseHistory(List<String> links) {
        for (String link : links) {
            System.out.println("---category link: " + link);
            // Webpage categorypage=
        }


    }
}
