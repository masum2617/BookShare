package com.example.bookshare.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookshare.CategoryActivity;
import com.example.bookshare.R;
import com.example.bookshare.adapter.CustomAdapter;
import com.example.bookshare.adapter.GridAdapterCat;
import com.example.bookshare.model.Book;
import com.example.bookshare.model.Cart;
import com.example.bookshare.model.Category;
import com.example.bookshare.model.GlobalData;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ViewFlipper v_flipper;
    private GridView gridView_categories, gridview_top10_per_week, gridview_top10_per_month, gridview_top10_per_year;
    private TextView viewAllPerWeek, viewAllPerMonth, viewAllPerYear;
    private ArrayList<Integer> images = new ArrayList<Integer>();
    private ProgressBar pbCategories;
    private LinearLayout homeBottomPart;
    private CustomAdapter.RecyclerViewClickListener rListener;
    private BadgeDrawable badgeDrawable;



    private int[] catImages = {
            R.drawable.owalium_murshida, R.drawable.madina_sharif, R.drawable.makkah_madina,
            R.drawable.quran_sharif, R.drawable.masjid, R.drawable.ic_list_black_48dp,
            R.drawable.ic_list_black_48dp, R.drawable.sunnat, R.drawable.ic_list_black_48dp,
            R.drawable.ic_list_black_48dp, R.drawable.ic_list_black_48dp, R.drawable.ic_list_black_48dp
    };



    private int[] bookImages = {
            R.drawable.rabi, R.drawable.ummu_rawsulina,R.drawable.zakat_cover, R.drawable.milad_shareef
    };

    private String url = GlobalData.url;
    private String action = "?action=";
    private String apiName = "";

    private ArrayList<Category> categoryList = new ArrayList<Category>();


    private RecyclerView listRecent,mList2, listSell, listRent;
    private List<Book> bookListRecent, bookListSell, bookListRent;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);
        v_flipper = root.findViewById(R.id.viewFlipper);

        images.add(R.drawable.slider1);
        images.add(R.drawable.slider2);
        images.add(R.drawable.slider3);
        //images.add(R.drawable.slider4);

        //set badge over cart menu in bottom
        BottomNavigationView BottomNavView = (BottomNavigationView) getActivity().findViewById(R.id.bottom_nav_view);
        badgeDrawable = BottomNavView.getOrCreateBadge(R.id.navigation_map);
        badgeDrawable.setBackgroundColor(Color.RED);
        badgeDrawable.setBadgeTextColor(Color.WHITE);
        badgeDrawable.setNumber(GlobalData.getInstance().cartValue);
        if(GlobalData.getInstance().cartValue==0){
            badgeDrawable.setVisible(false);
        }else if(GlobalData.getInstance().cartValue>0){
            badgeDrawable.setVisible(true);
        }


        for(Integer image:images){
            homeViewModel.flipperImages(image,v_flipper,getContext());
        }

//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                //textView.setText(s);
//            }
//        });

        //set dynamic margin on bottom
        homeBottomPart = (LinearLayout) root.findViewById(R.id.homeBottomPart);
        homeBottomPart.setMinimumHeight(getBoottomNavHeight(root));


        pbCategories = root.findViewById(R.id.pb_categories);

        //Gridview for book_icon Categories
        gridView_categories = (GridView) root.findViewById(R.id.gridview_categories);
        //get categories
        //loadCategoryData();
        showCategories();



        GlobalData.getInstance().totalCartPrice = 0;
        GlobalData.getInstance().tempTotalCartPrice = 0;
        for(Cart c : GlobalData.getInstance().cartList){
            GlobalData.getInstance().totalCartPrice += c.totalPrice;
        }
        GlobalData.getInstance().tempTotalCartPrice = GlobalData.getInstance().totalCartPrice;

        bookListSell = new ArrayList<Book>();
        bookListRent = new ArrayList<Book>();
        for(Book b : GlobalData.getInstance().recentBooks){
            if(b.purpose == 1){
                bookListSell.add(b);
            }else if(b.purpose == 2){
                bookListRent.add(b);
            }
        }


        //Recents books list
        listRecent = root.findViewById(R.id.list_recent);
        //mList2 = root.findViewById(R.id.list2);

        //set recyclerview custom listener for recent books
        setRecentOnClickListener();

        bookListRecent = new ArrayList<Book>();
        //bookListRecent.add(new Book(1,"আন নূরুর রবি‘য়াহ সাইয়্যিদাতুনা হযরত যাহরা...",170, R.drawable.rabi, 1));
        //bookListRecent.add(new Book(2,"kitab 2",195, R.drawable.zakat_cover, 1));
        //bookListRecent.add(new Book(3,"kitab 3",210, R.drawable.ummu_rawsulina, 1));
        //bookListRecent.add(new Book(4,"kitab 4",105, R.drawable.milad_shareef, 1));


        LinearLayoutManager managerRecent = new LinearLayoutManager(getContext());
        managerRecent.setOrientation(LinearLayoutManager.HORIZONTAL);
        listRecent.setLayoutManager(managerRecent);
        CustomAdapter adapterRecent = new CustomAdapter(getContext(),GlobalData.getInstance().recentBooks, rListener, badgeDrawable);
        listRecent.setAdapter(adapterRecent);


        //Top selling book list
        listSell = root.findViewById(R.id.list_sell);

        //set recyclerview custom listener for recent books
        setRecentOnClickListener();

        managerRecent = new LinearLayoutManager(getContext());
        managerRecent.setOrientation(LinearLayoutManager.HORIZONTAL);
        listSell.setLayoutManager(managerRecent);
        CustomAdapter adapterSell = new CustomAdapter(getContext(),bookListSell, rListener, badgeDrawable);
        listSell.setAdapter(adapterSell);



        //Top rent book list
        listRent = root.findViewById(R.id.list_rent);
        //set recyclerview custom listener for recent books
        setRecentOnClickListener();

        managerRecent = new LinearLayoutManager(getContext());
        managerRecent.setOrientation(LinearLayoutManager.HORIZONTAL);
        listRent.setLayoutManager(managerRecent);
        CustomAdapter adapterRent = new CustomAdapter(getContext(),bookListRent, rListener, badgeDrawable);
        listRent.setAdapter(adapterRent);



        return root;
    }

    private void setRecentOnClickListener() {
        rListener = new CustomAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Toast.makeText(getContext(), GlobalData.getInstance().recentBooks.get(position).getTitle(),Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void showCategories(){
        //GridAdapterCat adapter_cat = new GridAdapterCat(getContext(),catNames, catImages);
        GridAdapterCat adapter_cat = new GridAdapterCat(getContext(), GlobalData.getInstance().categoryList, catImages);
        gridView_categories.setAdapter(adapter_cat);
        pbCategories.setVisibility(View.GONE);
        gridView_categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),"This is "+GlobalData.getInstance().categoryList.get(position).categoryName,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), CategoryActivity.class);
                intent.putExtra("title", GlobalData.getInstance().categoryList.get(position).categoryName);
                intent.putExtra("id", GlobalData.getInstance().categoryList.get(position).categoryId);
                //startActivity(intent);
                //getActivity().finish();
            }
        });
    }

    private int getBoottomNavHeight(View root){
        View view = getActivity().findViewById(R.id.bottom_nav_view);
        return view.getHeight();


//        Resources resources = getContext().getResources();
//        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            return resources.getDimensionPixelSize(resourceId);
//        }
//        return 0;
    }
}