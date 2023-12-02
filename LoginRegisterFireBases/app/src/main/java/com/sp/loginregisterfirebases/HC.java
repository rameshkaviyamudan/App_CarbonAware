package com.sp.loginregisterfirebases;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HC extends AppCompatActivity {
    private TextView valueTextView1;
    private TextView valueTextView2;
    private TextView valueTextView3;
    private TextView valueTextView4;
    private Button minusButton1;
    private Button minusButton2;
    private Button minusButton3;
    private Button minusButton4;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationDrawerFragment drawerFragment;
    private DrawerLayout drawerLayout;
    private static final int CHILD_ACTIVITY_REQUEST_CODE = 1001; // You can use any integer value you want

    private Button plusButton1;
    private Button plusButton2;
    private Button plusButton3;
    private Button plusButton4;
    private Button resetButton, saveButton;

    private int value1 = 0;
    private int value2 = 0;
    private int value3 = 0;
    private int value4 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hc);
        getSupportActionBar().setDisplayShowTitleEnabled(true); // Show the title

        valueTextView1 = findViewById(R.id.acvalueTextView);
        valueTextView2 = findViewById(R.id.fanvalueTextView);
        valueTextView3 = findViewById(R.id.heatervalueTextView);
        valueTextView4 = findViewById(R.id.aircoolervalueTextView);
        minusButton1 = findViewById(R.id.phoneminusButton);
        minusButton2 = findViewById(R.id.tvminusButton);
        minusButton3 = findViewById(R.id.gameminusButton);
        minusButton4 = findViewById(R.id.laptopminusButton);
        plusButton1 = findViewById(R.id.phoneplusButton);
        plusButton2 = findViewById(R.id.tvplusButton);
        plusButton3 = findViewById(R.id.gameplusButton);
        plusButton4 = findViewById(R.id.laptopplusButton);
        saveButton = findViewById(R.id.hcsaveButton);
        // Enable the save button by default
        saveButton.setEnabled(true);

        updateValue();

        // Retrieve the saved flag value from SharedPreferences
        SharedPreferences sharedHCsavePreferences = getSharedPreferences("MyHCPrefs", MODE_PRIVATE);
        boolean HCcalculationsDone = sharedHCsavePreferences.getBoolean("calculationsDone", false);

        // Check if the calculations have already been done
        if (HCcalculationsDone) {
            // Disable the save button
            saveButton.setEnabled(false);
        }

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
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                showConfirmationDialog();
            }
        });
        // Find Button elements
        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetValues();
            }
        });
        // Load the BottomNavigationFragment
        BottomNavigationFragment bottomNavigationFragment = BottomNavigationFragment.newInstance("param1", "param2");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, bottomNavigationFragment);
        fragmentTransaction.commit();

    }
    private void resetValues() {
        value1 = 0;
        value2 = 0;
        value3 = 0;
        value4 = 0;
        updateValue();
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
        confirmationMessage.append("Air Conditioner: ").append(value1).append(" Hours").append("\n");
        confirmationMessage.append("Fan: ").append(value2).append(" Hours").append("\n");
        confirmationMessage.append("Air Purifier: ").append(value3).append(" Hours").append("\n");
        confirmationMessage.append("Air Cooler: ").append(value4).append(" Hours").append("\n");

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

    private double calculateCarbonFootprint() {
        // Calculate the carbon footprint based on the quantity of heating and cooling items
        // Assign a carbon footprint value to each item and calculate the total

        double carbonFootprint = 0.0;

        // Assign carbon footprint values based on the quantity of each item
        carbonFootprint += value1 * 2.0; // Example carbon footprint value for Aircon
        carbonFootprint += value2 * 0.5; // Example carbon footprint value for Fan
        carbonFootprint += value3 * 1.5; // Example carbon footprint value for Heater
        carbonFootprint += value4 * 1.0; // Example carbon footprint value for Air Cooler

        return carbonFootprint;
    }
    private void decreaseValue1() {
        value1 = Math.max(value1 - 1, 0);
        updateValue();
    }

    private void decreaseValue2() {
        value2 = Math.max(value2 - 1, 0);
        updateValue();
    }

    private void decreaseValue3() {
        value3 = Math.max(value3 - 1, 0);
        updateValue();
    }

    private void decreaseValue4() {
        value4 = Math.max(value4 - 1, 0);
        updateValue();
    }

    private void increaseValue1() {
        value1 = Math.min(value1 + 1, 24);
        updateValue();
    }

    private void increaseValue2() {
        value2 = Math.min(value2 + 1, 24);
        updateValue();
    }

    private void increaseValue3() {
        value3 = Math.min(value3 + 1, 24);
        updateValue();
    }

    private void increaseValue4() {
        value4 = Math.min(value4 + 1, 24);
        updateValue();
    }
    private void saveData() {
        // Retrieve the stored home value from SharedPreferences
        SharedPreferences sharedhomePreferences = getSharedPreferences("MyhomePrefs", MODE_PRIVATE);
        float homeCarbonFootprint = sharedhomePreferences.getFloat("homeCarbonFootprint", 0.0f);
        homeCarbonFootprint += calculateCarbonFootprint();

        // Store the updated value back into home SharedPreferences
        SharedPreferences.Editor homeeditor = sharedhomePreferences.edit();
        homeeditor.putFloat("homeCarbonFootprint", homeCarbonFootprint);
        homeeditor.apply();

        String carbonFootprintString = String.valueOf(homeCarbonFootprint);

        Toast.makeText(getApplicationContext(), carbonFootprintString, Toast.LENGTH_LONG).show();

        // Disable the save button
        saveButton.setEnabled(false);

        // Save the flag indicating that calculations have been done
        SharedPreferences sharedHCsavePreferences = getSharedPreferences("MyHCPrefs", MODE_PRIVATE);
        SharedPreferences.Editor HCeditor = sharedHCsavePreferences.edit();
        HCeditor.putBoolean("calculationsDone", true);
        HCeditor.apply();

        // Increment the activityCounter and store its value in SharedPreferences
        SharedPreferences sharedactPrefs = getSharedPreferences("MyactPrefs", MODE_PRIVATE);
        int activityCounter = sharedactPrefs.getInt("activityCounter", 0);
        activityCounter++;

        SharedPreferences.Editor acteditor = sharedactPrefs.edit();
        acteditor.putInt("activityCounter", activityCounter);
        acteditor.apply();
        // Pass the card identifier back to the main activity
        int cardIdentifier = 5; // Change this to the appropriate identifier for the Lighting card (1 in this case)
        Intent intent = new Intent();
        intent.putExtra("cardIdentifier", cardIdentifier);
        setResult(RESULT_OK, intent);


        // Finish the activity to go back
        finish();
    }

}