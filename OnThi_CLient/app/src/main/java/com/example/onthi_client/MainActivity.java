package com.example.onthi_client;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
GridView gvAuthor ;
EditText edtName;
EditText edtAddress;
EditText edtEmail;
EditText edtID;
Button btnSave, btnDel, btnClear, btnUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gvAuthor = (GridView) findViewById(R.id.gvAuthor);
        edtName = (EditText)findViewById(R.id.edtName);
        edtAddress = (EditText)findViewById(R.id.edtAddress);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtID = (EditText)findViewById(R.id.edtID);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnDel = (Button)findViewById(R.id.btnDelete);
        btnClear = (Button)findViewById(R.id.btnClear);
        btnUpdate = (Button)findViewById(R.id.btnUpdate);
        final Uri uri = Uri.parse("content://com.example.onthi_provider.AuthorProvider");
        loadData(uri);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtID.setText("");
                edtName.setText("");
                edtAddress.setText("");
                edtEmail.setText("");
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    ContentValues author = new ContentValues();
                    author.put("name", edtName.getText().toString());
                    author.put("address", edtAddress.getText().toString());
                    author.put("email", edtEmail.getText().toString());
                    getContentResolver().insert( uri ,author );
                    Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    loadData(uri);
                }catch (Exception ex){
                    Toast.makeText(MainActivity.this, "Thêm không thành công", Toast.LENGTH_SHORT).show();
                }


            }
        });

        gvAuthor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int idPos = (Math.round(i/4)) * 4;
                edtID.setText(adapterView.getItemAtPosition(idPos).toString());
                edtName.setText(adapterView.getItemAtPosition(idPos+1).toString());
                edtAddress.setText(adapterView.getItemAtPosition(idPos+2).toString());
                edtEmail.setText(adapterView.getItemAtPosition(idPos+3).toString());
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    final int id = Integer.parseInt(edtID.getText().toString());
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Xóa");
                    builder.setMessage("Bạn có chắc muốn xóa author id " + id );
                    builder.setCancelable(true);
                    builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int count = getContentResolver().delete(uri, "id_author = ? ", new String[]{ id +""});
                            if(count > 0) {
                                Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                                loadData(uri);
                            }
                            else Toast.makeText(MainActivity.this, "Xóa không thành công", Toast.LENGTH_SHORT).show();

                        }
                    });
                    builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }catch (Exception ex){
                    Toast.makeText(MainActivity.this, "Xóa không thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    ContentValues author = new ContentValues();
//                author.put("id_author", Integer.parseInt(edtID.getText().toString()));
                    author.put("name", edtName.getText().toString());
                    author.put("address", edtAddress.getText().toString());
                    author.put("email" , edtEmail.getText().toString());

                    int count = getContentResolver().update(uri,author, "id_author = ? " , new String[]{edtID.getText().toString()});
                    if(count > 0) {
                        Toast.makeText(MainActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        loadData(uri);
                    }else{
                        Toast.makeText(MainActivity.this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex){
                    Toast.makeText(MainActivity.this, "Cập nhật không thành công", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    public void loadData(Uri uri){
        ArrayList<String> listString = new ArrayList<>();
        Cursor cursor = getContentResolver().query(uri,null,null,null,"id_author");
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                listString.add(cursor.getInt(0)+"");
                listString.add(cursor.getString(1));
                listString.add(cursor.getString(2));
                listString.add(cursor.getString(3));
                cursor.moveToNext();
            } while(!cursor.isAfterLast());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_list_item_1,listString);

            gvAuthor.setAdapter(adapter);
        }
        else {
            Toast.makeText(MainActivity.this, "Dữ liệu rỗng", Toast.LENGTH_SHORT).show();
        }
    }
}
