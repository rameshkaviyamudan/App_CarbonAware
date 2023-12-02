package com.sp.loginregisterfirebases;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
// Add this import statement
import android.graphics.Bitmap;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class Stats extends AppCompatActivity {
    private ViewPager2 viewpager;
    private List<StatData> statList;
    private int volleyResponseStatus;
    private int currentCategoryIndex = 0;
    private SpeedometerView speedometerView;
    private Handler handler;
    private Runnable autoScrollRunnable;
    private boolean isAutoScrolling = false;

    private final long AUTO_SCROLL_INTERVAL = 300; // Set the interval for auto-scrolling (in milliseconds)
    List<Fragment> fragments = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_activity);
        getSupportActionBar().setDisplayShowTitleEnabled(true); // Show the title
// Assuming you have an ImageButton with ID 'download' in your layout
        ImageView downloadImage = findViewById(R.id.downloadimage);


        // Initialize the handler and auto-scroll runnable
        handler = new Handler();
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                int nextItem = viewpager.getCurrentItem() + 1;
                if (nextItem >= fragments.size()) {
                    // If at the last page, stop the auto-scrolling
                    handler.removeCallbacks(this);
                } else {
                    viewpager.setCurrentItem(nextItem, true);
                    // Schedule the next auto-scroll after the defined interval
                    handler.postDelayed(this, AUTO_SCROLL_INTERVAL);
                }
            }
        };


        // Initialize the SpeedometerView
        speedometerView = findViewById(R.id.speedometer);
// Assuming you have a SpeedometerView instance named "speedometerView"

        List<Section> sections = new ArrayList<>();
        sections.add(new Section(25f, Color.BLUE, 20));
        sections.add(new Section(40f, Color.GRAY, 15));
        sections.add(new Section(35f, Color.RED, 10));

        speedometerView.setSections(sections);

// Set the maxValue for your specific use case
        //speedometerView.setMaxValue(3000f);



        viewpager = findViewById(R.id.viewpager);
        // Create the GraphPagerAdapter with this activity as the fragment host
        GraphPagerAdapter graphPagerAdapter = new GraphPagerAdapter(this);
        viewpager.setAdapter(graphPagerAdapter);

        statList = new ArrayList<>();
        // Set up the ViewPager2.OnPageChangeCallback
        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                // Update currentCategoryIndex based on the selected page position
                if (position == 0) {
                    currentCategoryIndex = 1;
                    //Toast.makeText(getApplicationContext(), " evvv", Toast.LENGTH_SHORT).show();

                } else if (position == 1) {
                    currentCategoryIndex = 0;
                    //Toast.makeText(getApplicationContext(), "Error updating row", Toast.LENGTH_SHORT).show();

                }else if (position == 3) {
                    // When the 4th view is selected, stop scrolling by removing the callbacks
                    handler.removeCallbacks(autoScrollRunnable);
                }

            }
        });
        retrieveStatData();
        downloadImage.setOnClickListener(new View.OnClickListener() {            @Override
            public void onClick(View view) {
            startPDFDownload();

        }
        });

        // Load the BottomNavigationFragment
        BottomNavigationFragment bottomNavigationFragment = BottomNavigationFragment.newInstance("param1", "param2");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, bottomNavigationFragment);
        fragmentTransaction.commit();

    }
    private ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            currentCategoryIndex = position;
        }
    };
    private String generateUniqueFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS"); // Format: YearMonthDayHourMinuteSecondMillisecond
        String timestamp = sdf.format(new Date());

        Random random = new Random();
        int randomNumber = random.nextInt(10000); // Generate a random number between 0 and 9999

        return "stats" + timestamp + randomNumber;
    }
    private void openPdfWithFileProvider(String filePath) {
        File file = new File(filePath);
        Uri fileUri = FileProvider.getUriForFile(this, "com.sp.loginregisterfirebases.fileprovider", file);

        // Create an Intent to open the PDF file with a PDF viewer app
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        // Check if there's an app available to open the PDF
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // If no PDF viewer app is available, show a toast message
            Toast.makeText(getApplicationContext(), "No PDF viewer app found", Toast.LENGTH_SHORT).show();
        }
    }
    private void retrieveStatData() {
        viewpager.registerOnPageChangeCallback(onPageChangeCallback);

        SharedPreferences sharedPreferences = getSharedPreferences("MyfirPrefs", MODE_PRIVATE);
        String firstname = sharedPreferences.getString("firstname", "");

        String params = "?where={\"firstname\": {\"$eq\":[\"" + firstname + "\"]}}";
        String url = VolleyHelper.carburl + params;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int count = response.getInt("count");
                            JSONArray data = response.getJSONArray("data");

                            if (count > 0) {
                                JSONObject row = data.getJSONObject(0);

                                // Retrieve your stat data from the database
                                float homeCarbonFootprint = (float) row.getDouble("home_carbon_footprint");
                                float foodCarbonFootprint = (float) row.getDouble("food_carbon_footprint");
                                float travelCarbonFootprint = (float) row.getDouble("travel_carbon_footprint");
                                float totalCarbonFootprint = (float) row.getDouble("total_carbon_footprint");


                                StatData userStatData = new StatData(homeCarbonFootprint, foodCarbonFootprint, travelCarbonFootprint, totalCarbonFootprint);

// Pass the userStatData object to the GraphFragments
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("userStatData", userStatData);;

                                GraphFragment1 fragment1 = new GraphFragment1();
                                fragment1.setArguments(bundle);

                                GraphFragment2 fragment2 = new GraphFragment2();
                                fragment2.setArguments(bundle);

                                GraphFragment3 fragment3 = new GraphFragment3();
                                fragment3.setArguments(bundle);

                                GraphFragment4 fragment4 = new GraphFragment4();
                                fragment4.setArguments(bundle);
                                GraphFragment5 fragment5 = new GraphFragment5();
                                fragment5.setArguments(bundle);


                                fragments.add(fragment1);
                                fragments.add(fragment2);
                                fragments.add(fragment3);
                                fragments.add(fragment4);
                                fragments.add(fragment5);

                                // Pass the FragmentManager and Lifecycle to the adapter
                                CustomFragmentStateAdapter fragmentStateAdapter = new CustomFragmentStateAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
                                viewpager.setAdapter(fragmentStateAdapter);


                                // Update the value of the SpeedometerView
                                speedometerView.setValue((int) totalCarbonFootprint);
                            } else {
                                Toast.makeText(getApplicationContext(), "No record found. Please enter records to view your carbon footprint.", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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

        queue.add(jsonObjectRequest);
    }
    private void generatePDFWithGraphs(ArrayList<Fragment> fragments, String fileName) {
        Document document = new Document();

        try {
            File directory = new File(getExternalFilesDir(null), "my_pdf_directory");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            // Provide the file path and name where you want to save the PDF
            String filePath = getExternalFilesDir(null) + "/my_pdf_directory/" + fileName + ".pdf";
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath)); // Add this line
// Set the event handler to add headers and footers
            CustomPdfPageEventHelper eventHelper = new CustomPdfPageEventHelper();
            writer.setPageEvent(eventHelper); // Remove this line
            // Open the document for writing
            document.open();

            // Get the current date and time
            String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());


            // Iterate through all the fragments and add their content to the document
            for (Fragment fragment : fragments) {
                PdfPTable table = new PdfPTable(1);
                table.setWidthPercentage(100);
                // Create a new page for each fragment
                document.newPage();

                if (fragment instanceof GraphFragment1) {
                    GraphFragment1 graphFragment1 = (GraphFragment1) fragment;
                    // Capture the BarChart view as a bitmap
                    BarChart barChart = graphFragment1.getBarChart();

                    // Check if the BarChart is null, show a log message
                    if (barChart == null) {
                        Log.e("Stats", "BarChart is null in GraphFragment1");
                        continue; // Skip to the next fragment
                    }
                    // Set minimum scale values for the BarChart
                    barChart.setScaleMinima(1f, 1f); // Set the minimum x and y scale to 1

                    // Capture the BarChart view as a bitmap
                    Bitmap barChartBitmap = getBitmapFromView(graphFragment1.getBarChart());
                    int targetWidth = 500; // Set the width you desire
                    int targetHeight = 500; // Set the height you desire

                    Bitmap resizedBitmap = getResizedBitmap(barChartBitmap, targetWidth, targetHeight);

                    // Add the bitmap to the PDF document
                    Image image = Image.getInstance(getBytesFromBitmap(resizedBitmap));
                    // Center-align the image in the PDF
                    image.setAlignment(Image.ALIGN_CENTER);
                    // Create a table for additional details
                    table.addCell(createCellForGraphFragment(currentDate, "Here's your Total stats."));
                    document.add(image);

                    Log.d("PDFGeneration", "BarChart added to PDF");
                } else if (fragment instanceof GraphFragment2) {
                    GraphFragment2 graphFragment2 = (GraphFragment2) fragment;
                    // Capture the PieChart view as a bitmap
                    PieChart pieChart = graphFragment2.getPieChart();

                    // Check if the PieChart is null, show a log message
                    if (pieChart == null) {
                        Log.e("Stats", "PieChart is null in GraphFragment2");
                        continue; // Skip to the next fragment
                    }

                    // Capture the PieChart view as a bitmap
                    Bitmap pieChartBitmap = getBitmapFromView(graphFragment2.getPieChart());
                    int targetWidth = 700; // Set the width you desire
                    int targetHeight = 500; // Set the height you desire

                    Bitmap resizedBitmap = getResizedBitmap(pieChartBitmap, targetWidth, targetHeight);

                    // Add the bitmap to the PDF document
                    Image image = Image.getInstance(getBytesFromBitmap(resizedBitmap));
                    // Center-align the image in the PDF
                    image.setAlignment(Image.ALIGN_CENTER);
                    // Create a table for additional details
                    table.addCell(createCellForGraphFragment(currentDate, "Here's your Food stats."));

                    document.add(image);
                } else if (fragment instanceof GraphFragment3) {
                    GraphFragment3 graphFragment3 = (GraphFragment3) fragment;

                    // Capture the BarChart view as a bitmap
                    Bitmap barChartBitmap = getBitmapFromView(graphFragment3.getBarChart());
                    int targetWidth = 500; // Set the width you desire
                    int targetHeight = 500; // Set the height you desire

                    Bitmap resizedBitmap = getResizedBitmap(barChartBitmap, targetWidth, targetHeight);

                    // Add the bitmap to the PDF document
                    Image image = Image.getInstance(getBytesFromBitmap(resizedBitmap));
                    // Center-align the image in the PDF
                    image.setAlignment(Image.ALIGN_CENTER);
                    // Create a table for additional details
                    table.addCell(createCellForGraphFragment(currentDate, "Here's your Home stats."));

                    document.add(image);
                } else if (fragment instanceof GraphFragment4) {
                    GraphFragment4 graphFragment4 = (GraphFragment4) fragment;

                    // Capture the RadarChart view as a bitmap
                    Bitmap radarChartBitmap = getBitmapFromView(graphFragment4.getRadarChart());
// Resize the bitmap to a specific width and height
                    int targetWidth = 650; // Set the width you desire
                    int targetHeight = 500; // Set the height you desire

                    Bitmap resizedBitmap = getResizedBitmap(radarChartBitmap, targetWidth, targetHeight);

                    // Add the bitmap to the PDF document
                    Image image = Image.getInstance(getBytesFromBitmap(resizedBitmap));
                    // Center-align the image in the PDF
                    image.setAlignment(Image.ALIGN_CENTER);
                    // Create a table for additional details
                    table.addCell(createCellForGraphFragment(currentDate, "Here's your Travel stats."));

                    document.add(image);
                }

                // Add other views from other fragments similarly as needed

                // You can add spacing between graphs/views if required
                // document.add(new Paragraph("")); // Add some spacing between graphs


                // Add the table to the document
                document.add(table);
            }

            // Close the document
            document.close();

            // Show a toast indicating that the PDF has been generated successfully
            Toast.makeText(getApplicationContext(), "PDF generated successfully", Toast.LENGTH_SHORT).show();

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error generating PDF", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error reading/writing bitmap", Toast.LENGTH_SHORT).show();
        }
    }
    // Function to combine two bitmaps side by side
    private Bitmap combineBitmaps(Bitmap bitmap1, Bitmap bitmap2) {
        int width = bitmap1.getWidth() + bitmap2.getWidth();
        int height = Math.max(bitmap1.getHeight(), bitmap2.getHeight());
        Bitmap combinedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(combinedBitmap);
        canvas.drawBitmap(bitmap1, 0, 0, null);
        canvas.drawBitmap(bitmap2, bitmap1.getWidth(), 0, null);

        return combinedBitmap;
    }
    private byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
    // Helper method to create a PdfPCell with the given text
    private PdfPCell createCellForGraphFragment(String currentDate, String details) {
        PdfPCell cell = new PdfPCell();
        cell.addElement(new Phrase("Your carbon footprint stats as of " + currentDate, new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
        cell.addElement(new Phrase(details, new Font(Font.FontFamily.HELVETICA, 10)));
        return cell;
    }
    private Bitmap getResizedBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
}
    @Override
    protected void onResume() {
        super.onResume();
        // Start auto-scrolling when the activity is in the foreground
        handler.postDelayed(autoScrollRunnable, AUTO_SCROLL_INTERVAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop auto-scrolling when the activity is in the background
        handler.removeCallbacks(autoScrollRunnable);
    }
    private void autoScroll() {
        int currentItem = viewpager.getCurrentItem();
        if (currentItem >= fragments.size() - 1) {
            // Stop auto-scrolling when the last view is reached
            isAutoScrolling = false;
            handler.removeCallbacks(autoScrollRunnable);
            startPDFDownload();
        } else {
            // Scroll to the next item
            viewpager.setCurrentItem(currentItem + 1, true);
            // Schedule the next auto-scroll after the defined interval
            handler.postDelayed(autoScrollRunnable, AUTO_SCROLL_INTERVAL);

        }
    }
private void startPDFDownload(){
    // Generate a unique file name for the PDF
    String uniqueFileName = generateUniqueFileName();
    Toast.makeText(getApplicationContext(), "Error ascascasc/writing bitmap", Toast.LENGTH_SHORT).show();

    // Generate the PDF with graphs and views using the unique file name
    generatePDFWithGraphs(new ArrayList<>(fragments), uniqueFileName);
    File directory = new File(getExternalFilesDir(null), "my_pdf_directory");
    if (!directory.exists()) {
        directory.mkdirs();
    }
    // Provide the file path and name where you want to save the PDF
    String filePath = getExternalFilesDir(null) + "/my_pdf_directory/" + uniqueFileName + ".pdf";
    File pdfFile = new File(filePath);

    if (pdfFile.exists()) {
        openPdfWithFileProvider(filePath);

    } else {
        Toast.makeText(getApplicationContext(), "The file"+uniqueFileName+" does not exist! ", Toast.LENGTH_SHORT).show();
    }
}
}
