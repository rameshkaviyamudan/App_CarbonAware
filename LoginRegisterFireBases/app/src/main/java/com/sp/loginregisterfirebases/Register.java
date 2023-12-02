package com.sp.loginregisterfirebases;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {


    EditText firstname;
    EditText lastname;
    EditText number;
    EditText password;
    Button register;

    ImageButton togglePasswordButton;
    private int volleyResponseStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        number = findViewById(R.id.number);
        password = findViewById(R.id.password2);
        register = findViewById(R.id.register);
        register.setOnClickListener(onRegister);
        togglePasswordButton = findViewById(R.id.togglePassword2);

        // Remove title bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Remove title from window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }


        togglePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
                    // Show password
                    password.setTransformationMethod(null);
                    togglePasswordButton.setImageResource(R.drawable.ic_eye_closed);
                } else {
                    // Hide password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    togglePasswordButton.setImageResource(R.drawable.ic_eye_closed);
                }
                // Move cursor to the end of the text
                password.setSelection(password.getText().length());
            }
        });


    }


    View.OnClickListener onRegister = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String firstnameStr = firstname.getText().toString();
            String lastnameStr = lastname.getText().toString();
            String numberStr = number.getText().toString();
            String passwordStr = password.getText().toString();
            insertVolley(firstnameStr, lastnameStr, numberStr, passwordStr);
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
        }
    };

    private void insertVolley(String firstnameStr, String lastnameStr, String numberStr, String passwordStr) {
        // Create a JSON object from the parame ters
        Map<String, String> params = new HashMap<>();
        params.put("firstname", firstnameStr);
        params.put("lastname", lastnameStr);
        params.put("number", numberStr);
        params.put("password", passwordStr);

        JSONObject postdata = new JSONObject(params); // Data as JSON object to be inserted into the database
        RequestQueue queue = Volley.newRequestQueue(this);
        // REST API link
        String url = VolleyHelper.loginurl;
        // Use POST REST API call
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, postdata,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (volleyResponseStatus == 201) { // Created successfully in the database
                            Toast.makeText(getApplicationContext(), "Sign Up Successful", Toast.LENGTH_LONG).show();
                            // Optionally, you can perform additional actions here, such as navigating to the login screen

                            Intent intent = new Intent(Register.this, Login.class);
                            intent.putExtra("firstname", firstnameStr);
                            intent.putExtra("password", passwordStr);
                            startActivity(intent);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.e("OnErrorResponse", error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return VolleyHelper.getHeader();
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                volleyResponseStatus = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };
        // Add JsonObjectRequest to the RequestQueue
        queue.add(jsonObjectRequest);
    }
}
