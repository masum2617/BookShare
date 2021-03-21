package com.example.bookshare.ui.mycart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bookshare.CheckoutActivity;
import com.example.bookshare.R;
import com.example.bookshare.SingleBookActivity;
import com.example.bookshare.adapter.ListAdapterCartBookList;
import com.example.bookshare.model.Cart;
import com.example.bookshare.model.GlobalData;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MycartFragment extends Fragment {

    private MycartViewModel mycartViewModel;
    private LinearLayout checkoutLayout, listLayout;
    private BottomNavigationView bnv;
    private TextView tvTotalCartPrice, foundBooksQuantity, foundMsg;
    private ListView cartBookList;
    private BadgeDrawable badgeDrawable;
    private Button btnCheckOut;

    SharedPreferences preferences;
    public static final String FILE_NAME="preferenceFile";

    private String url = GlobalData.url;
    private String action = "?action=";
    private String apiName = "";

    private ArrayList<Cart> tempCartList = new ArrayList<Cart>();
    private double tempCartTotalPrice = 0.00;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mycartViewModel =
                ViewModelProviders.of(this).get(MycartViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_mycart, container, false);
        //final TextView textView = root.findViewById(R.id.text_notifications);
        mycartViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(getBoottomNavHeight(root)+"");
            }
        });

        //clone cart list from global data
        tempCartList.clear();
        //tempCartList.addAll(GlobalData.getInstance().cartList);
        GlobalData.getInstance().totalCartPrice = 0;
        GlobalData.getInstance().tempTotalCartPrice = 0;
        for(Cart c : GlobalData.getInstance().cartList){
            GlobalData.getInstance().totalCartPrice += c.totalPrice;
            tempCartList.add(new Cart(c.cartId, c.bookId, c.bookTitle, c.bookPrice, c.bookPicture, c.userId, c.quantity, c.totalPrice));
        }
        GlobalData.getInstance().tempTotalCartPrice = GlobalData.getInstance().totalCartPrice;
        //tempCartTotalPrice = GlobalData.getInstance().totalCartPrice;

        GlobalData.getInstance().tempTotalCartPrice = GlobalData.getInstance().totalCartPrice;

        System.out.println("-----------------------"+tempCartTotalPrice);
        System.out.println("-----------------------"+GlobalData.getInstance().totalCartPrice);

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

        tvTotalCartPrice = root.findViewById(R.id.totalCartPrice);
        tvTotalCartPrice.setText(GlobalData.getInstance().tempTotalCartPrice+"");

        checkoutLayout = (LinearLayout) root.findViewById(R.id.checkoutLayout);
        listLayout = (LinearLayout) root.findViewById(R.id.listLayout);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) checkoutLayout.getLayoutParams();
        layoutParams.setMargins( 0 , 0 , 0 , getBoottomNavHeight(root)) ;
        Button okButton= new Button( getContext() ) ;
        okButton.setText( "some text" ) ;
        okButton.setVisibility(View.GONE);
        checkoutLayout.addView(okButton , layoutParams) ;
        checkoutLayout.setMinimumHeight(getBoottomNavHeight(root));


        View view = root.findViewById(R.id.checkoutMainLayout);

        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) listLayout.getLayoutParams();
        int height = getBoottomNavHeight(root);
        layoutParams1.setMargins( 0 , 0 , 0 , height*2) ;
        Button okButton1= new Button( getContext() ) ;
        okButton1.setText( "some text" ) ;
        okButton1.setVisibility(View.GONE);
        listLayout.addView(okButton1 , layoutParams1) ;


        cartBookList = (ListView) root.findViewById(R.id.cart_book_list);
        foundBooksQuantity = (TextView) root.findViewById(R.id.foundBooksQuantity);
        foundMsg = (TextView) root.findViewById(R.id.foundMsg);

        foundBooksQuantity.setText(GlobalData.getInstance().cartList.size()+"");
        foundMsg.setVisibility(View.VISIBLE);

        ListAdapterCartBookList adapter = new ListAdapterCartBookList(tempCartList,getContext(),cartBookList,badgeDrawable, tvTotalCartPrice, tempCartTotalPrice);
        cartBookList.setAdapter(adapter);
        cartBookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cart cart = tempCartList.get(position);
                Toast.makeText(getContext(),"Title: "+tempCartList.get(position).getBookTitle(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), SingleBookActivity.class);
                intent.putExtra("book_title", cart.getBookTitle());
                intent.putExtra("book_id",cart.getBookId());
                startActivity(intent);
            }
        });

        btnCheckOut = (Button) root.findViewById(R.id.btnCheckOut);
        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Cart c : tempCartList){
                    System.out.println("Book: "+c.getBookTitle()+"; Quantity: "+c.getQuantity()+"; Total price: "+c.getTotalPrice());
                }
                System.out.println("-------------------------------------------------");
                for(Cart c : GlobalData.getInstance().cartList){
                    System.out.println("Book: "+c.getBookTitle()+"; Quantity: "+c.getQuantity()+"; Total price: "+c.getTotalPrice());
                }
                //checkOut();
                checkOutNew();
            }
        });

        return root;
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

    private void checkOutNew(){
        Intent i = new Intent(getContext(), CheckoutActivity.class);
        i.putExtra("cartList", tempCartList);
        i.putExtra("totalPrice", GlobalData.getInstance().tempTotalCartPrice);
        startActivity(i);
    }

    private void checkOut(){

        final JSONArray jsonArray = new JSONArray();
        for(Cart c : tempCartList){
            jsonArray.put(c);
        }

        apiName = "api_insertData.php";
        String actionN = action+"checkOut";
        String urlN = url+apiName+actionN;

        //System.out.println(">>>>>>>>>>>>>>>>>>>URL= "+urlN);

        StringRequest request = new StringRequest(Request.Method.POST, urlN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String [] s= response.split("-");
                if(s[0].equals("yes")){
                    Toast.makeText(getContext(),"Size="+s[1],Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getContext(), CheckoutActivity.class);
                    i.putExtra("cartList", tempCartList);
                    i.putExtra("totalPrice", GlobalData.getInstance().tempTotalCartPrice);
                    startActivity(i);
                } else if(response.toString().equals("tokenFail")){
                    Toast.makeText(getContext(),"Wrong token, try again",Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getContext(),"Failed to delete cart item",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                    //System.out.println(">>>>>>>>>>>>>>>>>>>URL= "+urlN);
                    //Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"Error occured!",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("myToken", "786");
                params.put("userId",GlobalData.getInstance().userId+"");
                params.put("cart", jsonArray.toString());

                return params;
            }
        };

        GlobalData.getInstance().addToRequestQueue(request);
    }

}