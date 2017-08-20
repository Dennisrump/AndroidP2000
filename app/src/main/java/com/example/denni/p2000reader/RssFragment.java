package com.example.denni.p2000reader;



import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.widget.SwipeRefreshLayout;
import java.util.List;

/**
 * Created by denni on 6-8-2017.
 */

public class RssFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView listView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        mSwipeRefreshLayout.setColorSchemeColors(Color.GRAY, Color.GREEN, Color.BLUE,
                Color.RED, Color.CYAN);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FetchNewItems();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 4000);
            }
        });
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        startService();
    }

    private void startService() {
        Intent intent = new Intent(getActivity(), RssService.class);
        getActivity().startService(intent);
    }
    private BroadcastReceiver resultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<RssItem> items = (List<RssItem>) intent.getSerializableExtra(RssService.ITEMS);
            if (items != null) {
                RssAdapter adapter = new RssAdapter(getActivity(), items);
                listView.setAdapter(adapter);
                Log.d("List", items.toString());
            } else {
                Toast.makeText(getActivity(), "An error occured while downloading the rss feed.", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RssAdapter adapter = (RssAdapter) parent.getAdapter();
        RssItem item = (RssItem) adapter.getItem(position);
        double Lat = item.getGeoLat();
        double Long = item.getGeoLong();
        Uri uri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Lat + "," + Long);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(RssService.ACTION_RSS_PARSED);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(resultReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(resultReceiver);
    }

    // Moet nog verbeterd worden -> nu wordt de hele fragment opnieuw opgebouwd en bewaard in de achtergrond
    public void FetchNewItems() {
        listView.setAdapter(null);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(this);
        fragmentTransaction.attach(this);
        fragmentTransaction.commit();
    }
}
