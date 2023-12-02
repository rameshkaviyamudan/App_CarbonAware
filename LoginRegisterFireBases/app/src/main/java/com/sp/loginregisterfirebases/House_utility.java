package com.sp.loginregisterfirebases;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class House_utility extends AppCompatActivity {
    private int totalActivities = 6; // Total number of activities
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private int volleyResponseStatus;
    private static final int CHILD_ACTIVITY_REQUEST_CODE = 1001; // You can use any integer value you want
    private static final int MAIN_ACTIVITY_REQUEST_CODE = 2002; // You can use any integer value you want

    CardView lightingCard;
    CardView kaCard;

    CardView laCard;

    CardView elecCard;

    CardView hcCard;
    CardView mCard;

    private boolean isLightingProcessed = false;
    private boolean isKAProcessed = false;
    private boolean isLAProcessed = false;
    private boolean isElectronicsProcessed = false;
    private boolean isHCProcessed = false;
    private boolean isMProcessed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_utility);

        // Storing the initial value of homeCarbonFootprint in SharedPreferences
        SharedPreferences sharedhomePreferences = getSharedPreferences("MyhomePrefs", MODE_PRIVATE);
        SharedPreferences.Editor homeeditor = sharedhomePreferences.edit();
        homeeditor.putFloat("homeCarbonFootprint", 0.0f); // Set initial value as 0
        homeeditor.apply();
      //Load the BottomNavigationFragment
        BottomNavigationFragment bottomNavigationFragment = BottomNavigationFragment.newInstance("param1", "param2");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, bottomNavigationFragment);
        fragmentTransaction.commit();
        // Increment the activityCounter and store its value in SharedPreferences
        SharedPreferences sharedactPrefs = getSharedPreferences("MyactPrefs", MODE_PRIVATE);
        SharedPreferences.Editor acteditor = sharedactPrefs.edit();
        acteditor.putInt("activityCounter", 0);
        acteditor.apply();

        // Retrieve the value of homeCarbonFootprint from SharedPreferences

        float homeCarbonFootprint = sharedhomePreferences.getFloat("homeCarbonFootprint", 0.0f);
        // Convert the double value to String
        String sharedhomestring = String.valueOf(homeCarbonFootprint);
        Toast.makeText(getApplicationContext(), sharedhomestring, Toast.LENGTH_LONG).show();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        getSupportActionBar().setDisplayShowTitleEnabled(true); // Hide the title
                lightingCard = findViewById(R.id.lightingCard);
        lightingCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(House_utility.this, Lighting.class);
                intent.putExtra("cardIdentifier", 1); // Pass the card identifier (1 for lightingCard)
                startActivityForResult(intent, CHILD_ACTIVITY_REQUEST_CODE);
            }
        });

        kaCard = findViewById(R.id.kaCard);
        kaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(House_utility.this, KA.class);
                intent.putExtra("cardIdentifier", 2); // Pass the card identifier (2 for kaCard)
                startActivityForResult(intent, CHILD_ACTIVITY_REQUEST_CODE);
            }
        });
        laCard = findViewById(R.id.laCard);
        laCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(House_utility.this, LA.class);
                intent.putExtra("cardIdentifier", 3); // Pass the card identifier (2 for kaCard)
                startActivityForResult(intent, CHILD_ACTIVITY_REQUEST_CODE);            }
        });

        elecCard = findViewById(R.id.elecCard);
        elecCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(House_utility.this, Electronics.class);
                intent.putExtra("cardIdentifier", 4); // Pass the card identifier (2 for kaCard)
                startActivityForResult(intent, CHILD_ACTIVITY_REQUEST_CODE);              }
        });

        hcCard = findViewById(R.id.hcCard);
        hcCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(House_utility.this, HC.class);
                intent.putExtra("cardIdentifier", 5); // Pass the card identifier (2 for kaCard)
                startActivityForResult(intent, CHILD_ACTIVITY_REQUEST_CODE);              }
        });
        mCard = findViewById(R.id.mCard);
        mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(House_utility.this, M.class);
                intent.putExtra("cardIdentifier", 6); // Pass the card identifier (2 for kaCard)
                startActivityForResult(intent, CHILD_ACTIVITY_REQUEST_CODE);              }
        });
    }

    private void checkAllActivitiesCompleted() {
        // Retrieve the saved flag value from SharedPreferences
        SharedPreferences sharedactPrefs = getSharedPreferences("MyactPrefs", MODE_PRIVATE);
        int activityCounter = sharedactPrefs.getInt("activityCounter", 0);
        // Retrieve the stored home value from SharedPreferences
        SharedPreferences sharedhomePreferences = getSharedPreferences("MyhomePrefs", MODE_PRIVATE);
        double carbonFootprint = sharedhomePreferences.getFloat("homeCarbonFootprint", 0.0f);
        // Retrieve the firstname from SharedPreferences
        SharedPreferences sharedfirPreferences = getSharedPreferences("MyfirPrefs", MODE_PRIVATE);
        String firstname = sharedfirPreferences.getString("firstname", "");

        if (activityCounter >= totalActivities) {
            // All activities have completed the save operation
            // Retrieve the previous total carbon footprint from SharedPreferences
            SharedPreferences sharedPreferences1 = getSharedPreferences("MyhousePrefs", Context.MODE_PRIVATE);
            // Add the calculated carbon footprint to the previous total
            float updatedTotalCarbonFootprint =  (float) carbonFootprint;

            // Save the updated total carbon footprint back to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences1.edit();
            editor.putFloat("housefootprint", updatedTotalCarbonFootprint);
            editor.apply();
            sendDataToDatabase(carbonFootprint, firstname);
            int cardIdentifier = 2; // Change this to the appropriate identifier for the Lighting card (1 in this case)
            Intent intent = new Intent();
            intent.putExtra("cardIdentifier", cardIdentifier);
            setResult(RESULT_OK, intent);

            finish();
        }
    }

    private void sendDataToDatabase(double carbonFootprint , String firstname){



        String params = "?where={\"firstname\": {\"$eq\":[\"" +firstname+"\"]}}";
        String url = VolleyHelper.carburl + params; //Query by username and password

        // Construct the URL for retrieving the row with the firstname

        RequestQueue queue = Volley.newRequestQueue(this);
        // Use GET REST api call

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(), "1: ", Toast.LENGTH_LONG).show();

                        if (volleyResponseStatus == 200) { // Read successfully from database
                            Toast.makeText(getApplicationContext(), "2: ", Toast.LENGTH_LONG).show();

                            try {
                                Toast.makeText(getApplicationContext(), "4: ", Toast.LENGTH_LONG).show();

                                int count = response.getInt("count"); //Number of records from database
                                JSONArray data = response.getJSONArray("data");

                                if (count > 0) {
                                    Toast.makeText(getApplicationContext(), "5: ", Toast.LENGTH_LONG).show();

                                    // Row with the firstname exists, update the values
                                    JSONObject row = data.getJSONObject(0);
                                    double previousCarbonFootprint = row.getDouble("home_carbon_footprint");
                                    int homeEntryWeight = row.getInt("home_entry_weight");

                                    // Update the carbon footprint and entry weight
                                    double updatedCarbonFootprint = previousCarbonFootprint + carbonFootprint;
                                    int updatedFoodEntryWeight = homeEntryWeight + 1;

                                    // Convert the double value to String
                                    String updatedCarbonFootprintString = String.valueOf(updatedCarbonFootprint);

                                    // Construct the JSON object with the updated values
                                    JSONObject updatedRow = new JSONObject();
                                    updatedRow.put("home_carbon_footprint", updatedCarbonFootprintString);
                                    updatedRow.put("home_entry_weight", updatedFoodEntryWeight);


                                    // Correct the query
                                    String carbputurl = VolleyHelper.carburl  + firstname;
                                    // Use PUT REST api call to update the row
                                    JsonObjectRequest updateRequest = new JsonObjectRequest(Request.Method.PATCH, carbputurl, updatedRow,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    // Row updated successfully
                                                    // TODO: Handle the response or perform any other necessary operations
                                                    Toast.makeText(getApplicationContext(), "Row updated successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    // Error updating the row
                                                    // TODO: Handle the error or perform any other necessary operations
                                                    Toast.makeText(getApplicationContext(), "Error updating row", Toast.LENGTH_SHORT).show();
                                                    Toast.makeText(getApplicationContext(), updatedCarbonFootprintString, Toast.LENGTH_LONG).show();
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

                                    // Add the update request to the RequestQueue
                                    queue.add(updateRequest);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "6: ", Toast.LENGTH_LONG).show();

                                    // Row with the firstname does not exist, create a new row
                                    String params = "?where={\"firstname\": {\"$eq\":[\"" +firstname+"\"]}}";
                                    String carburl = VolleyHelper.carburl + params; //Query by username and password

                                    // Construct the JSON object with the new row values
                                    JSONObject newRow = new JSONObject();
                                    newRow.put("firstname", firstname);
                                    newRow.put("home_carbon_footprint", carbonFootprint);
                                    newRow.put("home_entry_weight", 1);
                                    newRow.put("food_carbon_footprint", 0);
                                    newRow.put("food_entry_weight", 0);
                                    newRow.put("travel_carbon_footprint", 0);
                                    newRow.put("travel_entry_weight", 0);
                                    newRow.put("other_carbon_footprint", 0);
                                    newRow.put("other_entry_weight", 0);
                                    newRow.put("total_carbon_footprint", 0);
                                    // Use POST REST api call to create the new row
                                    JsonObjectRequest createRequest = new JsonObjectRequest(Request.Method.POST, carburl, newRow,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    // Row created successfully
                                                    // TODO: Handle the response or perform any other necessary operations
                                                    Toast.makeText(getApplicationContext(), "Row created successfully", Toast.LENGTH_SHORT).show();
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    // Error creating the row
                                                    // TODO: Handle the error or perform any other necessary operations
                                                    Toast.makeText(getApplicationContext(), "Error creating row", Toast.LENGTH_SHORT).show();
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

                                    // Add the create request to the RequestQueue
                                    queue.add(createRequest);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        Toast.makeText(getApplicationContext(), "not successful: ", Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(getApplicationContext(), "Error retrieving row", Toast.LENGTH_SHORT).show();
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

        // Add the GET request to the RequestQueue
        queue.add(jsonObjectRequest);
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkAllActivitiesCompleted();

        SharedPreferences sharedactPrefs = getSharedPreferences("MyactPrefs", MODE_PRIVATE);
        int activityCounter = sharedactPrefs.getInt("activityCounter", 0);
        String actcounterString = String.valueOf(activityCounter);

        Toast.makeText(getApplicationContext(), actcounterString, Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHILD_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                int cardIdentifier = data.getIntExtra("cardIdentifier", -1);
                if (cardIdentifier != -1) {
                    // Update the corresponding card's processing status
                    switch (cardIdentifier) {
                        case 1:
                            isLightingProcessed = true;
                            lightingCard.setCardBackgroundColor(Color.GRAY); // Change the card's background color to gray
                            break;
                        case 2:
                            isKAProcessed = true;
                            kaCard.setCardBackgroundColor(Color.GRAY); // Change the card's background color to gray

                            break;
                        case 3:
                            isLAProcessed = true;
                            laCard.setCardBackgroundColor(Color.GRAY); // Change the card's background color to gray
                            break;
                        case 4:
                            isElectronicsProcessed = true;
                            elecCard.setCardBackgroundColor(Color.GRAY); // Change the card's background color to gray

                            break;
                        case 5:
                            isHCProcessed = true;
                            hcCard.setCardBackgroundColor(Color.GRAY); // Change the card's background color to gray

                            break;
                        case 6:
                            isMProcessed = true;
                            mCard.setCardBackgroundColor(Color.GRAY); // Change the card's background color to gray

                            break;
                        // Handle other card identifiers similarly if needed
                    }
                }
            }
        }
    }

}