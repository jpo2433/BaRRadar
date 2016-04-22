package com.example.jasmin.barradar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jasmin.barradar.adapter.ItemAdapter;
import com.example.jasmin.barradar.database.DataSource;
import com.example.jasmin.barradar.model.Location;
import com.example.jasmin.barradar.R;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListView listView;
    private ItemAdapter adapter;
    private DataSource dataSource;

    public static final String EXTRA_LOCATION_ID = "extraLocationId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataSource = new DataSource(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, AddLocationActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView)findViewById(R.id.list_locations);

        List<Location> locations = dataSource.getAllLocations();
        adapter = new ItemAdapter(this, android.R.layout.simple_list_item_1, locations);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Create an Intent
                Intent intent = new Intent(ListActivity.this, LocationActivity.class);
                intent.putExtra("location_id", adapter.getItem(position).getId());
                startActivityForResult(intent, 111);

//                Location clickedItem = (Location) parent.getItemAtPosition(position);
//                intent.putExtra("title", clickedItem.getTitle());
//                intent.putExtra("description", clickedItem.getDescription());
//                intent.putExtra("picture-resource", clickedItem.getPictureResource());
//                intent.putExtra("type", clickedItem.getType());
//                intent.putExtra("address", clickedItem.getAddress());
//
//                //Open the new screen by starting the activity
//                startActivity(intent);
            }
        });

        registerForContextMenu(listView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100){
            if(resultCode == RESULT_OK) {
                long locationId = data.getLongExtra(EXTRA_LOCATION_ID, -1);
                if(locationId != -1) {
                    Location location = dataSource.getLocation(locationId);
                    adapter.add(location);
                    updateLocationListView();
                }
            }
        }
        if (requestCode == 2 || requestCode == 111) {
            if (resultCode == RESULT_OK) {
                updateLocationListView();
            }
        }
    }

    public void updateLocationListView() {
        List<Location> assignments = dataSource.getAllLocations();
        adapter = new ItemAdapter(this, android.R.layout.simple_list_item_1, assignments);
        listView.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Delete");
        menu.add(1, v.getId(), 1, "Edit");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getTitle() == "Delete") {
            Toast.makeText(getApplicationContext(), "Location deleted", Toast.LENGTH_LONG).show();
            Location assignment = adapter.getItem(info.position);
            adapter.remove(assignment);
            dataSource.deleteLocation(assignment);
            updateLocationListView();
        } else if(item.getTitle() == "Edit") {
            Intent intent = new Intent(ListActivity.this, EditLocationActivity.class);
            intent.putExtra("location_id", adapter.getItem(info.position).getId());
            startActivityForResult(intent, 111);
        }else{
            return false;
        }
        return true;
    }

}
