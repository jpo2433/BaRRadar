package com.example.jasmin.barradar.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.jasmin.barradar.R;
import com.example.jasmin.barradar.database.DataSource;
import com.example.jasmin.barradar.model.Location;

public class AddLocationActivity extends AppCompatActivity {

    private DataSource datasource;
    private EditText name;
    private EditText type;
    private EditText address;
    private EditText description;
    private ImageButton image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        datasource = new DataSource(this);

        name = (EditText) findViewById(R.id.add_location_name);
        type = (EditText) findViewById(R.id.add_location_type);
        address = (EditText) findViewById(R.id.add_location_address);
        description = (EditText) findViewById(R.id.add_location_description);
        image = (ImageButton) findViewById(R.id.add_location_image);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location location = new Location(name.getText().toString(), description.getText().toString(), type.getText().toString(), address.getText().toString(), 0);
                //TODO handle image
                long locationId = datasource.createLocation(location);
                Intent resultIntent = new Intent();
                resultIntent.putExtra(ListActivity.EXTRA_LOCATION_ID, locationId);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
