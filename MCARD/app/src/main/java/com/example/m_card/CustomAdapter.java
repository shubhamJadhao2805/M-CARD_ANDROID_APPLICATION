package com.example.m_card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    int logos[];
    String texts[];
    ArrayList<String> arrayList;
    LayoutInflater inflter;
    String times[];
    public CustomAdapter(Context applicationContext, int[] logos,ArrayList<String> texts,String[] times) {
        this.context = applicationContext;
        this.logos = logos;
        this.arrayList = texts;
        this.times = times;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return arrayList.size();
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
        view = inflter.inflate(R.layout.activity_gride, null); // inflate the layout
        try {
//            ImageView icon = (ImageView) view.findViewById(R.id.icon); // get the reference of ImageView
//            icon.setImageResource(logos[i]); // set logo images

            TextView dates = (TextView)view.findViewById(R.id.datesNames);
            dates.setText(arrayList.get(i));

        }catch (Exception e){

        }
        return view;
    }
}