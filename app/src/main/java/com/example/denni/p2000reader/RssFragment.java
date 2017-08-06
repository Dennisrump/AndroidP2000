package com.example.denni.p2000reader;

        import android.app.Fragment;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ListView;
        import android.widget.ProgressBar;
        import java.util.List;
/**
 * Created by denni on 6-8-2017.
 */

public class RssFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ProgressBar progressBar;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        progressBar = (ProgressBar) view.findViewByID(R.id.progressBar);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        return view;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

}
