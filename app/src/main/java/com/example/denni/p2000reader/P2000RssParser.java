package com.example.denni.p2000reader;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by denni on 6-8-2017.
 */

public class P2000RssParser {

    private static final String TAG_TITLE = "title";
    private static final String TAG_LINK = "link";
    private static final String TAG_GEOLAT = "geo:lat";
    private static final String TAG_GEOLONG = "geo:long";
    private static final String TAG_RSS = "rss";

    private final String ns = null;

    public List<RssItem> parse(InputStream inputStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            inputStream.close();
        }
    }

    private List<RssItem> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, TAG_RSS);
        String title = null;
        String link = null;
        double geoLat = 0;
        double geoLong = 0;
        List<RssItem> items = new ArrayList<RssItem>();
        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(TAG_TITLE)) {
                title = readTitle(parser);
            } else if (name.equals(TAG_LINK)) {
                link = readLink(parser);
            } else if (name.equals(TAG_GEOLAT)) {
                geoLat = readGeoLat(parser);
            } else if (name.equals(TAG_GEOLONG)) {
                geoLong = readGeoLong(parser);
            }
            if (title != null && link != null & geoLat != 0 & geoLong != 0) {
                RssItem item = new RssItem(title, link, geoLat, geoLong);
                items.add(item);
                title = null;
                link = null;
                geoLat = 0;
                geoLong = 0;
            }
        }
        return items;
    }

    private String readLink(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_LINK);
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, TAG_LINK);
        return link;
    }

    private String readTitle(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_TITLE);
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, TAG_TITLE);
        return title;
    }

    private double readGeoLat(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_GEOLAT);
        double geoLat = readNumber(parser);
        parser.require(XmlPullParser.END_TAG, ns, TAG_GEOLAT);
        return geoLat;
    }

    private double readGeoLong(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, TAG_GEOLONG);
        double geoLong = readNumber(parser);
        parser.require(XmlPullParser.END_TAG, ns, TAG_GEOLONG);
        return geoLong;
    }

    private String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private double readNumber(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return Double.parseDouble(result);
    }
}
