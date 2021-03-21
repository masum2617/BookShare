package com.example.bookshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bookshare.R;
import com.example.bookshare.model.Cart;
import com.example.bookshare.model.GlobalData;
import com.google.android.material.badge.BadgeDrawable;

import java.util.ArrayList;
import java.util.List;

public class ListAdapterCheckoutBookList extends BaseAdapter {

    private double cartListTotalPrice;
    private BadgeDrawable badgeDrawable;
    private Context context;
    private LayoutInflater inflater;
    private List<Cart> cartList = new ArrayList<Cart>();
    private ListView cartBookList;

    private String url = GlobalData.url;
    private String action = "?action=";
    private String apiName = "";


    public ListAdapterCheckoutBookList(List<Cart> cartList, Context context) {
        this.cartList.clear();
        this.cartList = cartList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Object getItem(int position) {
        return cartList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Cart cart = cartList.get(position);
        //For Top 10 books per week gridview
        View listViewCheckoutBooks = convertView;
        if(convertView==null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listViewCheckoutBooks = inflater.inflate(R.layout.row_item_cart_book, null);
        }

        //set badge over cart menu in bottom
//        BottomNavigationView BottomNavView = (BottomNavigationView) listViewCategoryBooks.findViewById(R.id.bottom_nav_view);
//        badgeDrawable = BottomNavView.getOrCreateBadge(R.id.navigation_mycart);
//        badgeDrawable.setBackgroundColor(Color.RED);
//        badgeDrawable.setBadgeTextColor(Color.WHITE);
//        badgeDrawable.setNumber(GlobalData.getInstance().cartValue);
//        if(GlobalData.getInstance().cartValue==0){
//            badgeDrawable.setVisible(false);
//        }else if(GlobalData.getInstance().cartValue>0){
//            badgeDrawable.setVisible(true);
//        }

        ImageView bookImage = (ImageView) listViewCheckoutBooks.findViewById(R.id.book_image);
        TextView bookTitle = (TextView) listViewCheckoutBooks.findViewById(R.id.book_title);
        TextView bookPrice = (TextView) listViewCheckoutBooks.findViewById(R.id.book_price);
        final TextView subTotal = (TextView) listViewCheckoutBooks.findViewById(R.id.cartItemSubTotal);
        final LinearLayout afterCartSection = (LinearLayout) listViewCheckoutBooks.findViewById(R.id.afterCart_section);
        final ProgressBar pbCart = (ProgressBar) listViewCheckoutBooks.findViewById(R.id.pbCart);
        final ImageButton btnCartMinus = (ImageButton) listViewCheckoutBooks.findViewById(R.id.btn_cart_minus);
        ImageButton btnCartPlus = (ImageButton) listViewCheckoutBooks.findViewById(R.id.btn_cart_plus);
        final TextView mCartQuantity = (TextView) listViewCheckoutBooks.findViewById(R.id.txtCartQuantity);
        final LinearLayout btnRemoveCartItem = (LinearLayout) listViewCheckoutBooks.findViewById(R.id.btnRemoveCartItem);
        TextView txtCartItemQty = (TextView) listViewCheckoutBooks.findViewById(R.id.txtCartItemQty);

        bookTitle.setText(cart.getBookTitle());
        bookPrice.setText(cart.getBookPrice()+"");
        subTotal.setText(cart.getTotalPrice()+"");
        mCartQuantity.setText(cart.getQuantity()+"");
        btnCartPlus.setVisibility(View.INVISIBLE);
        btnCartMinus.setVisibility(View.GONE);
        txtCartItemQty.setVisibility(View.VISIBLE);
        btnRemoveCartItem.setVisibility(View.GONE);

        //gridImageTop10PerWeek.setImageResource(R.drawable.ic_list_black_48dp);
        //gridTextTop10PerWeek.setText(bookTitles[position]);

        return listViewCheckoutBooks;
    }
}
