package com.example.mpd_coursework;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.mpd_coursework.ui.main.PlaceholderFragment;

import java.util.ArrayList;

public class FirstFragment extends Fragment {
    ArrayList<String> ListViewTitle = new ArrayList();
    ArrayList<String> ListViewDescription = new ArrayList();
    public FirstFragment()
    {
        // Empty public constructor
        AssignData();
    }

    public void AssignData(){
        DataFeed getFeed = new DataFeed();
        getFeed.execute();

        ListViewTitle = getFeed.getHeadlines();
        ListViewDescription = getFeed.getDescriptions();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.list_layout, container, false);
        ListView listView = (ListView)root.findViewById(R.id.listView);

        AssignData();

        FirstFragment.CustomAdapter adapter = new FirstFragment.CustomAdapter();
        listView.setAdapter(adapter);

        //DataFeed getFeed = new DataFeed();
        //getFeed.execute();

        return root;
    }

    private class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount()
        {
            return ListViewTitle.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View itemView = getLayoutInflater().inflate(R.layout.item_layout, null);

            TextView name = itemView.findViewById(R.id.Title);
            TextView desc = itemView.findViewById(R.id.Description);

            name.setText(ListViewTitle.get(i));
            desc.setText(ListViewDescription.get(i));

            return itemView;
        }
    }
}
