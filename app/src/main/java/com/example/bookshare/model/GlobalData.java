package com.example.bookshare.model;

import android.app.Application;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalData extends Application {
    public static final String TAG = GlobalData.class
            .getSimpleName();

    private RequestQueue mRequestQueue;

    private static GlobalData mInstance;

    public static final String url = "https://app.al-hilaal.net/bookshare/api/";

    public String userName = "";
    public int userId = 0;
    public String userPassword = "";
    public String userEmail = "";
    public String hijriMonth = "";
    public String activeMonth = "";
    public int monthId = 0;
    public int activeMonthId = 0;
    public int monthPosition = 0;
    public int activeMonthPosition = 0;
    public String hijriYear = "";
    public String activeYear = "";
    public  int yearId = 0;
    public  int activeYearId = 0;
    public  int yearPosition = 0;
    public  int activeYearPosition = 0;
    public boolean onRestartFlg = false;
    public int cartValue = 0;
    public int lastCartId;
    public double totalCartPrice = 0.00;
    public double tempTotalCartPrice = 0.00;

    public List<Cart> cartList = new ArrayList<Cart>();
    public List<Category> categoryList = new ArrayList<Category>();
    public List<Book> recentBooks = new ArrayList<Book>();
    public List<Book> myShelfBooks = new ArrayList<Book>();
    public List<Integer> cartBookIds = new ArrayList<Integer>();

    //set default status value 2 which means no book_icon in the process of adding to cart
    public int cartStatus = 2;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized GlobalData getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        //System.out.println("@@@@@@request processing");
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        //System.out.println("@@@@@@request processed");
        return mRequestQueue;
    }



    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public final void addToCart(final int userIdcart, final int bookIdcart){
        //pbSignup.setVisibility(View.VISIBLE);
        String apiName = "api_insertData.php";
        String action = "?action="+"addToCart";
        String urlFinal = url+apiName+action;
        //System.out.println("----------URL= "+url);
        boolean status;

        StringRequest request = new StringRequest(Request.Method.POST, urlFinal, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.toString().equals("yes")){
                    //Intent i = new Intent(getApplicationContext(),OrderActivity.class);
                    //startActivity(i);
                    Toast.makeText(getApplicationContext(),"Added to cart successfully",Toast.LENGTH_SHORT).show();
                    //pb.setVisibility(View.GONE);
                    //Intent goLogin = new Intent(SignupActivity.this, LoginActivity.class);
                    //startActivity(goLogin);
                    //finish();
                    GlobalData.getInstance().cartStatus = 1;
                } else if(response.toString().equals("tokenFail")){
                    Toast.makeText(getApplicationContext(),"Wrong token, try again",Toast.LENGTH_SHORT).show();
                    //pb.setVisibility(View.GONE);
                    //Intent reload = new Intent(getApplicationContext(),SignupActivity.class);
                    //startActivity(reload);
                    //finish();
                    GlobalData.getInstance().cartStatus = 0;
                } else{
                    //msg.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"Failed to add in cart-"+userIdcart+"-"+bookIdcart,Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),url,Toast.LENGTH_LONG).show();
                    //Intent reload = new Intent(getApplicationContext(),SignupActivity.class);
                    //startActivity(reload);
                    //finish();
                    //System.out.println("$$$"+response+"$$$");
                    //pb.setVisibility(View.GONE);
                    GlobalData.getInstance().cartStatus = 0;
                }
                //System.out.println(">>>>>>>>>>>>>>>>>>>>"+response);
                //pbSignup.setVisibility(View.GONE);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error occured!",Toast.LENGTH_SHORT).show();
                //pb.setVisibility(View.GONE);
                //System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<"+"ERRoR");
                //pbSignup.setVisibility(View.GONE);
                GlobalData.getInstance().cartStatus = 0;
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("myToken", "786");
                params.put("userId",userIdcart+"");
                params.put("bookId",bookIdcart+"");

                return params;
            }
        };


        //System.out.println("edUsername= "+GlobalData.getInstance().username);
        //System.out.println(GlobalData.getInstance());
        GlobalData.getInstance().addToRequestQueue(request);

    }
}
