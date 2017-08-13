package com.example.denni.p2000reader;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.List;

/**
 * Created by denni on 6-8-2017.
 */

public class RssService extends IntentService {
    public static final String ITEMS = "items";
    public static final String ACTION_RSS_PARSED = "com.p2000reader.ACTION_RSS_PARSED";
    private static final String RSS_LINK = "http://feeds.livep2000.nl/";


    public RssService() { super("RssService"); }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(Constants.TAG, "Service started");
        List<RssItem> rssItems = null;
        try {
            P2000RssParser parser = new P2000RssParser();
            rssItems = parser.parse(getInputStream(RSS_LINK));
        } catch (XmlPullParserException | IOException e) {
            Log.w(e.getMessage(), e);
        }

        Intent resultIntent = new Intent(ACTION_RSS_PARSED);
        resultIntent.putExtra(ITEMS, (Serializable) rssItems);
        LocalBroadcastManager.getInstance(this).sendBroadcast(resultIntent);
    }

    public InputStream getInputStream(String link) {
        try {
            URL url = new URL(link);
            return url.openConnection().getInputStream();
        } catch(IOException e) {
            Log.w(Constants.TAG, "Exception while receiving the input stream", e);
            return null;
        }
    }
}
