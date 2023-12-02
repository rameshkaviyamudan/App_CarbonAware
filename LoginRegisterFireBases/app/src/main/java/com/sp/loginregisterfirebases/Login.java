package com.sp.loginregisterfirebases;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Map;

public class Login extends AppCompatActivity implements TextToSpeech.OnInitListener {
    EditText firstname;
    EditText password;
    Button login;
    Button signupbtn;

    ImageButton togglePasswordButton;

    private int volleyResponseStatus;

    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firstname = findViewById(R.id.name);
        password = findViewById(R.id.pword);
        login = findViewById(R.id.login);
        signupbtn = findViewById(R.id.signupbtn);
        login.setOnClickListener(onLogin);
        signupbtn.setOnClickListener(onSignUp);
        togglePasswordButton = findViewById(R.id.togglePassword);

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

        textToSpeech = new TextToSpeech(this, this);
    }

    View.OnClickListener onLogin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("Login", "Login button clicked");
            String firstnameStr = firstname.getText().toString();
            String passwordStr = password.getText().toString();
            getByUserNamePasswordVolley(firstnameStr, passwordStr);
        }
    };

    View.OnClickListener onSignUp = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        }
    };



    // Get a record using the current ID
    private void getByUserNamePasswordVolley(String firstnameStr, String passwordStr) {
        String params = "?where={\"firstname\": {\"$eq\":[\"" + firstnameStr + "\"]}, \"password\" : {\"$eq\":[\"" + passwordStr + "\"]}}";
        String url = VolleyHelper.loginurl + params; // Query by username and password
        RequestQueue queue = Volley.newRequestQueue(this);
        // Use GET REST api call
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (volleyResponseStatus == 200) { // Read successfully from database
                            try {
                                int count = response.getInt("count"); // Number of records from database
                                if (count > 0) {
                                    // Use the retrieved username as needed
                                    Toast.makeText(getApplicationContext(), "Welcome to CarbonAware " + firstnameStr, Toast.LENGTH_SHORT).show();
                                    // Store the username and password in SharedPreferences
                                    SharedPreferences sharedfirPreferences = getSharedPreferences("MyfirPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedfirPreferences.edit();
                                    editor.putString("firstname", firstnameStr);
                                    editor.putString("password", passwordStr); // Add the password here
                                    editor.apply();

                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Login Fail", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
        // add JsonObjectRequest to the RequestQueue
        queue.add(jsonObjectRequest);
    }

    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            // Set the language of TTS (you can change to Locale.US, Locale.FRANCE, etc.)
            int result = textToSpeech.setLanguage(Locale.ENGLISH);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Handle language not supported or missing data
            } else {
                // Speak the welcome message after TTS is successfully initialized
                String welcomeMessage = "Welcome to CarbonAware";
                textToSpeech.speak(welcomeMessage, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        } else {
            // Handle TTS initialization failure
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release the TextToSpeech resources when the activity is destroyed
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}