package org.winnie;

import org.winnie.utils.Webpage;
import java.util.List;

public class App {
    public static void main(String[] args) {
        // path to special:random/category namespace
        String url = "https://wikipedia.org";
        String path = "/wiki/special:random/category";
        String query=".mw-category-group a[href]";

        // get page and links
        Webpage wiki = new Webpage(url, path,query);
        List<String> links = wiki.getLinks();
        for(String link:links){
            System.out.println(link);
        }
        // summary info
        System.out.println(wiki.toString());
    }
}
