package com.example.onthi_provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, "Bookstore", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("Create table " +
                "if not exists Author(id_author integer primary key autoincrement, name text, email text, address text)");
        sqLiteDatabase.execSQL("Create table " +
                "if not exists Book(id_book integer primary key autoincrement, title text, " +
                "id_author integer constraint id_author references Author(id_author) " +
                "on delete cascade on update cascade)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists Author");
        sqLiteDatabase.execSQL("drop table if exists Book");
    }
}
