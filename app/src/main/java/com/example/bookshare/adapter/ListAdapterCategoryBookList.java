package com.example.bookshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bookshare.R;
import com.example.bookshare.model.Book;
import com.example.bookshare.model.Cart;
import com.example.bookshare.model.GlobalData;
import com.google.android.material.badge.BadgeDrawable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListAdapterCategoryBookList extends BaseAdapter {

    private int bookImages[];
    private String[] bookTitles;
    private Context context;
    private LayoutInflater inflater;
    private List<Book> bookList;
    private TextView listTxtSentRequest;

    private String url = GlobalData.url;
    private String action = "?action=";
    private String apiName = "";

    private BadgeDrawable badgeDrawable;

    public ListAdapterCategoryBookList(List<Book> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Book book = bookList.get(position);
        //For Top 10 books per week gridview
        View listViewCategoryBooks = convertView;
        if(convertView==null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listViewCategoryBooks = inflater.inflate(R.layout.row_item_category_book, null);
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

        ImageView bookImage = (ImageView) listViewCategoryBooks.findViewById(R.id.book_image);
        TextView bookTitle = (TextView) listViewCategoryBooks.findViewById(R.id.book_title);
        TextView bookPrice = (TextView) listViewCategoryBooks.findViewById(R.id.book_price);
        TextView btnAddToCart = (TextView) listViewCategoryBooks.findViewById(R.id.btnBookAddToCart);
        listTxtSentRequest = (TextView) listViewCategoryBooks.findViewById(R.id.list_sent_request);

        final LinearLayout addToCartSection = (LinearLayout) listViewCategoryBooks.findViewById(R.id.addToCart_section);
        final LinearLayout afterCartSection = (LinearLayout) listViewCategoryBooks.findViewById(R.id.afterCart_section);
        final ProgressBar pbCart = (ProgressBar) listViewCategoryBooks.findViewById(R.id.pbCart);
//        final ImageButton btnCartMinus = (ImageButton) listViewCategoryBooks.findViewById(R.id.btn_cart_minus);
//        ImageButton btnCartPlus = (ImageButton) listViewCategoryBooks.findViewById(R.id.btn_cart_plus);
//        final TextView mCartQuantity = (TextView) listViewCategoryBooks.findViewById(R.id.txtCartQuantity);

        bookTitle.setText(book.getTitle());
        bookPrice.setText(book.getPrice()+"");

//        if(book.cartValue>0){
//            addToCartSection.setVisibility(View.GONE);
//            afterCartSection.setVisibility(View.VISIBLE);
//            mCartQuantity.setText(book.getCartValue()+"");
//            if(book.getCartValue()<=1){
//                btnCartMinus.setVisibility(View.INVISIBLE);
//            }else{
//                btnCartMinus.setVisibility(View.VISIBLE);
//            }
//        }

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbCart.setVisibility(View.VISIBLE);

                //Add to cart in database table process start
                apiName = "api_insertData.php";
                String actionN = action+"addToCart";
                String urlN = url+apiName+actionN;
                //System.out.println("----------URL= "+urlN);
                boolean status;

                addToCartSection.setVisibility(View.GONE);
                afterCartSection.setVisibility(View.VISIBLE);
                pbCart.setVisibility(View.GONE);

//                StringRequest request = new StringRequest(Request.Method.POST, urlN, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        //System.out.println("************URL= "+urlN);
//                        String [] s= response.split("-");
//                        if(s[0].toString().equals("yes")){
//                            pbCart.setVisibility(View.GONE);
//                            Toast.makeText(context,"Book added to cart successfully",Toast.LENGTH_SHORT).show();
//                            addToCartSection.setVisibility(View.GONE);
//                            afterCartSection.setVisibility(View.VISIBLE);
//                            if(book.getCartValue()<=1){
//                                btnCartMinus.setVisibility(View.INVISIBLE);
//                            }
//                            mCartQuantity.setText(book.getMinimumQuantity()+"");
//                            GlobalData.getInstance().cartValue++;
//                            GlobalData.getInstance().totalCartPrice += book.getTotalPriceForCart();
//                            GlobalData.getInstance().lastCartId = Integer.parseInt(s[1]);
//                            //badgeDrawable.setNumber(GlobalData.getInstance().cartValue);
//                            book.cartValue=book.minimumQuantity;
//                            GlobalData.getInstance().cartList.add(new Cart(GlobalData.getInstance().lastCartId, book.id, book.title, book.price, book.picture, GlobalData.getInstance().userId, book.cartValue, book.getTotalPriceForCart()));
//                        } else if(response.toString().equals("tokenFail")){
//                            pbCart.setVisibility(View.GONE);
//                            Toast.makeText(context,"Wrong token, try again",Toast.LENGTH_SHORT).show();
//                        } else{
//                            pbCart.setVisibility(View.GONE);
//                            Toast.makeText(context,"Failed to add in cart-"+GlobalData.getInstance().userId+"-"+book.getId(),Toast.LENGTH_SHORT).show();
//                            //System.out.println(">>>>>>>>>>>>>>>>>>>URL= "+urlN);
//                            //Toast.makeText(context,response,Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        pbCart.setVisibility(View.GONE);
//                        Toast.makeText(context,"Error occured!",Toast.LENGTH_SHORT).show();
//                    }
//                }){
//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("Content-Type", "application/json");
//                        params.put("myToken", "786");
//                        params.put("userId",GlobalData.getInstance().userId+"");
//                        params.put("bookId",book.getId()+"");
//                        params.put("quantity",book.getMinimumQuantity()+"");
//                        params.put("totalPrice",book.getTotalPriceForCart()+"");
//
//                        return params;
//                    }
//                };


                //System.out.println("edUsername= "+GlobalData.getInstance().username);
                //System.out.println(GlobalData.getInstance());
                //GlobalData.getInstance().addToRequestQueue(request);
            }
        });


//        btnCartPlus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                book.cartValue++;
//                pbCart.setVisibility(View.VISIBLE);
//
//                //increase cart value in database table process start
//                apiName = "api_updateData.php";
//                String actionN = action+"updateCart";
//                String urlN = url+apiName+actionN;
//
//                System.out.println(">>>>>>>>>>>>>>>>>>>URL= "+urlN);
//
//                StringRequest request = new StringRequest(Request.Method.POST, urlN, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if(response.toString().equals("yes")){
//                            mCartQuantity.setText(book.getCartValue()+"");
//                            pbCart.setVisibility(View.GONE);
//                            btnCartMinus.setVisibility(View.VISIBLE);
//                            for(Cart c : GlobalData.getInstance().cartList){
//                                if(book.id == c.bookId){
//                                    c.quantity = book.cartValue;
//                                }
//                            }
//                            GlobalData.getInstance().totalCartPrice += book.getPrice();
//                            //Toast.makeText(context,"Book-"+book_icon.id+" cart quantity "+book_icon.cartValue,Toast.LENGTH_SHORT).show();
//                        } else if(response.toString().equals("tokenFail")){
//                            pbCart.setVisibility(View.GONE);
//                            Toast.makeText(context,"Wrong token, try again",Toast.LENGTH_SHORT).show();
//                        } else{
//                            pbCart.setVisibility(View.GONE);
//                            Toast.makeText(context,"Failed to increase cart-"+GlobalData.getInstance().userId+"-"+book.getId(),Toast.LENGTH_SHORT).show();
//                            //Toast.makeText(context,response,Toast.LENGTH_LONG).show();
//                            //System.out.println(">>>>>>>>>>>>>>>>>>>URL= "+urlN);
//                            //Toast.makeText(context,response,Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        pbCart.setVisibility(View.GONE);
//                        Toast.makeText(context,"Error occured!",Toast.LENGTH_SHORT).show();
//                    }
//                }){
//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("Content-Type", "application/json");
//                        params.put("myToken", "786");
//                        params.put("userId",GlobalData.getInstance().userId+"");
//                        params.put("bookId",book.getId()+"");
//                        params.put("quantity",book.getCartValue()+"");
//                        params.put("totalPrice",book.getUpdateTotalPriceForCart()+"");
//
//                        return params;
//                    }
//                };
//
//                GlobalData.getInstance().addToRequestQueue(request);
//            }
//        });
//
//
//
//        btnCartMinus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                book.cartValue--;
//                pbCart.setVisibility(View.VISIBLE);
//
//                //increase cart value in database table process start
//                apiName = "api_updateData.php";
//                String actionN = action+"updateCart";
//                String urlN = url+apiName+actionN;
//
//                System.out.println(">>>>>>>>>>>>>>>>>>>URL= "+urlN);
//
//                StringRequest request = new StringRequest(Request.Method.POST, urlN, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        if(response.toString().equals("yes")){
//                            mCartQuantity.setText(book.getCartValue()+"");
//                            pbCart.setVisibility(View.GONE);
//                            if(book.cartValue<=1){
//                                btnCartMinus.setVisibility(View.INVISIBLE);
//                            }
//                            for(Cart c : GlobalData.getInstance().cartList){
//                                if(book.id == c.bookId){
//                                    c.quantity = book.cartValue;
//                                }
//                            }
//                            GlobalData.getInstance().totalCartPrice -= book.getPrice();
//                            //Toast.makeText(context,"Book-"+book_icon.id+" cart quantity "+book_icon.cartValue,Toast.LENGTH_SHORT).show();
//                        } else if(response.toString().equals("tokenFail")){
//                            pbCart.setVisibility(View.GONE);
//                            Toast.makeText(context,"Wrong token, try again",Toast.LENGTH_SHORT).show();
//                        } else{
//                            pbCart.setVisibility(View.GONE);
//                            Toast.makeText(context,"Failed to decrease cart-"+GlobalData.getInstance().userId+"-"+book.getId(),Toast.LENGTH_SHORT).show();
//                            //Toast.makeText(context,response,Toast.LENGTH_LONG).show();
//                            //System.out.println(">>>>>>>>>>>>>>>>>>>URL= "+urlN);
//                            //Toast.makeText(context,response,Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        pbCart.setVisibility(View.GONE);
//                        Toast.makeText(context,"Error occured!",Toast.LENGTH_SHORT).show();
//                    }
//                }){
//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("Content-Type", "application/json");
//                        params.put("myToken", "786");
//                        params.put("userId",GlobalData.getInstance().userId+"");
//                        params.put("bookId",book.getId()+"");
//                        params.put("quantity",book.getCartValue()+"");
//                        params.put("totalPrice",book.getUpdateTotalPriceForCart()+"");
//
//                        return params;
//                    }
//                };
//
//                GlobalData.getInstance().addToRequestQueue(request);
//            }
//        });

        //gridImageTop10PerWeek.setImageResource(R.drawable.ic_list_black_48dp);
        //gridTextTop10PerWeek.setText(bookTitles[position]);

        return listViewCategoryBooks;
    }
}
