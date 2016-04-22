package com.example.jasmin.barradar.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.jasmin.barradar.R;
import com.example.jasmin.barradar.database.DataSource;
import com.example.jasmin.barradar.model.Location;

public class EditLocationActivity extends AppCompatActivity {

    private DataSource datasource;
    private EditText name;
    private EditText type;
    private EditText address;
    private EditText description;
    private ImageButton image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        datasource = new DataSource(this);
        name = (EditText) findViewById(R.id.add_location_name);
        type = (EditText) findViewById(R.id.add_location_type);
        address = (EditText) findViewById(R.id.add_location_address);
        description = (EditText) findViewById(R.id.add_location_description);
        image = (ImageButton) findViewById(R.id.add_location_image);

        final long location_id = getIntent().getLongExtra("location_id", -1);
        final Location location = datasource.getLocation(location_id);

        name.setText(location.getTitle());
        type.setText(location.getType());
        address.setText(location.getAddress());
        description.setText(location.getDescription());

        //TODO image

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name1 = name.getText().toString();
                String description1 = description.getText().toString();
                String type1 = type.getText().toString();
                String address1 = address.getText().toString();

                //TODO image
                location.setTitle(name1);
                location.setType(type1);
                location.setAddress(address1);
                location.setDescription(description1);
                //TODO image

                datasource.updateLocation(location);
                Toast.makeText(EditLocationActivity.this, "Location Updated", Toast.LENGTH_SHORT).show();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("location_id", location.getId());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
