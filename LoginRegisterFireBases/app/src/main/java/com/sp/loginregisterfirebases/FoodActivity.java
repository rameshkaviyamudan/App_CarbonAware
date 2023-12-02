package com.sp.loginregisterfirebases;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

public class FoodActivity extends AppCompatActivity {

    private ImageView beefCheck, muttonCheck, seafoodCheck, eggsCheck, grainsCheck, dairyCheck, fruitsCheck, vegetablesCheck;
    private Button resetButton, saveButton;
    private int volleyResponseStatus;
    private static final int MAIN_ACTIVITY_REQUEST_CODE = 2002; // You can use any integer value you want

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diet);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // Hide the title
        getSupportActionBar().hide();
        // Retrieve the firstname from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyfirPrefs", MODE_PRIVATE);
        String firstname = sharedPreferences.getString("firstname", "");
        // Find ImageView elements
        beefCheck = findViewById(R.id.beef_check);
        muttonCheck = findViewById(R.id.mutton_check);
        seafoodCheck = findViewById(R.id.seafood_check);
        eggsCheck = findViewById(R.id.eggs_check);
        grainsCheck = findViewById(R.id.grains_check);
        dairyCheck = findViewById(R.id.dairy_check);
        fruitsCheck = findViewById(R.id.fruits_check);
        vegetablesCheck = findViewById(R.id.vegetables_check);

        // Set initial visibility of checkmarks
        beefCheck.setVisibility(View.INVISIBLE);
        muttonCheck.setVisibility(View.INVISIBLE);
        seafoodCheck.setVisibility(View.INVISIBLE);
        eggsCheck.setVisibility(View.INVISIBLE);
        grainsCheck.setVisibility(View.INVISIBLE);
        dairyCheck.setVisibility(View.INVISIBLE);
        fruitsCheck.setVisibility(View.INVISIBLE);
        vegetablesCheck.setVisibility(View.INVISIBLE);

        // Find Button elements
        resetButton = findViewById(R.id.reset);
        saveButton = findViewById(R.id.diet_save);


        // Load the BottomNavigationFragment
        BottomNavigationFragment bottomNavigationFragment = BottomNavigationFragment.newInstance("param1", "param2");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container2, bottomNavigationFragment);
        fragmentTransaction.commit();

        // Retrieve the saved flag value from SharedPreferences
        SharedPreferences sharedfosavePreferences = getSharedPreferences("MyfoPrefs", MODE_PRIVATE);
        boolean focalculationsDone = sharedfosavePreferences.getBoolean("calculationsDone", false);

        // Check if the calculations have already been done
        if (focalculationsDone) {
            // Disable the save button
            saveButton.setEnabled(false);
        }

        // Set click listeners for each card
        findViewById(R.id.beef_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCheckVisibility(beefCheck);
            }
        });

        findViewById(R.id.mutton_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCheckVisibility(muttonCheck);
            }
        });

        findViewById(R.id.seafood_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCheckVisibility(seafoodCheck);
            }
        });

        findViewById(R.id.eggs_crad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCheckVisibility(eggsCheck);
            }
        });

        findViewById(R.id.grains_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCheckVisibility(grainsCheck);
            }
        });

        findViewById(R.id.dairy_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCheckVisibility(dairyCheck);
            }
        });

        findViewById(R.id.fruits_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCheckVisibility(fruitsCheck);
            }
        });

        findViewById(R.id.vegetables_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleCheckVisibility(vegetablesCheck);
            }
        });


        // Set click listener for reset button
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset all checks to be invisible
                beefCheck.setVisibility(View.INVISIBLE);
                muttonCheck.setVisibility(View.INVISIBLE);
                seafoodCheck.setVisibility(View.INVISIBLE);
                eggsCheck.setVisibility(View.INVISIBLE);
                grainsCheck.setVisibility(View.INVISIBLE);
                dairyCheck.setVisibility(View.INVISIBLE);
                fruitsCheck.setVisibility(View.INVISIBLE);
                vegetablesCheck.setVisibility(View.INVISIBLE);
            }
        });

        // Set click listener for save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check which buttons are selected (visible checks)
                boolean isBeefSelected = beefCheck.getVisibility() == View.VISIBLE;
                boolean isMuttonSelected = muttonCheck.getVisibility() == View.VISIBLE;
                boolean isSeafoodSelected = seafoodCheck.getVisibility() == View.VISIBLE;
                boolean isEggsSelected = eggsCheck.getVisibility() == View.VISIBLE;
                boolean isGrainsSelected = grainsCheck.getVisibility() == View.VISIBLE;
                boolean isDairySelected = dairyCheck.getVisibility() == View.VISIBLE;
                boolean isFruitsSelected = fruitsCheck.getVisibility() == View.VISIBLE;
                boolean isVegetablesSelected = vegetablesCheck.getVisibility() == View.VISIBLE;

                // Create an instance of FoodSelection and set the fields
                FoodSelection foodSelection = new FoodSelection();
                foodSelection.setBeefSelected(isBeefSelected);
                foodSelection.setMuttonSelected(isMuttonSelected);
                foodSelection.setSeafoodSelected(isSeafoodSelected);
                foodSelection.setEggsSelected(isEggsSelected);
                foodSelection.setGrainsSelected(isGrainsSelected);
                foodSelection.setDairySelected(isDairySelected);
                foodSelection.setFruitsSelected(isFruitsSelected);
                foodSelection.setVegetablesSelected(isVegetablesSelected);
                // Show the confirmation dialog
                showFoodActivityConfirmationDialog(foodSelection);

            }
        });
    }
    private void showFoodActivityConfirmationDialog(FoodSelection foodSelection) {
        // Build the confirmation message with the selected items
        StringBuilder confirmationMessage = new StringBuilder("Confirm the following selections:\n");
        if (foodSelection.isBeefSelected()) {
            confirmationMessage.append("Beef\n");
        }
        if (foodSelection.isMuttonSelected()) {
            confirmationMessage.append("Mutton\n");
        }if (foodSelection.isSeafoodSelected()) {
            confirmationMessage.append("Seafood\n");
        }if (foodSelection.isEggsSelected()) {
            confirmationMessage.append("Eggs\n");
        }if (foodSelection.isGrainsSelected()) {
            confirmationMessage.append("Grains\n");
        }if (foodSelection.isDairySelected()) {
            confirmationMessage.append("Dairy\n");
        }if (foodSelection.isFruitsSelected()) {
            confirmationMessage.append("Fruits\n");
        }if (foodSelection.isVegetablesSelected()) {
            confirmationMessage.append("Vegetables\n");
        }
        // ... Include other food items as needed

        // Create and show the confirmation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Selections");
        builder.setMessage(confirmationMessage.toString());
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveFoodSelection(foodSelection); // Proceed with saving data
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
    private void saveFoodSelection(FoodSelection foodSelection) {
        // Calculate the carbon footprint based on the food selection
        double carbonFootprint = calculateCarbonFootprint(foodSelection);

        // Retrieve the previous total carbon footprint from SharedPreferences
        SharedPreferences sharedPreferences1 = getSharedPreferences("MyfoodPrefs", Context.MODE_PRIVATE);
        // Add the calculated carbon footprint to the previous total
        float updatedTotalCarbonFootprint =  (float) carbonFootprint;

        // Save the updated total carbon footprint back to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences1.edit();
        editor.putFloat("foodfootprint", updatedTotalCarbonFootprint);
        editor.apply();
// Retrieve the firstname from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyfirPrefs", MODE_PRIVATE);
        String firstname = sharedPreferences.getString("firstname", "");

        // Save the carbon footprint value to the database
        saveCarbonFootprintToDatabase(carbonFootprint, firstname);

        // Disable the save button
        saveButton.setEnabled(false);

        // Save the flag indicating that calculations have been done
        SharedPreferences sharedfosavePreferences = getSharedPreferences("MyfoPrefs", MODE_PRIVATE);
        SharedPreferences.Editor foeditor = sharedfosavePreferences.edit();
        foeditor.putBoolean("calculationsDone", true);
        foeditor.apply();
        int cardIdentifier = 1; // Change this to the appropriate identifier for the Lighting card (1 in this case)
        Intent intent = new Intent();
        intent.putExtra("cardIdentifier", cardIdentifier);
        setResult(RESULT_OK, intent);

        // Finish the activity to go back
        finish();
    }

    private void toggleCheckVisibility(ImageView checkImageView) {
        int visibility = checkImageView.getVisibility();
        if (visibility == View.VISIBLE) {
            checkImageView.setVisibility(View.INVISIBLE);
        } else {
            checkImageView.setVisibility(View.VISIBLE);
        }
    }

    private double calculateCarbonFootprint(FoodSelection foodSelection) {
        // Calculate the carbon footprint based on the food selection
        // Assign a carbon footprint value to each food item and calculate the total

        double carbonFootprint = 0.0;

        if (foodSelection.isBeefSelected()) {
            carbonFootprint += 20.0; // Example carbon footprint value for beef (higher impact)
        }
        if (foodSelection.isMuttonSelected()) {
            carbonFootprint += 15.0; // Example carbon footprint value for mutton
        }
        if (foodSelection.isSeafoodSelected()) {
            carbonFootprint += 10.0; // Example carbon footprint value for seafood
        }
        if (foodSelection.isEggsSelected()) {
            carbonFootprint += 5.0; // Example carbon footprint value for eggs
        }
        if (foodSelection.isGrainsSelected()) {
            carbonFootprint += 3.0; // Example carbon footprint value for grains
        }
        if (foodSelection.isDairySelected()) {
            carbonFootprint += 8.0; // Example carbon footprint value for dairy
        }
        if (foodSelection.isFruitsSelected()) {
            carbonFootprint += 2.0; // Example carbon footprint value for fruits
        }
        if (foodSelection.isVegetablesSelected()) {
            carbonFootprint += 1.0; // Example carbon footprint value for vegetables (lower impact)
        }

        return carbonFootprint;
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
                                    double previousCarbonFootprint = row.getDouble("food_carbon_footprint");
                                    int foodEntryWeight = row.getInt("food_entry_weight");

                                    // Update the carbon footprint and entry weight
                                    double updatedCarbonFootprint = previousCarbonFootprint + carbonFootprint;
                                    int updatedFoodEntryWeight = foodEntryWeight + 1;

                                    // Convert the double value to String
                                    String updatedCarbonFootprintString = String.valueOf(updatedCarbonFootprint);

                                    // Construct the JSON object with the updated values
                                    JSONObject updatedRow = new JSONObject();
                                    updatedRow.put("food_carbon_footprint", updatedCarbonFootprintString);
                                    updatedRow.put("food_entry_weight", updatedFoodEntryWeight);


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
                                    newRow.put("food_carbon_footprint", carbonFootprint);
                                    newRow.put("food_entry_weight", 1);
                                    newRow.put("home_carbon_footprint", 0);
                                    newRow.put("home_entry_weight", 0);
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
