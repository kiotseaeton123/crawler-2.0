package org.winnie;

import org.winnie.utils.NoLinksFoundException;
import org.winnie.utils.Webpage;
import java.util.List;

public class App {

    static String wikiurl = "https://wikipedia.org";

    public static void main(String[] args) {
        // path to special:random/category namespace
        String path = "/wiki/special:random/category";
        String query = ".mw-category-group a[href]";

        // encase in while loop if multiple attempts necessary
        int attempts=3;
        while(attempts>0){
            try {
                // get category page and links
                Webpage wiki = new Webpage(wikiurl, path, query);
    
                List<String> links = wiki.getLinks();
    
                // summary info
                System.out.println(wiki.toString());
    
                // crawl edit history
                parseHistory(links);

                break;
    
            } catch (NoLinksFoundException e) {
                System.out.println(e.getMessage());
                attempts--;
            }
    
        }
        
    }

    public static void parseHistory(List<String> links) {
        for (String link : links) {
            System.out.println("---category link: " + link);

            try {
                // get topic history page and its edit history link
                Webpage page = new Webpage(wikiurl, link, "li#ca-history a[href]");
                String historylink = page.getLinks().get(0);
                System.out.println(historylink);
                // // get edit history page and parse users
                // Webpage historypage=new Webpage(wikiurl,historylink,".mw-user-link a[href]");
                // List<String> userlinks=historypage.getLinks();

                // for(String user:userlinks){
                //     System.out.println(user);
                // }

            } catch (NoLinksFoundException e) {
                System.out.println(e.getMessage());
            }

        }

    }
}
