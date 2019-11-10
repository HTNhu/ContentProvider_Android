package com.example.onthi_provider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContentValues author = new ContentValues();
        author.put("name", "NhuHT");
        author.put("address", "Quan 12");
        author.put("email", "nhu@gmail.com");
        ContentValues author1 = new ContentValues();
        author1.put("name", "KyLT");
        author1.put("address", "Quan 12");
        author1.put("email", "ky@gmail.com");
        ContentValues author2 = new ContentValues();
        author2.put("name", "HieuTM");
        author2.put("address", "Quan 12");
        author2.put("email", "hieu@gmail.com");
        String uri = "content://com.example.onthi_provider.AuthorProvider";
        Uri aut = Uri.parse(uri);
        getContentResolver().insert(aut, author);
        getContentResolver().insert(aut, author1);
        getContentResolver().insert(aut, author2);
    }
}
