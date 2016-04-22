package com.example.jasmin.barradar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.jasmin.barradar.model.Comment;
import com.example.jasmin.barradar.model.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasmin on 23.02.2016.
 */
public class DataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] locationAllColumns = { MySQLiteHelper.COLUMN_LOCATION_ID, MySQLiteHelper.COLUMN_LOCATION_NAME, MySQLiteHelper.COLUMN_LOCATION_TYPE, MySQLiteHelper.COLUMN_LOCATION_ADDRESS, MySQLiteHelper.COLUMN_LOCATION_DESCRIPTION, MySQLiteHelper.COLUMN_LOCATION_IMAGE };

    private String[] commentsAllColums = {MySQLiteHelper.COLUMN_COMMENT_ID, MySQLiteHelper.COLUMN_COMMENT_LOCATION_ID, MySQLiteHelper.COLUMN_COMMENT};

    public DataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
        database = dbHelper.getWritableDatabase();
        dbHelper.close();
    }

    // Opens the database to use it
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    // Closes the database when you no longer need it
    public void close() {
        dbHelper.close();
    }

    public long createLocation(Location location) {
        // If the database is not open yet, open it
        if (!database.isOpen())
            open();

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_LOCATION_NAME, location.getTitle());
        values.put(MySQLiteHelper.COLUMN_LOCATION_TYPE, location.getType());
        values.put(MySQLiteHelper.COLUMN_LOCATION_ADDRESS, location.getAddress());
        values.put(MySQLiteHelper.COLUMN_LOCATION_DESCRIPTION, location.getDescription());
        //TODO save picture
//        values.put(MySQLiteHelper.COLUMN_LOCATION_IMAGE, location.getPictureResource());
        long insertId = database.insert(MySQLiteHelper.TABLE_LOCATIONS, null, values);
        location.setId(insertId);
        // If the database is open, close it
        if (database.isOpen())
            close();

        return insertId;
    }

    public long createComment(Comment comment, Location location){
        if (!database.isOpen())
            open();

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_COMMENT_LOCATION_ID, comment.getLocation());
        values.put(MySQLiteHelper.COLUMN_COMMENT, comment.getComment());

        long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS, null, values);
        comment.setId(insertId);
//        location.addComment(comment);

        if (database.isOpen())
            close();

        return insertId;
    }

    public void deleteLocation(Location location) {
        if (!database.isOpen())
            open();

        database.delete(MySQLiteHelper.TABLE_LOCATIONS, MySQLiteHelper.COLUMN_LOCATION_ID + " =?", new String[]{Long.toString(location.getId())});

        if (database.isOpen())
            close();
    }

    public void deleteComment(Comment comment){
        if (!database.isOpen())
            open();

        //delete from list
//        location.removeComment(comment);

        //delete from database
        database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_COMMENT_ID + "=?", new String[]{Long.toString(comment.getId())});

        if (database.isOpen())
            close();
    }

    public void updateLocation(Location location) {
        if (!database.isOpen())
            open();

        ContentValues args = new ContentValues();
        args.put(MySQLiteHelper.COLUMN_LOCATION_NAME, location.getTitle());
        args.put(MySQLiteHelper.COLUMN_LOCATION_TYPE, location.getType());
        args.put(MySQLiteHelper.COLUMN_LOCATION_ADDRESS, location.getAddress());
        args.put(MySQLiteHelper.COLUMN_LOCATION_DESCRIPTION, location.getDescription());

        //TODO update picture

        database.update(MySQLiteHelper.TABLE_LOCATIONS, args, MySQLiteHelper.COLUMN_LOCATION_ID + "=?", new String[]{Long.toString(location.getId())});
        if (database.isOpen())
            close();
    }

    public List<Location> getAllLocations() {
        if (!database.isOpen())
            open();

        List<Location> locations = new ArrayList<Location>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_LOCATIONS, locationAllColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Location location = cursorToLocation(cursor);
            locations.add(location);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        if (database.isOpen())
            close();

        return locations;
    }

    public List<Comment> getAllComments(Location location){
        if (!database.isOpen())
            open();

        List<Comment> comments = new ArrayList<Comment>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS, commentsAllColums, MySQLiteHelper.COLUMN_COMMENT_LOCATION_ID +"=?", new String[] {Long.toString(location.getId())}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Comment comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();

        if (database.isOpen())
            close();

        return comments;
    }


    private Location cursorToLocation(Cursor cursor) {
        try {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_LOCATION_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_LOCATION_NAME));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_LOCATION_TYPE));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_LOCATION_ADDRESS));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_LOCATION_DESCRIPTION));

            //TODO picture

            Location location = new Location(name, description, type, address, 0);
            location.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_LOCATION_ID)));

            return location;
        } catch(CursorIndexOutOfBoundsException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    private Comment cursorToComment(Cursor cursor) {
        try {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_COMMENT_ID));
            long location = cursor.getLong(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_COMMENT_LOCATION_ID));
            String comment = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.COLUMN_COMMENT));

            Comment comment1 = new Comment(location, comment);
            comment1.setId(id);

            return comment1;
        } catch(CursorIndexOutOfBoundsException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public Location getLocation(long columnId) {
        if (!database.isOpen())
            open();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_LOCATIONS, locationAllColumns, MySQLiteHelper.COLUMN_LOCATION_ID + "=?", new String[] { Long.toString(columnId)}, null, null, null);

        cursor.moveToFirst();
        Location location = cursorToLocation(cursor);
        cursor.close();

        if (database.isOpen())
            close();

        return location;
    }

}
