package com.example.clain.smasher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class MyArrayAdapter extends ArrayAdapter<DataScore> {



    private final Context context;

    public MyArrayAdapter(Context context, ArrayList<DataScore> values) {
        super(context, R.layout.cell_layout, values);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View cellView = convertView;

        if (cellView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            cellView = inflater.inflate(R.layout.cell_layout, parent, false);
        }

        TextView titreTextView = (TextView) cellView.findViewById(R.id.titre);
        TextView prioriteTextView = (TextView) cellView.findViewById(R.id.priorite);

        DataScore t = getItem(position);
        titreTextView.setText("Pseudo:  "+t.getPseudo());
        prioriteTextView.setText("Score:  "+t.getScore());






        return cellView;
    }
}


