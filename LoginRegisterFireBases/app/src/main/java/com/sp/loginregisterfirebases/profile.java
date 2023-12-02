package com.sp.loginregisterfirebases;

// Import statements...

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class profile extends AppCompatActivity {
    EditText firstname;
    EditText password;
    Button delete;
    private int volleyResponseStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        firstname = findViewById(R.id.firstname2);
        password = findViewById(R.id.Password);
        delete = findViewById(R.id.delete);
        delete.setOnClickListener(onDelete);

        // Load the BottomNavigationFragment
        BottomNavigationFragment bottomNavigationFragment = BottomNavigationFragment.newInstance("param1", "param2");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, bottomNavigationFragment);
        fragmentTransaction.commit();
    }

    View.OnClickListener onDelete = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String firstnameStr = firstname.getText().toString();
            String passwordStr = password.getText().toString();
            deleteAccount(firstnameStr, passwordStr);
            Intent intent = new Intent(getApplicationContext(), Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    };

    private void deleteAccount(String firstnameStr, String passwordStr) {
        String params = firstnameStr + "/" + passwordStr;
        String url = VolleyHelper.loginurl + params; // Query by username and password
        RequestQueue queue = Volley.newRequestQueue(this);

        // Use DELETE REST api call
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // This block will execute when the request is successful
                        if (volleyResponseStatus == 204) {
                            Toast.makeText(profile.this, "Response status: " + volleyResponseStatus, Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), "Account deleted", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // This block will execute when there is an error with the request
                        Log.e("OnErrorResponse", error.toString());
                        Toast.makeText(getApplicationContext(), "Account deleted", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                return VolleyHelper.getHeader();
            }
        };

        // Add JsonObjectRequest to the RequestQueue
        queue.add(jsonObjectRequest);
    }
}