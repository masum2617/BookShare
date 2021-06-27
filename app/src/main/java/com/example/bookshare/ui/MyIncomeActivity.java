package com.example.bookshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookshare.adapter.ListAdapterCategoryBookList;
import com.example.bookshare.adapter.ListAdapterEarningBookList;
import com.example.bookshare.model.Book;
import com.example.bookshare.model.GlobalData;
import com.example.bookshare.model.Order;

import java.util.ArrayList;
import java.util.List;

public class MyEarningsActivity extends AppCompatActivity {

    private int orderId;
    private TextView totalEarningAmount;
    private ImageView backBtn;
    private ProgressBar pbEarningBookList;
    private ListView earningBookList;

    private List<Order> orderList = new ArrayList<Order>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_earnings);

        pbEarningBookList = (ProgressBar) findViewById(R.id.pbEarningBookList);
        pbEarningBookList.setVisibility(View.VISIBLE);

        earningBookList = (ListView) findViewById(R.id.earning_book_list);
        totalEarningAmount = (TextView) findViewById(R.id.total_earning);
        totalEarningAmount.setText(GlobalData.getInstance().totalEarning+"");

        if(GlobalData.getInstance().orderList.size()>0){

            ListAdapterEarningBookList adapter = new ListAdapterEarningBookList(GlobalData.getInstance().orderList,getApplicationContext());
            earningBookList.setAdapter(adapter);
            pbEarningBookList.setVisibility(View.GONE);
            earningBookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Order order = GlobalData.getInstance().orderList.get(position);
                    Toast.makeText(getApplicationContext(),"Position "+position,Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),SingleBookActivity.class);
                    intent.putExtra("book_title", order.getBookTitle());
                    intent.putExtra("book_id",order.getBookId());
                    //startActivity(intent);
                }
            });
        }

        //action back
        backBtn = (ImageView) findViewById(R.id.btn_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goHome = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(goHome);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent goHome = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(goHome);
        finish();
    }
}