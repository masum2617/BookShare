package com.example.bookshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookshare.adapter.ListAdapterCategoryBookList;
import com.example.bookshare.model.Book;
import com.example.bookshare.model.GlobalData;
import com.example.bookshare.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserBooks extends AppCompatActivity {

    private String userName;
    private int userId;
    private TextView user_name, foundBooksQuantity, foundMsg;
    private ImageView backBtn;
    private ProgressBar pbUserBookList;
    private ListView userBookList;
    private String[] bookNames = {
            "Book - 1", "Book - 2", "Book - 3", "Book - 4",
            "Book - 5", "Book - 6", "Book - 7", "Book - 8",
            "Book - 9", "Book - 10", "Book - 11", "Book - 12"
    };
    private List<Book> bookList = new ArrayList<Book>();

    SharedPreferences preferences;
    public static final String FILE_NAME="preferenceFile";

    private String url = GlobalData.url;
    private String action = "?action=";
    private String apiName = "";

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_books);

        if(!isConnected(getApplicationContext())){
            Intent loadAgain = new Intent(getApplicationContext(),SplashActivity.class);
            startActivity(loadAgain);
        }

        pbUserBookList = (ProgressBar) findViewById(R.id.pbUserBookList);
        pbUserBookList.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        //userName = intent.getStringExtra("title");
        //userId = intent.getIntExtra("id",1);
        user = (User) intent.getSerializableExtra("user");

        //setTitle
        user_name = (TextView) findViewById(R.id.user_name);
        user_name.setText(user.title);

        //fetch books of this category
        //getThisCategoryBooks();

        foundBooksQuantity = (TextView) findViewById(R.id.foundBooksQuantity);
        foundMsg = (TextView) findViewById(R.id.foundMsg);
        System.out.println("Category book_icon list size="+bookList.size());

        //action back
        backBtn = (ImageView) findViewById(R.id.btn_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userBookList = (ListView) findViewById(R.id.user_book_list);

        bookList.add(new Book(1,"Book - 1", 270));
        bookList.add(new Book(2,"Book - 2", 165));
        bookList.add(new Book(3,"Book - 3", 310));

        if(bookList.size()>0){
            foundBooksQuantity.setText(bookList.size()+"");
            foundMsg.setVisibility(View.VISIBLE);

            ListAdapterCategoryBookList adapter = new ListAdapterCategoryBookList(bookList,getApplicationContext());
            userBookList.setAdapter(adapter);
            userBookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Book book = bookList.get(position);
                    Toast.makeText(getApplicationContext(),"Position "+position,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),SingleBookActivity.class);
                    intent.putExtra("book_title", book.getTitle());
                    intent.putExtra("book_id",book.getId());
                    startActivity(intent);
                }
            });
        }
        pbUserBookList.setVisibility(View.GONE);
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