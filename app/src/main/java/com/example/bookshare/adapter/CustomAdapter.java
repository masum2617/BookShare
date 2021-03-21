package com.example.bookshare.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private List<Book> books;
    private RecyclerViewClickListener listener;

    private String url = GlobalData.url;
    private String action = "?action=";
    private String apiName = "";

    private BadgeDrawable badgeDrawable;

    public CustomAdapter(Context context, List<Book> books, RecyclerViewClickListener listener, BadgeDrawable badgeDrawable) {
        this.context = context;
        this.books = books;
        this.listener = listener;
        this.badgeDrawable = badgeDrawable;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mName, mPrice, mCartAdded, mCartQuantity, mLabel;
        ImageView mImage;
        LinearLayout btnBookAddToCart, afterCart_section;
        BottomNavigationView bnv;
        ProgressBar pbCart;
        ImageButton btnCartMinus, btnCartPlus;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.grid_bookName);
            mPrice = (TextView) itemView.findViewById(R.id.grid_bookPrice);
            mCartAdded = (TextView) itemView.findViewById(R.id.grid_cartAddedText);
            mImage = (ImageView) itemView.findViewById(R.id.grid_image);
            btnBookAddToCart = (LinearLayout) itemView.findViewById(R.id.btnBookAddToCart);
            afterCart_section = (LinearLayout) itemView.findViewById(R.id.afterCart_section);
            bnv = (BottomNavigationView) itemView.findViewById(R.id.bottom_nav_view);
            pbCart = (ProgressBar) itemView.findViewById(R.id.grid_pbCart);
            btnCartMinus = (ImageButton) itemView.findViewById(R.id.btn_cart_minus);
            btnCartPlus = (ImageButton) itemView.findViewById(R.id.btn_cart_plus);
            mCartQuantity = (TextView) itemView.findViewById(R.id.txtCartQuantity);
            mLabel = (TextView) itemView.findViewById(R.id.grid_label);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v,getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_item_book,parent,false);

        //BottomNavigationView BottomNavView = (BottomNavigationView) parent.findViewById(R.id.bottom_nav_view);
        //badgeDrawable = BottomNavView.getOrCreateBadge(R.id.navigation_mycart);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final Book book = books.get(position);
        holder.pbCart.setVisibility(View.GONE);
        holder.mCartAdded.setVisibility(View.GONE);
        holder.btnBookAddToCart.setVisibility(View.VISIBLE);
        holder.afterCart_section.setVisibility(View.GONE);
        String bookName = book.getTitle();
        if(bookName.length()>12){
            bookName = bookName.substring(0,11);
            bookName = bookName + "...";
        }
        holder.mName.setText(bookName);
        holder.mPrice.setText("৳ "+book.getPrice());
        holder.mImage.setImageResource(book.getImageTemp());

        if(book.purpose==1){
            holder.mLabel.setText("For Sell");
            holder.mLabel.setBackgroundColor(Color.RED);
        }else if(book.purpose==2){
            holder.mLabel.setText("For Rent");
            holder.mLabel.setBackgroundColor(Color.BLUE);
        }
        if(book.cartValue>0){
            holder.btnBookAddToCart.setVisibility(View.GONE);
            holder.mCartAdded.setVisibility(View.VISIBLE);
        }

//        if(book_icon.cartValue>0){
//            holder.btnBookAddToCart.setVisibility(View.GONE);
//            holder.afterCart_section.setVisibility(View.VISIBLE);
//            holder.mCartQuantity.setText(book_icon.getCartValue()+"");
//            if(book_icon.getCartValue()<=1){
//                holder.btnCartMinus.setVisibility(View.INVISIBLE);
//            }else{
//                holder.btnCartMinus.setVisibility(View.VISIBLE);
//            }
//        }

        holder.btnBookAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.pbCart.setVisibility(View.VISIBLE);

                //Add to cart in database table process start
                apiName = "api_insertData.php";
                String actionN = action+"addToCart";
                String urlN = url+apiName+actionN;
                //System.out.println("----------URL= "+urlN);
                boolean status;

                StringRequest request = new StringRequest(Request.Method.POST, urlN, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //System.out.println("************URL= "+urlN);
                        String [] s= response.split("-");
                        if(s[0].toString().equals("yes")){
                            holder.pbCart.setVisibility(View.GONE);
                            Toast.makeText(context,"Book added to cart successfully",Toast.LENGTH_SHORT).show();
                            holder.btnBookAddToCart.setVisibility(View.GONE);
                            //holder.afterCart_section.setVisibility(View.VISIBLE);
                            holder.mCartAdded.setVisibility(View.VISIBLE);
                            if(book.getCartValue()<=1){
                                holder.btnCartMinus.setVisibility(View.INVISIBLE);
                            }
                            holder.mCartQuantity.setText(book.getMinimumQuantity()+"");
                            GlobalData.getInstance().cartValue++;
                            GlobalData.getInstance().totalCartPrice += book.getTotalPriceForCart();
                            GlobalData.getInstance().lastCartId = Integer.parseInt(s[1]);
                            badgeDrawable.setNumber(GlobalData.getInstance().cartValue);
                            book.cartValue=book.minimumQuantity;
                            GlobalData.getInstance().cartList.add(new Cart(GlobalData.getInstance().lastCartId, book.id, book.title, book.price, book.picture, GlobalData.getInstance().userId, book.cartValue, book.getTotalPriceForCart()));
                        } else if(response.toString().equals("tokenFail")){
                            holder.pbCart.setVisibility(View.GONE);
                            Toast.makeText(context,"Wrong token, try again",Toast.LENGTH_SHORT).show();
                        } else{
                            holder.pbCart.setVisibility(View.GONE);
                            Toast.makeText(context,"Failed to add in cart-"+GlobalData.getInstance().userId+"-"+book.getId(),Toast.LENGTH_SHORT).show();
                            //System.out.println(">>>>>>>>>>>>>>>>>>>URL= "+urlN);
                            //Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        holder.pbCart.setVisibility(View.GONE);
                        Toast.makeText(context,"Error occured!",Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        params.put("myToken", "786");
                        params.put("userId",GlobalData.getInstance().userId+"");
                        params.put("bookId",book.getId()+"");
                        params.put("quantity",book.getMinimumQuantity()+"");
                        params.put("totalPrice",book.getTotalPriceForCart()+"");

                        return params;
                    }
                };


                //System.out.println("edUsername= "+GlobalData.getInstance().username);
                //System.out.println(GlobalData.getInstance());
                GlobalData.getInstance().addToRequestQueue(request);

                //Add to cart in database table process start

            }
        });



        holder.btnCartPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.pbCart.setVisibility(View.VISIBLE);

                //increase cart value in database table process start
                apiName = "api_updateData.php";
                String actionN = action+"updateCart";
                String urlN = url+apiName+actionN;

                System.out.println(">>>>>>>>>>>>>>>>>>>URL= "+urlN);

                StringRequest request = new StringRequest(Request.Method.POST, urlN, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.toString().equals("yes")){
                            book.cartValue++;
                            holder.mCartQuantity.setText(book.getCartValue()+"");
                            holder.pbCart.setVisibility(View.GONE);
                            holder.btnCartMinus.setVisibility(View.VISIBLE);
                            for(Cart c : GlobalData.getInstance().cartList){
                                if(book.id == c.bookId){
                                     c.quantity = book.cartValue;
                                     c.totalPrice += book.price;
                                }
                            }
                            GlobalData.getInstance().totalCartPrice += book.getPrice();
                            Toast.makeText(context,"Book cart quantity increased",Toast.LENGTH_SHORT).show();
                        } else if(response.toString().equals("tokenFail")){
                            holder.pbCart.setVisibility(View.GONE);
                            Toast.makeText(context,"Wrong token, try again",Toast.LENGTH_SHORT).show();
                        } else{
                            holder.pbCart.setVisibility(View.GONE);
                            Toast.makeText(context,"Failed to increase cart-"+GlobalData.getInstance().userId+"-"+book.getId(),Toast.LENGTH_SHORT).show();
                            //Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                            //System.out.println(">>>>>>>>>>>>>>>>>>>URL= "+urlN);
                            //Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        holder.pbCart.setVisibility(View.GONE);
                        Toast.makeText(context,"Error occured!",Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        params.put("myToken", "786");
                        params.put("userId",GlobalData.getInstance().userId+"");
                        params.put("bookId",book.getId()+"");
                        params.put("quantity",book.getCartValue()+"");
                        params.put("totalPrice",book.getUpdateTotalPriceForCart()+"");

                        return params;
                    }
                };

                GlobalData.getInstance().addToRequestQueue(request);
            }
        });



        holder.btnCartMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.pbCart.setVisibility(View.VISIBLE);

                //increase cart value in database table process start
                apiName = "api_updateData.php";
                String actionN = action+"updateCart";
                String urlN = url+apiName+actionN;

                //System.out.println(">>>>>>>>>>>>>>>>>>>URL= "+urlN);

                StringRequest request = new StringRequest(Request.Method.POST, urlN, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.toString().equals("yes")){
                            book.cartValue--;
                            holder.mCartQuantity.setText(book.getCartValue()+"");
                            holder.pbCart.setVisibility(View.GONE);
                            if(book.cartValue<=1){
                                holder.btnCartMinus.setVisibility(View.INVISIBLE);
                            }
                            for(Cart c : GlobalData.getInstance().cartList){
                                if(book.id == c.bookId){
                                    c.quantity = book.cartValue;
                                    c.totalPrice -= book.price;
                                }
                            }
                            GlobalData.getInstance().totalCartPrice -= book.getPrice();
                            //Toast.makeText(context,"Book-"+book_icon.id+" cart quantity "+book_icon.cartValue,Toast.LENGTH_SHORT).show();
                        } else if(response.toString().equals("tokenFail")){
                            holder.pbCart.setVisibility(View.GONE);
                            Toast.makeText(context,"Wrong token, try again",Toast.LENGTH_SHORT).show();
                        } else{
                            holder.pbCart.setVisibility(View.GONE);
                            Toast.makeText(context,"Failed to decrease cart-"+GlobalData.getInstance().userId+"-"+book.getId(),Toast.LENGTH_SHORT).show();
                            //Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                            //System.out.println(">>>>>>>>>>>>>>>>>>>URL= "+urlN);
                            //Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        holder.pbCart.setVisibility(View.GONE);
                        Toast.makeText(context,"Error occured!",Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        params.put("myToken", "786");
                        params.put("userId",GlobalData.getInstance().userId+"");
                        params.put("bookId",book.getId()+"");
                        params.put("quantity",book.getCartValue()+"");
                        params.put("totalPrice",book.getUpdateTotalPriceForCart()+"");

                        return params;
                    }
                };

                GlobalData.getInstance().addToRequestQueue(request);
            }
        });

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public interface RecyclerViewClickListener{
        void onClick(View v, int position);
    }


}
