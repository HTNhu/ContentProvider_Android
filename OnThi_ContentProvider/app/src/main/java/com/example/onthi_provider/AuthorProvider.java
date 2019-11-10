package com.example.onthi_provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class AuthorProvider extends ContentProvider {
    private SQLiteDatabase db;
    static final String AUTHORITY = "com.example.onthi_provider.AuthorProvider";
    static final int ALLAUTHORS = 1;
    static final int AUTHOR_ID = 2;

    static  final String CONTENT_PATH  = "Bookstore";
    static  final  Uri CONTENT_URI = Uri.parse("content://" +AUTHORITY +"/" + CONTENT_PATH);

    private static HashMap<String, String> AUTHOR_PROJECTION_MAP;
    static  final UriMatcher uriMatcher = new UriMatcher( UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, CONTENT_PATH, ALLAUTHORS);
        uriMatcher.addURI(AUTHORITY, CONTENT_PATH + "/#", AUTHOR_ID);
    }
    @Override
    public boolean onCreate() {
        DBHelper dbHelper = new DBHelper(getContext());
        db = dbHelper.getWritableDatabase();
        if(db != null)
        return true;
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables("Author");
        switch (uriMatcher.match(uri)){
            case ALLAUTHORS: {
                    sqLiteQueryBuilder.setProjectionMap(AUTHOR_PROJECTION_MAP);
                    break;
            }
            case AUTHOR_ID:{
                    sqLiteQueryBuilder.appendWhere("id_author =" + uri.getPathSegments().get(1));
                    break;
            }
        }
        if(sortOrder == "" || sortOrder == null) {
            sortOrder = "name";
        }
        Cursor cursor = sqLiteQueryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return getContext().getContentResolver().getType(Settings.System.CONTENT_URI);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
       long row = db.insert("Author",null, contentValues);
        if(row > 0) {
            Uri uriReturn = ContentUris.withAppendedId(CONTENT_URI, row);
            getContext().getContentResolver().notifyChange(uriReturn, null);
            return uriReturn;
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArg) {
            int count = db.delete("Author", selection, selectionArg);
            getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
       int count = db.update("Author", contentValues, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
