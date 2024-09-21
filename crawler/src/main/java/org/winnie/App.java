package org.winnie;

import org.winnie.utils.Webpage;
import java.util.List;

public class App {
    public static void main(String[] args) {
        String url="https://wikipedia.org";
        String path="/wiki/java";
        // query specifies links to get from the page
        String query="#mw-content-text [href~=^/wiki/\\w+$]";
        
        // get page and links
        Webpage page=new Webpage(url,path,query);
        List<String> links=page.getLinks();

        // summary info
        System.out.println(page.toString());
    }
}
