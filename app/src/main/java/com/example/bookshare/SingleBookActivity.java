package com.example.bookshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookshare.adapter.ListAdapterComments;

public class SingleBookActivity extends AppCompatActivity {

    private String bookName;
    private int bookId;
    private TextView book_title, book_price, avg_rating, total_commenter, book_description, total_comments;
    private TextView main_rating, total_ratings, total_5star_ratings, total_4star_ratings, total_3star_ratings, total_2star_ratings, total_1star_ratings;
    private ImageView backBtn, bookDP;
    private TabHost host;
    private Button submit_review, submit_comment;
    private ListView commentList;

    private String[] usernames = {
            "Book - 1", "Book - 2", "Book - 3", "Book - 4",
            "Book - 5"
    };
    private String[] descriptions = {
            "very good book_icon", "useful", "informative", "best book_icon i have ever read!",
            "described clearly"
    };
    //private TabWidget widget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_book);
        if(!isConnected(getApplicationContext())){
            Intent loadAgain = new Intent(getApplicationContext(),SplashActivity.class);
            startActivity(loadAgain);
        }

        Intent intent = getIntent();
        bookName = intent.getStringExtra("book_title");
        bookId = intent.getIntExtra("book_id",0);

        //setTitle
        book_title = (TextView) findViewById(R.id.book_title);
        book_title.setText(bookName);

        //set avg rating
        avg_rating = (TextView) findViewById(R.id.avg_rating);
        avg_rating.setText("4.5");

        //set total commenter
        total_commenter = (TextView) findViewById(R.id.total_commenter);
        total_commenter.setText("12");

        //set price
        book_price = (TextView) findViewById(R.id.book_price);
        book_price.setText("160" + " Tk.");

        //action back
        backBtn = (ImageView) findViewById(R.id.btn_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Book Image set
        bookDP = (ImageView) findViewById(R.id.book_dp);
        bookDP.setImageResource(R.drawable.ummu_rawsulina);

        //TabHost set
        host = (TabHost)findViewById(R.id.tabHost);
        host.setup();


        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        //set the content for the tab as we set content for an activity in the oncreate method
        //you can pass an intent to this as well to load an activity or load a fragment
        //since we have defined our content in the activity_main.xml
        // we will pass the tab1 id to our setcontent method

        spec.setContent(R.id.tab1);
        spec.setIndicator("", getResources().getDrawable(R.drawable.tab_selector_one));
        host.addTab(spec);

        book_description = findViewById(R.id.book_description);
        book_description.setText("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book_icon. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker.");

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("", getResources().getDrawable(R.drawable.tab_selector_two));
        host.addTab(spec);

        main_rating = findViewById(R.id.main_rating);
        main_rating.setText("4.73");

        total_ratings = findViewById(R.id.total_ratings);
        total_ratings.setText("34");

        total_5star_ratings = findViewById(R.id.total_5star_ratings);
        total_5star_ratings.setText("12");

        total_4star_ratings = findViewById(R.id.total_4star_ratings);
        total_4star_ratings.setText("8");

        total_3star_ratings = findViewById(R.id.total_3star_ratings);
        total_3star_ratings.setText("5");

        total_2star_ratings = findViewById(R.id.total_2star_ratings);
        total_2star_ratings.setText("2");

        total_1star_ratings = findViewById(R.id.total_1star_ratings);
        total_1star_ratings.setText("7");

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("", getResources().getDrawable(R.drawable.tab_selector_three));
        host.addTab(spec);

        submit_review = findViewById(R.id.submit_review);
        submit_comment = findViewById(R.id.submit_comment);

        total_comments = findViewById(R.id.total_comments);
        total_comments.setText(12+"");

        commentList = (ListView) findViewById(R.id.comment_list);
        ListAdapterComments adapter = new ListAdapterComments(usernames, descriptions,getApplicationContext());
        commentList.setAdapter(adapter);
        commentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"Position "+position,Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getApplicationContext(),SingleBookActivity.class);
                //intent.putExtra("book_title", usernames[position]);
                //intent.putExtra("book_id",position);
                //startActivity(intent);
            }
        });
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
