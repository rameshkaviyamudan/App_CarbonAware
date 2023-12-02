package com.sp.loginregisterfirebases;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button calculatebutton;
    private int volleyResponseStatus;
    private static final int MAIN_ACTIVITY_REQUEST_CODE = 2002; // You can use any integer value you want
    private boolean isLHomeProcessed = false;
    private boolean isTravelProcessed = false;
    private boolean isDietProcessed = false;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView drawerIcon;
    private ImageView closeDrawerIcon;
    private View sidebarLayout;
    private View overlayView;

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationDrawerFragment drawerFragment;
    private AboutFragment aboutFragment;
    CardView travelcard;
    CardView home_utility;
    CardView dietCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        aboutFragment = new AboutFragment();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main4);
        calculatebutton = findViewById(R.id.calculatebutton);
        // Initialize the NavigationView after setting the content view
        navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        // Find the cards inside the navigation header view
        CardView aboutCard = headerView.findViewById(R.id.about);
        CardView websiteCard = headerView.findViewById(R.id.website);
        CardView logoutCard = headerView.findViewById(R.id.logout);
        CardView exitCard = headerView.findViewById(R.id.exit);
        ImageView qrImageView = headerView.findViewById(R.id.qr);
        overlayView = findViewById(R.id.overlay_view); // Assuming you have an overlay view in your XML layout
        overlayView.setVisibility(View.GONE); // Initially hide the overlay

        aboutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!aboutFragment.isAdded()) {
                    overlayView.setVisibility(View.VISIBLE); // Show the overlay
                    AboutFragment aboutFragment = new AboutFragment();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container2, aboutFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    // AboutFragment is already open, show a message or handle as needed
                    Toast.makeText(getApplicationContext(), "About page is already open", Toast.LENGTH_SHORT).show();
                }
            }
        });
        overlayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do nothing
            }
        });

        websiteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlayView.setVisibility(View.INVISIBLE); // Show the overlay

                WebViewFragment webViewFragment = new WebViewFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container2, webViewFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        logoutCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        exitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle "Exit" card click
                // Implement your logic here
                finish();
            }
        });
        qrImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlayView.setVisibility(View.INVISIBLE); // Show the overlay
                WebViewFragment2 webViewFragment = new WebViewFragment2();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container2, webViewFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        // Retrieve the firstname from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyfirPrefs", MODE_PRIVATE);
        String firstname = sharedPreferences.getString("firstname", "");

        // Set up the Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerFragment = new NavigationDrawerFragment();

        // Replace the FrameLayout with the Navigation Drawer fragment
        //getSupportFragmentManager().beginTransaction()
          //      .replace(R.id.fragment_container2, drawerFragment)
            //    .commit();

        // Set up the ActionBarDrawerToggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Enable the navigation icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        navigationView = findViewById(R.id.navigation_view);

        // Load the BottomNavigationFragment
        BottomNavigationFragment bottomNavigationFragment = BottomNavigationFragment.newInstance("param1", "param2");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, bottomNavigationFragment);
        fragmentTransaction.commit();



        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Button resetButton = findViewById(R.id.resetmain);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Enable the Calculate button and change its color back to dark green
                calculatebutton.setEnabled(true);
                calculatebutton.setBackgroundColor(Color.parseColor("#007E33")); // Dark green color
                // !Save the flag indicating that calculations have been done
                SharedPreferences sharedLAsavePreferences = getSharedPreferences("MyLAPrefs", MODE_PRIVATE);
                SharedPreferences.Editor LAeditor = sharedLAsavePreferences.edit();
                LAeditor.putBoolean("calculationsDone", false);
                LAeditor.apply();
                // !Save the flag indicating that calculations have been done
                SharedPreferences sharedKAsavePreferences = getSharedPreferences("MyKAPrefs", MODE_PRIVATE);
                SharedPreferences.Editor KAeditor = sharedKAsavePreferences.edit();
                KAeditor.putBoolean("calculationsDone", false);
                KAeditor.apply();
                // !Save the flag indicating that calculations have been done
                SharedPreferences sharedHCsavePreferences = getSharedPreferences("MyHCPrefs", MODE_PRIVATE);
                SharedPreferences.Editor HCeditor = sharedHCsavePreferences.edit();
                HCeditor.putBoolean("calculationsDone", false);
                HCeditor.apply();
                // !Save the flag indicating that calculations have been done
                SharedPreferences sharedlisavePreferences = getSharedPreferences("MyliPrefs", MODE_PRIVATE);
                SharedPreferences.Editor lieditor = sharedlisavePreferences.edit();
                lieditor.putBoolean("calculationsDone", false);
                lieditor.apply();
                // !Save the flag indicating that calculations have been done
                SharedPreferences sharedmsavePreferences = getSharedPreferences("MymPrefs", MODE_PRIVATE);
                SharedPreferences.Editor meditor = sharedmsavePreferences.edit();
                meditor.putBoolean("calculationsDone", false);
                meditor.apply();

                // !Save the flag indicating that calculations have been done
                SharedPreferences sharedelecsavePreferences = getSharedPreferences("MyElecPrefs", MODE_PRIVATE);
                SharedPreferences.Editor eleceditor = sharedelecsavePreferences.edit();
                eleceditor.putBoolean("calculationsDone", false);
                eleceditor.apply();
                // !Save the flag indicating that calculations have been done
                SharedPreferences sharedfosavePreferences = getSharedPreferences("MyfoPrefs", MODE_PRIVATE);
                SharedPreferences.Editor foeditor = sharedfosavePreferences.edit();
                foeditor.putBoolean("calculationsDone", false);
                foeditor.apply();
                // !Save the flag indicating that calculations have been done
                SharedPreferences sharedtrsavePreferences = getSharedPreferences("MytrPrefs", MODE_PRIVATE);
                SharedPreferences.Editor treditor = sharedtrsavePreferences.edit();
                treditor.putBoolean("calculationsDone", false);
                treditor.apply();

            }
        });

        dietCard = findViewById(R.id.dietcard);
        home_utility = findViewById(R.id.home_utility);
        travelcard = findViewById(R.id.travelcard);

        dietCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open FoodActivity
                Intent intent = new Intent(MainActivity.this, FoodActivity.class);
                intent.putExtra("cardIdentifier", 1); // Pass the card identifier (1 for lightingCard)
                startActivityForResult(intent, MAIN_ACTIVITY_REQUEST_CODE);            }
        });
        home_utility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open FoodActivity
                Intent intent = new Intent(MainActivity.this, House_utility.class);
                intent.putExtra("cardIdentifier", 2); // Pass the card identifier (1 for lightingCard)
                startActivityForResult(intent, MAIN_ACTIVITY_REQUEST_CODE);             }
        });
        travelcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open FoodActivity
                Intent intent = new Intent(MainActivity.this, Travel.class);
                intent.putExtra("cardIdentifier", 3); // Pass the card identifier (1 for lightingCard)
                startActivityForResult(intent, MAIN_ACTIVITY_REQUEST_CODE);             }
        });
        calculatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Disable the Calculate button and change its color to grayscale
                calculatebutton.setEnabled(false);
                calculatebutton.setBackgroundColor(Color.GRAY);

                Calculatefootprint(firstname);

            }
        });
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void Calculatefootprint(String firstname) {


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

                                    // !Save the flag indicating that calculations have been done
                                    SharedPreferences sharedLAsavePreferences = getSharedPreferences("MyLAPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor LAeditor = sharedLAsavePreferences.edit();
                                    LAeditor.putBoolean("calculationsDone", false);
                                    LAeditor.apply();
                                    // !Save the flag indicating that calculations have been done
                                    SharedPreferences sharedKAsavePreferences = getSharedPreferences("MyKAPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor KAeditor = sharedKAsavePreferences.edit();
                                    KAeditor.putBoolean("calculationsDone", false);
                                    KAeditor.apply();
                                    // !Save the flag indicating that calculations have been done
                                    SharedPreferences sharedHCsavePreferences = getSharedPreferences("MyHCPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor HCeditor = sharedHCsavePreferences.edit();
                                    HCeditor.putBoolean("calculationsDone", false);
                                    HCeditor.apply();
                                    // !Save the flag indicating that calculations have been done
                                    SharedPreferences sharedlisavePreferences = getSharedPreferences("MyliPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor lieditor = sharedlisavePreferences.edit();
                                    lieditor.putBoolean("calculationsDone", false);
                                    lieditor.apply();
                                    // !Save the flag indicating that calculations have been done
                                    SharedPreferences sharedmsavePreferences = getSharedPreferences("MymPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor meditor = sharedmsavePreferences.edit();
                                    meditor.putBoolean("calculationsDone", false);
                                    meditor.apply();

                                    // !Save the flag indicating that calculations have been done
                                    SharedPreferences sharedelecsavePreferences = getSharedPreferences("MyElecPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor eleceditor = sharedelecsavePreferences.edit();
                                    eleceditor.putBoolean("calculationsDone", false);
                                    eleceditor.apply();
                                    // !Save the flag indicating that calculations have been done
                                    SharedPreferences sharedfosavePreferences = getSharedPreferences("MyfoPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor foeditor = sharedfosavePreferences.edit();
                                    foeditor.putBoolean("calculationsDone", false);
                                    foeditor.apply();
                                    // !Save the flag indicating that calculations have been done
                                    SharedPreferences sharedtrsavePreferences = getSharedPreferences("MytrPrefs", MODE_PRIVATE);
                                    SharedPreferences.Editor treditor = sharedtrsavePreferences.edit();
                                    treditor.putBoolean("calculationsDone", false);
                                    treditor.apply();


                                    // Row with the firstname exists, update the values
                                    JSONObject row = data.getJSONObject(0);
                                    int homeEntryWeight = row.getInt("home_entry_weight");
                                    double homecarbonfootprint = row.getDouble("home_carbon_footprint");
                                    int foodEntryWeight = row.getInt("food_entry_weight");
                                    double foodcarbonfootprint = row.getDouble("food_carbon_footprint");
                                    int travelEntryWeight = row.getInt("travel_entry_weight");
                                    double travelcarbonfootprint = row.getDouble("travel_carbon_footprint");
                                    double totalcarbonfootprint ;

                                    if (homeEntryWeight == foodEntryWeight && foodEntryWeight == travelEntryWeight) {
                                        // All entry_weights are equal
                                        double total = homecarbonfootprint + foodcarbonfootprint + travelcarbonfootprint;
                                        totalcarbonfootprint= (total/foodEntryWeight) * 30.0*0.8;
                                    } else {
                                        // Entry weights are not equal
                                        double total = (homecarbonfootprint / homeEntryWeight) + (foodcarbonfootprint / foodEntryWeight) + (travelcarbonfootprint / travelEntryWeight);
                                        totalcarbonfootprint= total * 30.0*0.8;

                                    }
                                    SharedPreferences sharedPreferences = getSharedPreferences("MytotalPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    // Store the totalcarbonfootprint value in SharedPreferences
                                    editor.putFloat("totalcarbonfootprint", (float) totalcarbonfootprint);
                                    editor.apply();
                                    // Construct the JSON object with the updated values
                                    JSONObject updatedRow = new JSONObject();
                                    updatedRow.put("total_carbon_footprint", totalcarbonfootprint);
                                    String totalcarbonfootprintString= String.valueOf(totalcarbonfootprint);

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
                                                    Toast.makeText(getApplicationContext(), totalcarbonfootprintString, Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(getApplicationContext(), "No record has been Found please Enter record to View your carbon footprint", Toast.LENGTH_LONG).show();

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MAIN_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                int cardIdentifier = data.getIntExtra("cardIdentifier", -1);
                if (cardIdentifier != -1) {
                    // Update the corresponding card's processing status
                    switch (cardIdentifier) {
                        case 1:
                            isDietProcessed = true;
                            dietCard.setCardBackgroundColor(Color.GRAY); // Change the card's background color to gray
                            break;
                        case 2:
                            isLHomeProcessed = true;
                            home_utility.setCardBackgroundColor(Color.GRAY); // Change the card's background color to gray
                            break;
                        case 3:
                            isTravelProcessed = true;
                            travelcard.setCardBackgroundColor(Color.GRAY); // Change the card's background color to gray
                            break;
                        // Handle other card identifiers similarly if needed
                        // Re-enable the Calculate button and change its color back to dark green

                    }
                    calculatebutton.setEnabled(true);
                    calculatebutton.setBackgroundColor(Color.parseColor("#007E33")); // Dark green color

                }
            }
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent); // Set the new intent in case you need to access it later
    }
}
