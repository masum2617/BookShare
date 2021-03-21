package com.example.bookshare.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bookshare.R;
import com.example.bookshare.SingleBookActivity;
import com.example.bookshare.model.Book;
import com.example.bookshare.model.Cart;
import com.example.bookshare.model.GlobalData;
import com.google.android.material.badge.BadgeDrawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListAdapterCartBookList extends BaseAdapter {

    private final TextView tvTotalCartPrice;
    private double cartListTotalPrice=0.00;
    private BadgeDrawable badgeDrawable;
    private int bookImages[];
    private String[] bookTitles;
    private Context context;
    private LayoutInflater inflater;
    private List<Cart> cartList = new ArrayList<Cart>();
    private ListView cartBookList;

    private Double tempTotal = 0.00;

    private String url = GlobalData.url;
    private String action = "?action=";
    private String apiName = "";


    public ListAdapterCartBookList(List<Cart> cartList, Context context, ListView cartBookList, BadgeDrawable badgeDrawable, TextView tvTotalCartPrice, double cartListTotalPrice) {
        this.cartList = cartList;
        this.context = context;
        this.cartBookList = cartBookList;
        this.badgeDrawable = badgeDrawable;
        this.tvTotalCartPrice = tvTotalCartPrice;
        this.cartListTotalPrice = cartListTotalPrice;
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
        tempTotal += cart.totalPrice;
        tvTotalCartPrice.setText(GlobalData.getInstance().tempTotalCartPrice+"");
        //For Top 10 books per week gridview
        View listViewCartBooks = convertView;
        if(convertView==null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listViewCartBooks = inflater.inflate(R.layout.row_item_cart_book, null);
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

        ImageView bookImage = (ImageView) listViewCartBooks.findViewById(R.id.book_image);
        TextView bookTitle = (TextView) listViewCartBooks.findViewById(R.id.book_title);
        TextView bookPrice = (TextView) listViewCartBooks.findViewById(R.id.book_price);
        final TextView subTotal = (TextView) listViewCartBooks.findViewById(R.id.cartItemSubTotal);
        final LinearLayout afterCartSection = (LinearLayout) listViewCartBooks.findViewById(R.id.afterCart_section);
        final ProgressBar pbCart = (ProgressBar) listViewCartBooks.findViewById(R.id.pbCart);
        final ImageButton btnCartMinus = (ImageButton) listViewCartBooks.findViewById(R.id.btn_cart_minus);
        ImageButton btnCartPlus = (ImageButton) listViewCartBooks.findViewById(R.id.btn_cart_plus);
        final TextView mCartQuantity = (TextView) listViewCartBooks.findViewById(R.id.txtCartQuantity);
        final LinearLayout btnRemoveCartItem = (LinearLayout) listViewCartBooks.findViewById(R.id.btnRemoveCartItem);
        final ProgressBar pbCartItemRemove = (ProgressBar) listViewCartBooks.findViewById(R.id.pbCartItemRemove);

        pbCartItemRemove.setVisibility(View.GONE);

        bookTitle.setText(cart.getBookTitle());
        bookPrice.setText(cart.getBookPrice()+"");
        subTotal.setText(cart.getTotalPrice()+"");
        mCartQuantity.setText(cart.getQuantity()+"");

        if(cart.getQuantity()<=1){
            btnCartMinus.setVisibility(View.INVISIBLE);
        }else{
            btnCartMinus.setVisibility(View.VISIBLE);
        }


        btnCartPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.quantity++;
                cart.totalPrice += cart.bookPrice;
                btnCartMinus.setVisibility(View.VISIBLE);
                mCartQuantity.setText(cart.getQuantity()+"");
                GlobalData.getInstance().tempTotalCartPrice += cart.bookPrice;
                tvTotalCartPrice.setText(GlobalData.getInstance().tempTotalCartPrice+"");
                subTotal.setText(cart.getTotalPrice()+"");
            }
        });


        btnCartMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.quantity--;
                cart.totalPrice -= cart.bookPrice;
                if(cart.quantity<=1){
                    btnCartMinus.setVisibility(View.INVISIBLE);
                }else {
                    btnCartMinus.setVisibility(View.VISIBLE);
                }
                mCartQuantity.setText(cart.getQuantity()+"");
                GlobalData.getInstance().tempTotalCartPrice -= cart.bookPrice;
                tvTotalCartPrice.setText(GlobalData.getInstance().tempTotalCartPrice+"");
                subTotal.setText(cart.getTotalPrice()+"");
            }
        });

        btnRemoveCartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //book_icon.cartValue++;
                //holder.pbCart.setVisibility(View.VISIBLE);
                pbCartItemRemove.setVisibility(View.VISIBLE);
                //increase cart value in database table process start
                apiName = "api_deleteData.php";
                String actionN = action+"deleteCartItem";
                String urlN = url+apiName+actionN;

                //System.out.println(">>>>>>>>>>>>>>>>>>>URL= "+urlN);

                StringRequest request = new StringRequest(Request.Method.POST, urlN, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.toString().equals("yes")){
                            GlobalData.getInstance().cartList.remove(position);
                            GlobalData.getInstance().cartValue--;
                            GlobalData.getInstance().totalCartPrice -= cart.totalPrice;
                            System.out.println("--------------------------"+GlobalData.getInstance().totalCartPrice);
                            GlobalData.getInstance().tempTotalCartPrice -= cart.totalPrice;
                            cartList.remove(position);
                            badgeDrawable.setNumber(GlobalData.getInstance().cartValue);
                            tvTotalCartPrice.setText(GlobalData.getInstance().tempTotalCartPrice+"");
                            //cartList.remove(position);

                            for(Book b : GlobalData.getInstance().recentBooks){
                                if(b.id == cart.bookId){
                                    b.cartValue = 0;
                                }
                            }

                            ListAdapterCartBookList adapter = new ListAdapterCartBookList(cartList,context,cartBookList,badgeDrawable, tvTotalCartPrice,cartListTotalPrice);
                            cartBookList.setAdapter(adapter);
                            cartBookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Cart cart = cartList.get(position);
                                    Toast.makeText(context,"Title: "+cartList.get(position).getBookTitle(),Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, SingleBookActivity.class);
                                    intent.putExtra("book_title", cart.getBookTitle());
                                    intent.putExtra("book_id",cart.getBookId());
                                    pbCartItemRemove.setVisibility(View.GONE);
                                    context.startActivity(intent);
                                }
                            });
                            //Toast.makeText(context,"Book-"+book_icon.id+" cart quantity "+book_icon.cartValue,Toast.LENGTH_SHORT).show();
                        } else if(response.toString().equals("tokenFail")){
                            Toast.makeText(context,"Wrong token, try again",Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(context,"Failed to delete cart item",Toast.LENGTH_SHORT).show();
                            //Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                            //System.out.println(">>>>>>>>>>>>>>>>>>>URL= "+urlN);
                            //Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Error occured!",Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        params.put("myToken", "786");
                        params.put("userId",GlobalData.getInstance().userId+"");
                        params.put("bookId",cart.getBookId()+"");

                        return params;
                    }
                };

                GlobalData.getInstance().addToRequestQueue(request);
            }
        });

        //gridImageTop10PerWeek.setImageResource(R.drawable.ic_list_black_48dp);
        //gridTextTop10PerWeek.setText(bookTitles[position]);

        return listViewCartBooks;
    }
}
