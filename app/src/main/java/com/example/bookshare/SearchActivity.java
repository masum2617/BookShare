package com.example.bookshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bookshare.adapter.ListAdapterCategoryBookList;
import com.example.bookshare.model.Book;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private String categoryName;
    private EditText searchName;
    private ImageView backBtn;
    private ListView searchBookList;
    private List<Book> bookList = new ArrayList<Book>();
    private String[] bookNames = {
            "Book - 1", "Book - 2", "Book - 3", "Book - 4",
            "Book - 5", "Book - 6", "Book - 7", "Book - 8",
            "Book - 9", "Book - 10", "Book - 11", "Book - 12"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        if(!isConnected(getApplicationContext())){
            Intent loadAgain = new Intent(getApplicationContext(),SplashActivity.class);
            startActivity(loadAgain);
        }

        searchBookList = (ListView) findViewById(R.id.search_book_list);
        ListAdapterCategoryBookList adapter = new ListAdapterCategoryBookList(bookList,getApplicationContext());
        searchBookList.setAdapter(adapter);
        searchBookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"Position "+position,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),SingleBookActivity.class);
                intent.putExtra("book_title", bookNames[position]);
                intent.putExtra("book_id",position);
                startActivity(intent);
            }
        });

        //action back
        backBtn = (ImageView) findViewById(R.id.btn_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Search text
        searchName = (EditText) findViewById(R.id.search_name);
    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        }
        else return false;
    }
}
