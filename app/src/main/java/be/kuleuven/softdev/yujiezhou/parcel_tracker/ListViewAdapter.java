package be.kuleuven.softdev.yujiezhou.parcel_tracker;

/**
 * Created by yujiezhou on 05/12/2017.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    private Context context;
    private ArrayList<HashMap<String, String>> data;

    ListViewAdapter(Context context,
                    ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // Declare Variables
        TextView origin;
        TextView destination;
        TextView current_location;
        TextView date;
        TextView discription;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        assert inflater != null;
        @SuppressLint("ViewHolder") View itemView = inflater.inflate(R.layout.listview_item, parent, false);
        // Get the position
        HashMap<String, String> resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        origin = (TextView) itemView.findViewById(R.id.origin);
        destination = (TextView) itemView.findViewById(R.id.destination);
        current_location = (TextView) itemView.findViewById(R.id.current_location);
        date = (TextView) itemView.findViewById(R.id.Date_time);
        discription = (TextView) itemView.findViewById(R.id.discription);

        // Capture position and set results to the TextViews
        origin.setText(resultp.get(DisplayActivity.ORIGIN));
        destination.setText(resultp.get(DisplayActivity.DESTINATION));
        current_location.setText(resultp.get(DisplayActivity.CURRENT_LOCATION));
        date.setText(resultp.get(DisplayActivity.DATE));
        discription.setText(resultp.get(DisplayActivity.DISCRIPTION));

        return itemView;
    }
}