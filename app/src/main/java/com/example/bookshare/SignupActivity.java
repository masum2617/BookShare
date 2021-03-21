package com.example.bookshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.bookshare.model.GlobalData;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private LinearLayout txtGotoLogin;
    private EditText edUsername, edPhone, edPassword, edRePassword;
    private RadioGroup rgGender;
    private Button btnSignup;
    private ProgressBar pbSignup;
    private TextView txtGenderError;

    private int genderCheckId, gender=0;
    private String username, phone, password, rePassword;
    private SharedPreferences preferences;
    private static final String FILE_NAME="preferenceFile";

    private String url = GlobalData.url;
    private String action = "?action=";
    private String apiName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        init();

        txtGotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoLogin = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(gotoLogin);
                finish();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag = formValidation();
                if(flag==1){
                    registerUser();
                }
                //pbSignup.setVisibility(View.VISIBLE);
                //registerUser();
            }
        });
    }

    private void init(){
        txtGotoLogin = (LinearLayout) findViewById(R.id.txtGotoLogin);
        edUsername = (EditText) findViewById(R.id.username_signup);
        edPhone = (EditText) findViewById(R.id.phone_signup);
        edPassword = (EditText) findViewById(R.id.password_signup);
        edRePassword = (EditText) findViewById(R.id.rePassword_signup);
        rgGender = (RadioGroup) findViewById(R.id.genderSelect_signup);
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.maleSelect_signup:
                        gender = 1;
                        break;

                    case R.id.femaleSelect_signup:
                        gender = 2;
                        break;
                }
            }
        });
        btnSignup = (Button) findViewById(R.id.btn_signup);
        pbSignup = (ProgressBar) findViewById(R.id.pb_signup);
        pbSignup.setVisibility(View.GONE);
        txtGenderError = (TextView) findViewById(R.id.txtGenderError_signup);
        txtGenderError.setVisibility(View.GONE);
    }

    private void getFormValues(){
        username = edUsername.getText().toString();
        phone = edPhone.getText().toString();
        password = edPassword.getText().toString();
        rePassword = edRePassword.getText().toString();
        genderCheckId = rgGender.getCheckedRadioButtonId();
        System.out.println("-----------------Gender ID="+genderCheckId);
    }

    private int formValidation(){
        getFormValues();
        txtGenderError.setVisibility(View.GONE);
        pbSignup.setVisibility(View.GONE);

        if(TextUtils.isEmpty(username)){
            edUsername.setError("Please enter username");
            return 0;
        }

        if(TextUtils.isEmpty(phone)){
            edPhone.setError("Please enter phone number");
            return 0;
        }

        if(TextUtils.isEmpty(password)){
            edPassword.setError("Please enter password");
            return 0;
        }else if(password.length()<6){
            edPassword.setError("Password should be atleast 6 digit");
            return 0;
        }

        if(!rePassword.equals(password)){
            edRePassword.setError("Password don't match");
            return 0;
        }

        if(rgGender.getCheckedRadioButtonId()==-1){
            txtGenderError.setVisibility(View.VISIBLE);
            return 0;
        }

        return 1;
    }

    public void registerUser(){
        pbSignup.setVisibility(View.VISIBLE);
        apiName = "api_signup.php";
        action = action+"signup";
        url = url+apiName+action;
        //System.out.println("----------URL= "+url);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.toString().equals("yes")){
                    //Intent i = new Intent(getApplicationContext(),OrderActivity.class);
                    //startActivity(i);
                    Toast.makeText(getApplicationContext(),"You've registered successfully",Toast.LENGTH_LONG).show();
                    //pb.setVisibility(View.GONE);
                    Intent goLogin = new Intent(SignupActivity.this,LoginActivity.class);
                    startActivity(goLogin);
                    finish();

                } else if(response.toString().equals("tokenFail")){
                    Toast.makeText(getApplicationContext(),"Wrong token, try again",Toast.LENGTH_SHORT).show();
                    //pb.setVisibility(View.GONE);
                    Intent reload = new Intent(getApplicationContext(),SignupActivity.class);
                    startActivity(reload);
                    finish();
                } else{
                    //msg.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"Server error, try again",Toast.LENGTH_LONG).show();
                    Intent reload = new Intent(getApplicationContext(),SignupActivity.class);
                    startActivity(reload);
                    finish();
                    //System.out.println("$$$"+response+"$$$");
                    //pb.setVisibility(View.GONE);
                }
                //System.out.println(">>>>>>>>>>>>>>>>>>>>"+response);
                pbSignup.setVisibility(View.GONE);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error occured!",Toast.LENGTH_SHORT).show();
                //pb.setVisibility(View.GONE);
                System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<"+"ERRoR");
                pbSignup.setVisibility(View.GONE);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("myToken", "786");
                params.put("username",username);
                params.put("password",password);
                params.put("phone",phone);
                params.put("gender",gender+"");

                return params;
            }
        };


        //System.out.println("edUsername= "+GlobalData.getInstance().username);
        //System.out.println(GlobalData.getInstance());
        GlobalData.getInstance().addToRequestQueue(request);
    }
}
