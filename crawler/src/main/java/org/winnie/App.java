package org.winnie;

import org.winnie.utils.NoLinksFoundException;
import org.winnie.utils.Webpage;
import org.winnie.utils.User;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class App {

    static String wikiurl = "https://wikipedia.org";

    public static void main(String[] args) {
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

        // print data summary
        for (String topiclink : data.keySet()) {
            System.out.println("\n\n" + topiclink);
            HashSet<User> users = data.get(topiclink);

            for (User user : users) {
                System.out.print(user.getUserName() + ", ");
            }
            System.out.println("\n\n----------");
        }

    }
}
