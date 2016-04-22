package com.example.jasmin.barradar.activities;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jasmin.barradar.R;
import com.example.jasmin.barradar.database.DataSource;
import com.example.jasmin.barradar.model.Comment;
import com.example.jasmin.barradar.model.Location;

import java.util.List;

public class LocationActivity extends AppCompatActivity {

    private DataSource datasource;
    private TextView title;
    private TextView type;
    private TextView address;
    private TextView description;

    private ArrayAdapter<Comment> commentArrayAdapter;

    private EditText comment;
    private Button addComment;
    private ListView comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        datasource = new DataSource(this);
        final long location_id = getIntent().getLongExtra("location_id", -1);

        title = (TextView) findViewById(R.id.location_title);
        type = (TextView) findViewById(R.id.location_type);
        address = (TextView) findViewById(R.id.location_address);
        description = (TextView) findViewById(R.id.location_description);
        comment = (EditText) findViewById(R.id.comment);
        addComment = (Button) findViewById(R.id.saveComment);
        comments = (ListView) findViewById(R.id.listComments);

        final Location location = refreshLocationView(location_id);
        List<Comment> co = datasource.getAllComments(location);
        commentArrayAdapter = new ArrayAdapter<Comment>(this, android.R.layout.simple_list_item_1, co);
        comments.setAdapter(commentArrayAdapter);
        registerForContextMenu(comments);

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = comment.getText().toString();
                Comment com = new Comment(location_id, text);
                datasource.createComment(com, location);
                comment.setText("");
                commentArrayAdapter.notifyDataSetChanged();
                refreshLocationView(location_id);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LocationActivity.this, EditLocationActivity.class);
                intent.putExtra("location_id", location_id);
                startActivityForResult(intent, 110);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 110){
            if(resultCode == RESULT_OK) {
               refreshLocationView(data.getExtras().getLong("location_id"));
            }
        }
    }

    private Location refreshLocationView(long id){
        //TODO image
        final Location location = datasource.getLocation(id);
        title.setText(location.getTitle());
        type.setText(location.getType());
        address.setText(location.getAddress());
        description.setText(location.getDescription());

        List<Comment> co = datasource.getAllComments(location);
        commentArrayAdapter = new ArrayAdapter<Comment>(this, android.R.layout.simple_list_item_1, co);
        comments.setAdapter(commentArrayAdapter);

        return location;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getTitle() == "Delete") {
            Toast.makeText(getApplicationContext(), "Comment deleted", Toast.LENGTH_LONG).show();
            Comment comment  = commentArrayAdapter.getItem(info.position);
            commentArrayAdapter.remove(comment);
            datasource.deleteComment(comment);

           refreshLocationView(comment.getLocation());
        } else {
            return false;
        }
        return true;
    }
}
