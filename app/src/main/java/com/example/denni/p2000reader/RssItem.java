package com.example.denni.p2000reader;

/**
 * Created by denni on 6-8-2017.
 */

public class RssItem {

    private final String title;
    private final String link;

    public RssItem(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public String getTitle() {return title;}

    public String getLink() {return link;}

}
