package org.winnie;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.net.*;
import java.io.*;

public class App 
{
    public static void main( String[] args )
    {
        scrapeTopic("/wiki/Java");
    }

    public static void scrapeTopic(String path){
        String html=getUrl("https://www.wikipedia.org/"+path);
        Document document=Jsoup.parse(html);
       
        // select paragraphs <p>
        Elements paragraphs=document.select("p");
        for(Element p:paragraphs){
            System.out.println(p.text());
        }
    }

    public static String getUrl(String url){
        URL urlobject=null;
        try{
            urlobject=new URL(url);
        }catch(MalformedURLException e){
            System.out.println("url malformed");
            return "";
        }

        URLConnection connection=null;
        BufferedReader in=null;
        String output="";
        try{
            connection=urlobject.openConnection();
            in=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line="";
            while((line=in.readLine())!=null){
                output+=line;
            }
            in.close();
        }catch(IOException e){
            System.out.println("error connecting to url");
            return "";
        }
        return output;
    }
}
