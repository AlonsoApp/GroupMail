package com.cloupix.groupmail.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.cloupix.groupmail.business.Group;

import java.util.ArrayList;


/**
 * Created by alonsoapp on 23/04/16.
 *
 */
public class Dao {


    private SQLiteDatabase database;
    private SQLHelper sqlHelper;

    public Dao(Context context)
    {
        sqlHelper = new SQLHelper(context);
    }

    public void open() throws SQLException {
        database = sqlHelper.getWritableDatabase();
    }

    public void close()
    {
        sqlHelper.close();
    }

    public boolean isConnectionOpen(){
        if(database==null)
            return false;
        else
            return database.isOpen();
    }

    //<editor-fold desc="Internal classes">


    //</editor-fold>

    //<editor-fold desc="Inserts">

    public long insertGroup(Group group) {
        if(database==null)
            return -1;

        //  Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        if(group.getGroupId() != -1)
            values.put(GroupMailDbContract.Group.COLUMN_NAME_GROUP_ID, group.getGroupId());
        values.put(GroupMailDbContract.Group.COLUMN_NAME_NAME, group.getName());
        values.put(GroupMailDbContract.Group.COLUMN_NAME_ARCHIVED, group.isArchived()?1:0);

        return database.insertWithOnConflict(GroupMailDbContract.Group.TABLE_NAME, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    public long insertContact(com.cloupix.groupmail.business.Contact contact) {
        if(database==null)
            return -1;

        //  Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        if(contact.getContactId() != -1)
            values.put(GroupMailDbContract.Contact.COLUMN_NAME_CONTACT_ID, contact.getContactId());
        values.put(GroupMailDbContract.Contact.COLUMN_NAME_EMAIL, contact.getEmail());
        values.put(GroupMailDbContract.Contact.COLUMN_NAME_GROUP_ID, contact.getGroupId());

        return database.insertWithOnConflict(GroupMailDbContract.Contact.TABLE_NAME, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    //</editor-fold>

    //<editor-fold desc="Queries">


    public ArrayList<Group> getGroups(){
        if(database==null)
            return null;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                GroupMailDbContract.Group.COLUMN_NAME_GROUP_ID,
                GroupMailDbContract.Group.COLUMN_NAME_NAME,
                GroupMailDbContract.Group.COLUMN_NAME_ARCHIVED
        };
        // How you want the results sorted in the resulting Cursor
        //String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor c = database.query(
                GroupMailDbContract.Group.TABLE_NAME,       // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        ArrayList<Group> list = cursorToGroupList(c);
        c.close();
        return list;
    }

    public ArrayList<Group> getGroups(boolean archived){
        if(database==null)
            return null;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                GroupMailDbContract.Group.COLUMN_NAME_GROUP_ID,
                GroupMailDbContract.Group.COLUMN_NAME_NAME,
                GroupMailDbContract.Group.COLUMN_NAME_ARCHIVED
        };

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection = GroupMailDbContract.Group.COLUMN_NAME_ARCHIVED + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Integer.toString(archived?1:0)
        };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor c = database.query(
                GroupMailDbContract.Group.TABLE_NAME,       // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        ArrayList<Group> list = cursorToGroupList(c);
        c.close();
        return list;
    }

    public Group getGroupById(long groupId) {
        if(database==null)
            return null;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                GroupMailDbContract.Group.COLUMN_NAME_GROUP_ID,
                GroupMailDbContract.Group.COLUMN_NAME_NAME,
                GroupMailDbContract.Group.COLUMN_NAME_ARCHIVED
        };

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection = GroupMailDbContract.Group.COLUMN_NAME_GROUP_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(groupId)
        };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor c = database.query(
                GroupMailDbContract.Group.TABLE_NAME,       // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        Group group = cursorToGroup(c);
        c.close();
        return group;
    }


    public ArrayList<com.cloupix.groupmail.business.Contact> getContactsByGroupId(long groupId) {
        if(database==null)
            return null;

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                GroupMailDbContract.Contact.COLUMN_NAME_CONTACT_ID,
                GroupMailDbContract.Contact.COLUMN_NAME_EMAIL,
                GroupMailDbContract.Contact.COLUMN_NAME_GROUP_ID
        };

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection = GroupMailDbContract.Contact.COLUMN_NAME_GROUP_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(groupId)
        };

        // How you want the results sorted in the resulting Cursor
        //String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

        Cursor c = database.query(
                GroupMailDbContract.Contact.TABLE_NAME,       // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        ArrayList<com.cloupix.groupmail.business.Contact> list = cursorToContactList(c);
        c.close();
        return list;
    }


    //</editor-fold>

    //<editor-fold desc="Update">

    public void updateArchiveGroupById(long groupId, boolean archived) {

        if(database==null)
            return;

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(GroupMailDbContract.Group.COLUMN_NAME_ARCHIVED, archived?1:0);

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                GroupMailDbContract.Group.COLUMN_NAME_GROUP_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(groupId)
        };

        // Insert the new row, returning the primary key value of the new row
        database.update(
                GroupMailDbContract.Group.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

    //</editor-fold>

    //<editor-fold desc="Delete">


    public void deleteGroupById(long groupId) {

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                GroupMailDbContract.Group.COLUMN_NAME_GROUP_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(groupId)
        };

        database.delete(
                GroupMailDbContract.Group.TABLE_NAME,
                selection,
                selectionArgs
        );
    }

    public void deleteContactById(long emailEntityId) {

        // A filter declaring which rows to return, formatted as an SQL WHERE clause
        // (excluding the WHERE itself). Passing null will return all rows for the given table.
        String selection =
                GroupMailDbContract.Contact.COLUMN_NAME_CONTACT_ID + "=?";

        // You may include ?s in selection, which will be replaced by the values from selectionArgs,
        // in order that they appear in the selection. The values will be bound as Strings.
        String[] selectionArgs = new String[]{
                Long.toString(emailEntityId)
        };

        database.delete(
                GroupMailDbContract.Contact.TABLE_NAME,
                selection,
                selectionArgs
        );
    }

    //</editor-fold>

    //<editor-fold desc="Converters">


    private Group cursorToGroup(Cursor cursor){
        if(cursor.isBeforeFirst())
            cursor.moveToFirst();
        if(cursor.isAfterLast())
            return null;

        Group group = new Group();

        group.setGroupId(cursor.getLong(
                cursor.getColumnIndexOrThrow(GroupMailDbContract.Group.COLUMN_NAME_GROUP_ID)));
        group.setName(cursor.getString(
                cursor.getColumnIndexOrThrow(GroupMailDbContract.Group.COLUMN_NAME_NAME)));
        group.setArchived(cursor.getInt(
                cursor.getColumnIndexOrThrow(GroupMailDbContract.Group.COLUMN_NAME_ARCHIVED))>0);

        return group;
    }

    private com.cloupix.groupmail.business.Contact cursorToContact(Cursor cursor){
        if(cursor.isBeforeFirst())
            cursor.moveToFirst();
        if(cursor.isAfterLast())
            return null;

        com.cloupix.groupmail.business.Contact contact = new com.cloupix.groupmail.business.Contact();

        contact.setContactId(cursor.getLong(
                cursor.getColumnIndexOrThrow(GroupMailDbContract.Contact.COLUMN_NAME_CONTACT_ID)));
        contact.setEmail(cursor.getString(
                cursor.getColumnIndexOrThrow(GroupMailDbContract.Contact.COLUMN_NAME_EMAIL)));
        contact.setGroupId(cursor.getLong(
                cursor.getColumnIndexOrThrow(GroupMailDbContract.Contact.COLUMN_NAME_GROUP_ID)));

        return contact;
    }


    private ArrayList<Group> cursorToGroupList(Cursor cursor){
        ArrayList<Group> arrayList = new ArrayList<>();

        while(cursor.moveToNext()){
            arrayList.add(cursorToGroup(cursor));
        }

        return arrayList;
    }

    private ArrayList<com.cloupix.groupmail.business.Contact> cursorToContactList(Cursor cursor){
        ArrayList<com.cloupix.groupmail.business.Contact> arrayList = new ArrayList<>();

        while(cursor.moveToNext()){
            arrayList.add(cursorToContact(cursor));
        }

        return arrayList;
    }

    //</editor-fold>

}
