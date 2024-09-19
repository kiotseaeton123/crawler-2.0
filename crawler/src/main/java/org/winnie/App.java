package org.winnie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.*;
import java.util.Random;

public class App {
    public static void main(String[] args) {
        scrapetopic("/wiki/Java");
    }

    public static void scrapetopic(String path) {
        String url="https://www.wikipedia.org"+path;

        try {
            // get html and select css tags
            Document document = Jsoup.connect(url).get();
            Elements paragraphs = document.select("p");
            Elements links=document.select("#mw-content-text [href~=^/wiki/[\\w]+$]");

            // number elements in html
            int nparagraphs=paragraphs.size();
            int nlinks=links.size();

            // crawl links if there are links
            if(nlinks>0 && nparagraphs>0){
                System.out.println("here");
                Random random=new Random();
                String randomlink=links.get(random.nextInt(nlinks)).attr("href");
                String firstparagraph=paragraphs.get(0).text();

                if(!randomlink.isEmpty() && randomlink.startsWith("/wiki")){
                    System.out.println("link: "+randomlink);
                    System.out.println("first paragraph: "+firstparagraph);
                    scrapetopic(randomlink);
                }
                else{
                    System.out.println("------------------------");
                    scrapetopic("/wiki/Java");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
