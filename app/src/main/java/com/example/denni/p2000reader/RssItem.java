package com.example.denni.p2000reader;

/**
 * Created by denni on 6-8-2017.
 */

public class RssItem {

    private final String title;
    private final String link;
    private final double geoLat;
    private final double geoLong;


    public RssItem(String title, String link, double geoLat, double geoLong) {
        this.title = title;
        this.link = link;
        this.geoLat = geoLat;
        this.geoLong = geoLong;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public double getGeoLat() {
        return geoLat;
    }

    public double getGeoLong() {
        return geoLong;
    }

    public String toString() {
        return "Title : " + getTitle() + "\nLink : " + getLink() + "\nGeoLat " + getGeoLat() + "\nGeoLong " + getGeoLong() + "\n";
    }
}
