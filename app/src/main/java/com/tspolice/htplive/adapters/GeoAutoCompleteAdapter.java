package com.tspolice.htplive.adapters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.tspolice.htplive.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeoAutoCompleteAdapter {/*extends BaseAdapter implements Filterable {

    private Context mContext;
    private List<GeoSearchResult> resultList;
    private static final int MAX_RESULTS = 10;

    public GeoAutoCompleteAdapter(Context context) {
        this.mContext = context;
        resultList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    *//*@Override
    public Object getItem(int position) {
        return null;
    }*//*

    @Override
    public GeoSearchResult getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.geo_search_result, parent, false);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.geo_search_result_text);
        textView.setText(getItem(position).getAddress());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List locations = findLocations(mContext, constraint.toString());
                    // Assign the data to the FilterResults
                    filterResults.values = locations;
                    filterResults.count = locations.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    resultList = (List) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

    private List<GeoSearchResult> findLocations(Context context, String query_text) {
        List<GeoSearchResult> geo_search_results = new ArrayList<GeoSearchResult>();
        Geocoder geocoder = new Geocoder(context, context.getResources().getConfiguration().locale);
        List<Address> addresses;
        try {
            // Getting a maximum of 15 Address that matches the input text
            addresses = geocoder.getFromLocationName(query_text, 15);
            for(int i=0;i<addresses.size();i++){
                Address address = (Address) addresses.get(i);
                if(address.getMaxAddressLineIndex() != -1)
                {
                    geo_search_results.add(new GeoSearchResult(address));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return geo_search_results;
    }*/
}
