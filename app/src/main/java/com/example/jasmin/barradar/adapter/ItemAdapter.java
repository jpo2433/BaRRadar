package com.example.jasmin.barradar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jasmin.barradar.model.Location;
import com.example.jasmin.barradar.R;

import java.util.List;

/**
 * Created by Jasmin on 18.02.2016.
 */
public class ItemAdapter extends ArrayAdapter<Location> {
    class ViewHolder {
        private ImageView picture;
        private TextView title;
        private TextView description;
        private TextView type;
        private TextView address;
        //Initialize the variables
        public ViewHolder(View view) {
            picture = (ImageView) view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.title);
            type = (TextView) view.findViewById(R.id.type);

        }

        //Method to set the values in a row
        public void populateRow(Location item) {
            //Set the icon for this row
            picture.setImageResource(item.getPictureResource());
            //Set the title for this row
            title.setText(item.getTitle());
            //Set the description for this row
            type.setText(item.getDescription());
        }

    }

    private LayoutInflater inflater;
    public ItemAdapter(Context context, int resource, List<Location> objects){
        super(context, resource, objects);
        inflater = LayoutInflater.from(getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        //Check if the row is new
        if (row == null) {
            //Inflate the layout if it didn't exist yet
            row = inflater.inflate(R.layout.row_item, parent, false);
            //Create a new view holder instance
            holder = new ViewHolder(row);
            //set the holder as a tag so we can get it back later
            row.setTag(holder);
        } else {
            //The row isn't new so we can reuse the view holder
            holder = (ViewHolder) row.getTag();
        }
        //Populate the row
        holder.populateRow(getItem(position));
        return row;
    }
}
