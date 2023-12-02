package com.sp.loginregisterfirebases;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Travel extends AppCompatActivity {
    private TextView valueTextView1;
    private TextView valueTextView2;
    private TextView valueTextView3;
    private TextView valueTextView4;
    private Button minusButton1;
    private Button minusButton2;
    private Button minusButton3;
    private Button minusButton4;
    private static final int MAIN_ACTIVITY_REQUEST_CODE = 2002; // You can use any integer value you want

    private Button plusButton1;
    private Button plusButton2;
    private Button plusButton3;
    private Button plusButton4;
    private Button resetButton, saveButton;
    private int volleyResponseStatus;

    private int value1 = 0;
    private int value2 = 0;
    private int value3 = 0;
    private int value4 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);
        // Retrieve the firstname from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyfirPrefs", MODE_PRIVATE);
        String firstname = sharedPreferences.getString("firstname", "");
        valueTextView1 = findViewById(R.id.valueTextView1);
        valueTextView2 = findViewById(R.id.valueTextView2);
        valueTextView3 = findViewById(R.id.valueTextView3);
        valueTextView4 = findViewById(R.id.valueTextView4);
        minusButton1 = findViewById(R.id.minusButton1);
        minusButton2 = findViewById(R.id.minusButton2);
        minusButton3 = findViewById(R.id.minusButton3);
        minusButton4 = findViewById(R.id.minusButton4);
        plusButton1 = findViewById(R.id.plusButton1);
        plusButton2 = findViewById(R.id.plusButton2);
        plusButton3 = findViewById(R.id.plusButton3);
        plusButton4 = findViewById(R.id.plusButton4);
        saveButton = findViewById(R.id.travelsaveButton);

        // Retrieve the saved flag value from SharedPreferences
        SharedPreferences sharedtrsavePreferences = getSharedPreferences("MytrPrefs", MODE_PRIVATE);
        boolean trcalculationsDone = sharedtrsavePreferences.getBoolean("calculationsDone", false);

        // Check if the calculations have already been done
        if (trcalculationsDone) {
            // Disable the save button
            saveButton.setEnabled(false);
        }
        updateValue();
        minusButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseValue1();
            }
        });

        minusButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseValue2();
            }
        });

        minusButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseValue3();
            }
        });
        minusButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseValue4();
            }
        });

        plusButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseValue1();
            }
        });

        plusButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseValue2();
            }
        });

        plusButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseValue3();
            }
        });
        plusButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseValue4();
            }
        });

        // Set click listener for save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });
        // Load the BottomNavigationFragment
        BottomNavigationFragment bottomNavigationFragment = BottomNavigationFragment.newInstance("param1", "param2");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, bottomNavigationFragment);
        fragmentTransaction.commit();

    }

    private void updateValue() {
        valueTextView1.setText(String.valueOf(value1));
        valueTextView2.setText(String.valueOf(value2));
        valueTextView3.setText(String.valueOf(value3));
        valueTextView4.setText(String.valueOf(value4));
    }
    private void showConfirmationDialog() {
        // Build the confirmation message with the entered values
        StringBuilder confirmationMessage = new StringBuilder("Confirm the following entries:\n");
        confirmationMessage.append("Refrigerator: ").append(value1).append(" Hours").append("\n");
        confirmationMessage.append("Oven: ").append(value2).append(" Hours").append("\n");
        confirmationMessage.append("Microwave: ").append(value3).append(" Hours").append("\n");
        confirmationMessage.append("Dishwasher: ").append(value4).append(" Hours").append("\n");

        // Create and show the confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Save");
        builder.setMessage(confirmationMessage.toString());
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveData(); // Proceed with saving data
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing (cancel saving)
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void saveData() {
        // Calculate the carbon footprint based on the values of kitchen appliances
        // Assign a carbon footprint value to each appliance and calculate the total

        double carbonFootprint = 0.0;

        // Assign carbon footprint values based on the appliance type and quantity
        carbonFootprint += value1 * 1.0; // Example carbon footprint value for MRT
        carbonFootprint += value2 * 2.0;  // Example carbon footprint value for Bus
        carbonFootprint += value3 * 3.0;  // Example carbon footprint value for Car
        carbonFootprint += value4 * 0.5;  // Example carbon footprint value for Bike
        // Retrieve the firstname from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyfirPrefs", MODE_PRIVATE);
        String firstname = sharedPreferences.getString("firstname", "");

        SharedPreferences sharedPreferences1 = getSharedPreferences("MytravelPrefs", Context.MODE_PRIVATE);
        // Add the calculated carbon footprint to the previous total
        float updatedTotalCarbonFootprint =  (float) carbonFootprint;

        // Save the updated total carbon footprint back to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        editor.putFloat("travelfootprint", updatedTotalCarbonFootprint);
        editor.apply();

        saveCarbonFootprintToDatabase(carbonFootprint, firstname);
        // Disable the save button
        saveButton.setEnabled(false);
        // Save the flag indicating that calculations have been done
        SharedPreferences sharedtrsavePreferences = getSharedPreferences("MytrPrefs", MODE_PRIVATE);
        SharedPreferences.Editor treditor = sharedtrsavePreferences.edit();
        treditor.putBoolean("calculationsDone", true);
        treditor.apply();
        // Finish the activity to go back
        int cardIdentifier = 3; // Change this to the appropriate identifier for the Lighting card (1 in this case)
        Intent intent = new Intent();
        intent.putExtra("cardIdentifier", cardIdentifier);
        setResult(RESULT_OK, intent);

        finish();
    }
    private void decreaseValue1() {
        value1--;
        updateValue();
    }

    private void decreaseValue2() {
        value2--;
        updateValue();
    }

    private void decreaseValue3() {
        value3--;
        updateValue();
    }

    private void decreaseValue4() {
        value4--;
        updateValue();
    }

    private void increaseValue1() {
        value1++;
        updateValue();
    }

    private void increaseValue2() {
        value2++;
        updateValue();
    }

    private void increaseValue3() {
        value3++;
        updateValue();
    }
    private void increaseValue4() {
        value4++;
        updateValue();
    }
    private void saveCarbonFootprintToDatabase(double carbonFootprint, String firstname) {


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
                                    double previousCarbonFootprint = row.getDouble("travel_carbon_footprint");
                                    int travelEntryWeight = row.getInt("travel_entry_weight");

                                    // Update the carbon footprint and entry weight
                                    double updatedCarbonFootprint = previousCarbonFootprint + carbonFootprint;
                                    int updatedtravelEntryWeight = travelEntryWeight + 1;

                                    // Convert the double value to String
                                    String updatedCarbonFootprintString = String.valueOf(updatedCarbonFootprint);

                                    // Construct the JSON object with the updated values
                                    JSONObject updatedRow = new JSONObject();
                                    updatedRow.put("travel_carbon_footprint", updatedCarbonFootprintString);
                                    updatedRow.put("travel_entry_weight", updatedtravelEntryWeight);


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
                                    newRow.put("travel_carbon_footprint", carbonFootprint);
                                    newRow.put("travel_entry_weight", 1);
                                    newRow.put("home_carbon_footprint", 0);
                                    newRow.put("home_entry_weight", 0);
                                    newRow.put("food_carbon_footprint", 0);
                                    newRow.put("food_entry_weight", 0);
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

                        Toast.makeText(getApplicationContext(), "not successful: ", Toast.LENGTH_LONG).show();

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
}