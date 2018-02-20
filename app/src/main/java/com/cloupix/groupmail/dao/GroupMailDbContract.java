package com.cloupix.groupmail.dao;

import android.provider.BaseColumns;

/**
 * Created by alonsoapp on 23/04/16.
 *
 */
public class GroupMailDbContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public GroupMailDbContract() {}


    //<editor-fold desc="Tables">

    public static abstract class Group implements BaseColumns {

        public static final String TABLE_NAME = "email_group";
        public static final String COLUMN_NAME_GROUP_ID = "group_id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_ARCHIVED = "archived";
    }

    public static abstract class Contact implements BaseColumns {

        public static final String TABLE_NAME = "contact";
        public static final String COLUMN_NAME_CONTACT_ID = "contact_id";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_GROUP_ID = "group_id";
    }

    //</editor-fold>


    //<editor-fold desc="Statements">

    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ", ";

    public static final String SQL_CREATE_TABLE_GROUP =
            "CREATE TABLE " + Group.TABLE_NAME + " (" +
                    Group.COLUMN_NAME_GROUP_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                    Group.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    Group.COLUMN_NAME_ARCHIVED + INTEGER_TYPE + COMMA_SEP +
                    " UNIQUE (" + Group.COLUMN_NAME_GROUP_ID + ") ON CONFLICT REPLACE" +
                    " );";
    public static final String SQL_CREATE_TABLE_CONTACT =
            "CREATE TABLE " + Contact.TABLE_NAME + " (" +
                    Contact.COLUMN_NAME_CONTACT_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                    Contact.COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP +
                    Contact.COLUMN_NAME_GROUP_ID + INTEGER_TYPE + COMMA_SEP +
                    " FOREIGN KEY ("+ Contact.COLUMN_NAME_GROUP_ID+")" +
                    " REFERENCES "+ Group.TABLE_NAME+" ("+ Group.COLUMN_NAME_GROUP_ID+") ON DELETE CASCADE" +
                    " UNIQUE (" + Contact.COLUMN_NAME_CONTACT_ID + ") ON CONFLICT REPLACE" +
                    " );";

    public static final String SQL_DELETE_TABLE_GROUP =
            "DROP TABLE IF EXISTS " + Group.TABLE_NAME;
    public static final String SQL_DELETE_TABLE_CONTACT =
            "DROP TABLE IF EXISTS " + Contact.TABLE_NAME;

    //</editor-fold>
}
