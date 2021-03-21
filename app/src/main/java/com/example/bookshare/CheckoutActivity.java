package com.example.bookshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.bookshare.model.Cart;
import com.example.bookshare.model.GlobalData;
import com.example.bookshare.model.PaymentMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {
    private TextView txtCartSize, checkoutTotalPrice, checkoutTotalBooks, checkoutSubtotalPrice, checkoutTotalVat, checkoutTotalVatPercent, amountWithMethodCharge, methodChargePercent, methodName;
    private ImageView backBtn;
    private RadioGroup rgPaymentMethod;
    private LinearLayout lvMethodErrorMsg, afterMethodChargeSection;
    private Button checkoutConfirmButton;

    private ArrayList<Cart> cartList = new ArrayList<Cart>();
    private List<PaymentMethod> methodList = new ArrayList<PaymentMethod>();
    private double totalPrice;
    private double totalAfterCharge;

    private String url = GlobalData.url;
    private String action = "?action=";
    private String apiName = "";

    private PaymentMethod pmSelected = new PaymentMethod();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        txtCartSize = (TextView) findViewById(R.id.cartSize);
        checkoutTotalBooks = (TextView) findViewById(R.id.checkoutTotalBooks);
        checkoutSubtotalPrice = (TextView) findViewById(R.id.checkoutSubtotalPrice);
        checkoutTotalVat = (TextView) findViewById(R.id.checkoutTotalVat);
        checkoutTotalVatPercent = (TextView) findViewById(R.id.checkoutTotalVatPercent);
        checkoutTotalPrice = (TextView) findViewById(R.id.checkoutTotalPrice);
        rgPaymentMethod = (RadioGroup) findViewById(R.id.rgPaymentMethod);
        lvMethodErrorMsg = (LinearLayout) findViewById(R.id.methodErrorMsg);
        checkoutConfirmButton = (Button) findViewById(R.id.checkoutConfirmButton);
        afterMethodChargeSection = (LinearLayout) findViewById(R.id.after_method_charge_section);
        amountWithMethodCharge = (TextView) findViewById(R.id.amountWithMethodCharge);
        methodChargePercent = (TextView) findViewById(R.id.methodChargePercent);
        methodName = (TextView) findViewById(R.id.methodName);

        Intent intent = getIntent();
        cartList = (ArrayList<Cart>) intent.getSerializableExtra("cartList");
        totalPrice = intent.getDoubleExtra("totalPrice",0.00);
        //System.out.println(cartList.size());
        totalAfterCharge = totalPrice;

        int totalBooks = 0;
        for(Cart c : cartList){
            totalBooks += c.quantity;
        }

        txtCartSize.setText(cartList.size()+"");
        checkoutTotalBooks.setText(totalBooks+"");
        checkoutSubtotalPrice.setText(totalPrice+"");
        checkoutTotalPrice.setText(totalPrice+"");
        rgPaymentMethod.setOrientation(RadioGroup.VERTICAL);

        //load and show payment methods
        loadPaymentMethods();

        //action back
        backBtn = (ImageView) findViewById(R.id.btn_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //show cart products list
        //lvCart = (ListView) findViewById(R.id.checkout_book_list);
        //ListAdapterCheckoutBookList adapter = new ListAdapterCheckoutBookList(cartList, getApplicationContext());
        //lvCart.setAdapter(adapter);
        LinearLayout checkoutList = (LinearLayout) findViewById(R.id.checkoutList);
        for(int i=0; i<cartList.size(); i++){
            Cart cart = cartList.get(i);
            View view = getLayoutInflater().inflate(R.layout.row_item_cart_book, null);
            ImageView bookImage = (ImageView) view.findViewById(R.id.book_image);
            TextView bookTitle = (TextView) view.findViewById(R.id.book_title);
            TextView bookPrice = (TextView) view.findViewById(R.id.book_price);
            final TextView subTotal = (TextView) view.findViewById(R.id.cartItemSubTotal);
            final LinearLayout afterCartSection = (LinearLayout) view.findViewById(R.id.afterCart_section);
            final ProgressBar pbCart = (ProgressBar) view.findViewById(R.id.pbCart);
            final ImageButton btnCartMinus = (ImageButton) view.findViewById(R.id.btn_cart_minus);
            ImageButton btnCartPlus = (ImageButton) view.findViewById(R.id.btn_cart_plus);
            final TextView mCartQuantity = (TextView) view.findViewById(R.id.txtCartQuantity);
            final LinearLayout btnRemoveCartItem = (LinearLayout) view.findViewById(R.id.btnRemoveCartItem);
            TextView txtCartItemQty = (TextView) view.findViewById(R.id.txtCartItemQty);

            bookTitle.setText(cart.getBookTitle());
            bookPrice.setText(cart.getBookPrice()+"");
            subTotal.setText(cart.getTotalPrice()+"");
            mCartQuantity.setText(cart.getQuantity()+"");
            btnCartPlus.setVisibility(View.INVISIBLE);
            btnCartMinus.setVisibility(View.GONE);
            txtCartItemQty.setVisibility(View.VISIBLE);
            btnRemoveCartItem.setVisibility(View.GONE);
            //view.setElevation(0);
            checkoutList.addView(view);
            if(i != (cartList.size()-1)){
                view = getLayoutInflater().inflate(R.layout.row_vertical_line, null);
                checkoutList.addView(view);
            }
        }

        checkoutConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rgPaymentMethod.getCheckedRadioButtonId() == -1){
                    lvMethodErrorMsg.setVisibility(View.VISIBLE);
                }else {
                    checkOut();
                }
            }
        });

        rgPaymentMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                PaymentMethod pm = methodList.get(checkedId);
                pmSelected = pm;
                if(pm.methodCharge>0){
                    totalAfterCharge = totalPrice + (totalPrice * pm.methodCharge / 100);
                    amountWithMethodCharge.setText(totalAfterCharge+"");
                    methodChargePercent.setText(pm.methodCharge+"");
                    methodName.setText(pm.methodName+"");
                    afterMethodChargeSection.setVisibility(View.VISIBLE);
                    lvMethodErrorMsg.setVisibility(View.GONE);
                }
            }
        });
    }


    protected void loadPaymentMethods(){
        apiName = "api_getData.php";
        String actionN = action+"getPaymentMethods";
        String urlN = url+apiName+actionN;
        //System.out.println("--------------------------------------url:"+urlN+"#");
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, urlN, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //System.out.println("------------------@@@@@@response");
                final RadioButton[] rb = new RadioButton[response.length()];
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject object = response.getJSONObject(i);
                        PaymentMethod pm = new PaymentMethod();
                        pm.methodId = Integer.parseInt(object.getString("idPaymentMethod"));
                        pm.methodName = object.getString("methodName");
                        pm.methodLogo = object.getString("methodLogo");
                        pm.methodCharge = Double.parseDouble(object.getString("methodCharge"));
                        pm.methodInfo = object.getString("methodInfo");
                        pm.methodInfoImage = object.getString("methodInfoImage");

                        methodList.add(pm);

                        rb[i]  = new RadioButton(getApplicationContext());
                        rb[i].setText(" " + pm.methodName);
                        rb[i].setId(i);
                        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0, 0, 0, 30);
                        rb[i].setLayoutParams(params);
                        rgPaymentMethod.addView(rb[i]);

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(),"Exception on loading method data",Toast.LENGTH_LONG).show();
                        //serverError = true;
                        e.printStackTrace();
                    }
                }
                //GlobalData.getInstance().cartValue = GlobalData.getInstance().categoryList.size();

                //showCategories();
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>Response length = "+response.length());
                System.out.println("----------------------total methods = "+methodList.size());

                //Intent goHome = new Intent(SplashActivity.this,MainActivity.class);
                //startActivity(goHome);

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

    private void checkOut(){

        final JSONArray jsonArray = new JSONArray();
        for(Cart c : cartList){
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
                    Toast.makeText(getApplicationContext(),"Order ID="+s[1],Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ThankyouActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("paymentMethodObj", pmSelected);
                    intent.putExtra("totalAmount", totalAfterCharge);
                    startActivity(intent);
                } else if(response.toString().equals("tokenFail")){
                    Toast.makeText(getApplicationContext(),"Wrong token, try again",Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(),"Failed to create order",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                    //System.out.println(">>>>>>>>>>>>>>>>>>>URL= "+urlN);
                    //Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error occured!",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("myToken", "786");
                params.put("userId",GlobalData.getInstance().userId+"");
                params.put("cart", jsonArray.toString());
                params.put("totalBill", totalAfterCharge+"");

                return params;
            }
        };

        GlobalData.getInstance().addToRequestQueue(request);
    }

}