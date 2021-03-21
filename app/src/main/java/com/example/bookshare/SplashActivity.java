package com.example.bookshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.bookshare.model.Book;
import com.example.bookshare.model.Cart;
import com.example.bookshare.model.Category;
import com.example.bookshare.model.GlobalData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {
    private TextView errorMessage;
    //private Button tryAgainBtn;
    private  Button tryAgainBtn;
    private ImageView errorIcon;

    private ProgressBar pb;

    SharedPreferences preferences;
    public static final String FILE_NAME="preferenceFile";

    private String url = GlobalData.url;
    private String action = "?action=";
    private String apiName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Create fullscreen mode
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tryAgainBtn = findViewById(R.id.btn_tryAgain);
        errorMessage = findViewById(R.id.noConnectionText);
        errorIcon = findViewById(R.id.noConnectionIcon);
        pb = findViewById(R.id.progressBar);

        if(!isConnected(getApplicationContext())){
            tryAgainBtn.setVisibility(View.VISIBLE);
            errorMessage.setVisibility(View.VISIBLE);
            errorIcon.setVisibility(View.VISIBLE);
            tryAgainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent loadAgain = new Intent(getApplicationContext(),SplashActivity.class);
                    startActivity(loadAgain);
                }
            });

        }
        else {
            pb.setVisibility(View.VISIBLE);
            tryAgainBtn.setVisibility(View.GONE);
            errorMessage.setVisibility(View.GONE);
            errorIcon.setVisibility(View.GONE);
            new WaitTask().execute();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
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

    class WaitTask extends AsyncTask<Void, Void, Void> {

        //ArrayList<Category> ac = new ArrayList<Category>();
       // ArrayList<Content> contentList = new ArrayList<Content>();
        Boolean serverError = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            preferences = getSharedPreferences(FILE_NAME,MODE_PRIVATE);
            fetchPrefData();
            //loadCategoryData();

        }




        protected void fetchPrefData(){
            if(preferences.getString("loggedin","").equals("yes")){
                //System.out.println("@@@@@@@@@@@@@@ user id set done");
                GlobalData.getInstance().userId = preferences.getInt("userId",0);
                GlobalData.getInstance().userName = preferences.getString("username","");
                GlobalData.getInstance().userPassword = preferences.getString("password","");
                //checkCartValue();
                loadCategoryData();
            }else{
                Intent goLogin = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(goLogin);
            }
        }
    }



    protected void loadCategoryData(){
        GlobalData.getInstance().categoryList.clear();
        apiName = "api_getData.php";
        String actionN = action+"getCategories";
        String urlN = url+apiName+actionN;
        System.out.println("--------------------------------------url:"+urlN+"#");
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlN, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //System.out.println("------------------@@@@@@response");

                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Category c = new Category();
                        c.categoryId = Integer.parseInt(object.getString("idCategory"));
                        c.categoryName = object.getString("categoryName");

                        GlobalData.getInstance().categoryList.add(c);

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),"Exception on loading category data",Toast.LENGTH_LONG).show();
                        //serverError = true;
                        e.printStackTrace();
                    }
                }
                //GlobalData.getInstance().cartValue = GlobalData.getInstance().categoryList.size();

                //showCategories();
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>Response length = "+response.length());
                System.out.println("----------------------total categories = "+GlobalData.getInstance().categoryList.size());

                //Intent goHome = new Intent(SplashActivity.this,MainActivity.class);
                //startActivity(goHome);
                loadCartData();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error loading category data",Toast.LENGTH_LONG).show();
                //serverError = true;
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("myToken", "786");

                return params;
            }
        };
        GlobalData.getInstance().addToRequestQueue(request);

    }



    protected void loadCartData(){
        GlobalData.getInstance().totalCartPrice = 0.00;
        GlobalData.getInstance().cartList.clear();
        apiName = "api_getData.php";
        String actionN = action+"getCartItems";
        String urlN = url+apiName+actionN;

        //System.out.println("--------------------------------------url:"+urlN+"#");
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, urlN, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //System.out.println("------------------@@@@@@response");

                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Cart c = new Cart();
                        c.cartId = Integer.parseInt(object.getString("idCart"));
                        c.userId = Integer.parseInt(object.getString("idUser"));
                        c.bookId = Integer.parseInt(object.getString("idBook"));
                        c.bookTitle = object.getString("bookTitle");
                        c.bookPrice = Double.parseDouble(object.getString("bookPrice"));
                        c.bookPicture = object.getString("bookPicture");
                        c.quantity = Integer.parseInt(object.getString("quantity"));
                        c.totalPrice = Double.parseDouble(object.getString("totalPrice"));
                        GlobalData.getInstance().totalCartPrice += c.totalPrice;

                        GlobalData.getInstance().cartList.add(c);

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),"Exception on loading cart data",Toast.LENGTH_LONG).show();
                        //serverError = true;
                        e.printStackTrace();
                    }
                }
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>Cart size="+GlobalData.getInstance().cartList.size());
                GlobalData.getInstance().cartValue = GlobalData.getInstance().cartList.size();

                //showCategories();
                //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>Response length = "+response.length());
                //System.out.println("----------------------total categories = "+GlobalData.getInstance().categoryList);

                loadRecentBooks();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error loading cart data",Toast.LENGTH_LONG).show();
                //serverError = true;
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("myToken", "786");
                params.put("userId",GlobalData.getInstance().userId+"");

                return params;
            }
        };
        GlobalData.getInstance().addToRequestQueue(request);

    }



    protected void loadRecentBooks(){
        GlobalData.getInstance().recentBooks.clear();
        apiName = "api_getData.php";
        String actionN = action+"getRecentBooks";
        String urlN = url+apiName+actionN;

        //System.out.println("--------------------------------------url:"+urlN+"#");
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, urlN, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //System.out.println("------------------@@@@@@response");

                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        Book b = new Book();
                        b.id = Integer.parseInt(object.getString("idBook"));
                        b.price = Double.parseDouble(object.getString("bookPrice"));
                        b.minimumQuantity = Integer.parseInt(object.getString("minimumQuantity"));
                        b.totalPage = Integer.parseInt(object.getString("totalPage"));
                        b.title = object.getString("bookTitle");
                        b.picture = object.getString("bookPicture");
                        b.link = object.getString("bookLink");
                        b.edition = object.getString("edition");
                        b.isbn = object.getString("isbn");
                        b.publisher = object.getString("publisher");
                        b.country = object.getString("country");
                        b.language = object.getString("language");
                        b.description = object.getString("bookDescription");
                        b.category = object.getString("categoryName");
                        b.author = object.getString("author");
                        b.idUser = Integer.parseInt(object.getString("idUser"));
                        b.userName = object.getString("userName");
                        b.purpose = Integer.parseInt(object.getString("purpose"));

                        for(Cart c : GlobalData.getInstance().cartList){
                            if(b.id == c.bookId){
                                b.cartValue = c.quantity;
                            }

                            //System.out.println("Book id="+c.bookId+","+b.id+" ; quantity="+c.quantity+","+b.cartValue);
                        }
                        System.out.println("Book id="+b.id+" ; purpose="+b.purpose);

                        GlobalData.getInstance().recentBooks.add(b);

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),"Exception on loading recent books",Toast.LENGTH_LONG).show();
                        //serverError = true;
                        e.printStackTrace();
                    }
                }
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>Recent books size="+GlobalData.getInstance().recentBooks.size());

                //showCategories();
                //System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>Response length = "+response.length());
                //System.out.println("----------------------total categories = "+GlobalData.getInstance().categoryList);

                Intent goHome = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(goHome);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error loading cart data",Toast.LENGTH_LONG).show();
                //serverError = true;
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("myToken", "786");
                params.put("userId",GlobalData.getInstance().userId+"");

                return params;
            }
        };
        GlobalData.getInstance().addToRequestQueue(request);

    }



    private void checkCartValue() {
        apiName = "api_getData.php";
        action = action+"getCartValue";
        url = url+apiName+action;
        //System.out.println("----------URL= "+url);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String [] s= response.split("-");
                if(s[0].equals("yes")){
                    GlobalData.getInstance().cartValue = Integer.parseInt(s[1]);
                    System.out.println("-+-+-+-+-+-+-+-+-cart value="+GlobalData.getInstance().cartValue+";");
                    Intent goHome = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(goHome);
                } else{
                    GlobalData.getInstance().cartValue = 0;
                }
                //System.out.println(">>>>>>>>>>>>>>>>>>>>"+response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error occured!",Toast.LENGTH_SHORT).show();
                //pb.setVisibility(View.GONE);
                //System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<"+"ERRoR");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("myToken", "786");
                params.put("userId",GlobalData.getInstance().userId+"");
                //params.put("password",password);

                return params;
            }
        };


        //System.out.println("edUsername= "+GlobalData.getInstance().username);
        //System.out.println(GlobalData.getInstance());
        GlobalData.getInstance().addToRequestQueue(request);
    }
}
