package com.sp.loginregisterfirebases;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SpeedometerView extends View {
    private Paint dialPaint;
    private Paint valuePaint;
    private float maxValue = 10000;
    private float value;
    private int borderWidth = 20; // The thickness of the filled portion from the border
    private List<Section> sections = new ArrayList<>();
    private String valueText = "";
    private String headerText = "Total Footprint"; // The header text to be displayed
    private int defaultColor = Color.GRAY;
    private int maxValueColor = Color.BLUE;
    private int filledColor = Color.BLUE; // Color for the filled portion

    public void setSections(List<Section> sections) {
        this.sections = sections;
        invalidate();
    }

    public SpeedometerView(Context context) {
        super(context);
        init();
    }

    public SpeedometerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        dialPaint = new Paint();
        dialPaint.setColor(Color.LTGRAY);
        dialPaint.setStyle(Paint.Style.STROKE);
        dialPaint.setStrokeWidth(20);

        valuePaint = new Paint();
        valuePaint.setColor(Color.BLUE);
        valuePaint.setStyle(Paint.Style.STROKE);
        valuePaint.setStrokeWidth(20);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Calculate the radius of the semi-circle (half of the minimum dimension)
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        int radius = 400;

        // Calculate the center coordinates of the semi-circle
        int centerX = viewWidth / 2;
        int centerY = viewHeight;

        // Calculate the sweep angle for the blue filled portion
        float currentValue = value;
        float sweepAngle = 180 * (value / maxValue);

        // Draw the grey default section (full semi-circle)
        Paint defaultPaint = new Paint();
        defaultPaint.setColor(defaultColor);
        defaultPaint.setStyle(Paint.Style.STROKE);
        defaultPaint.setStrokeWidth(borderWidth);
// Draw the header text on top of the semi-circle
        Paint headerTextPaint = new Paint();
        headerTextPaint.setColor(Color.BLACK);
        headerTextPaint.setTextSize(60); // Adjust the text size as needed

        Rect headerTextBounds = new Rect();
        headerTextPaint.getTextBounds(headerText, 0, headerText.length(), headerTextBounds);
        int headerTextX = centerX - headerTextBounds.width() / 2;
        int headerTextY = centerY - radius + borderWidth - headerTextBounds.height(); // Above the center
        canvas.drawText(headerText, headerTextX, headerTextY, headerTextPaint);

        RectF defaultRectF = new RectF(centerX - radius + borderWidth, centerY - radius + borderWidth,
                centerX + radius - borderWidth, centerY + radius - borderWidth);
        canvas.drawArc(defaultRectF, 180, 180, false, defaultPaint);
// Calculate the colors for the gradient based on the current value
        int startColor;
        int endColor;

        if (currentValue <= maxValue / 2) {
            // Green to Yellow gradient
            startColor = Color.GREEN;
            endColor = Color.YELLOW;
        } else {
            // Yellow to Red gradient
            startColor = Color.YELLOW;
            endColor = Color.RED;
        }

        // Draw the filled portion with custom color gradient
        int[] colors = {startColor, endColor};
        float[] positions = {0.0f, 1.0f - (sweepAngle / 180.0f)};
        Paint gradientPaint = new Paint();
        Shader shader = new LinearGradient(
                centerX, centerY - radius + borderWidth,
                centerX, centerY + radius - borderWidth,
                colors, positions, Shader.TileMode.CLAMP);
        gradientPaint.setShader(shader);
        gradientPaint.setStyle(Paint.Style.STROKE);
        gradientPaint.setStrokeWidth(borderWidth);

        RectF gradientRectF = new RectF(centerX - radius + borderWidth, centerY - radius + borderWidth,
                centerX + radius - borderWidth, centerY + radius - borderWidth);
        canvas.drawArc(gradientRectF, 180, sweepAngle, false, gradientPaint);

        // Draw the value text slightly above the center of the semi-circle
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(90); // Adjust the text size as needed
// Convert the float value to a String and set it as the valueText
        String valueText = String.valueOf(value);
        Rect textBounds = new Rect();
        textPaint.getTextBounds(valueText, 0, valueText.length(), textBounds);
        int textX = centerX - textBounds.width() / 2;
        int textY = centerY - radius/2 + borderWidth + textBounds.height(); // Slightly above the center
        canvas.drawText(valueText, textX, textY, textPaint);

        // Draw other elements of the speedometer (e.g., needle, labels, etc.) as needed
        // ...
    }

    public void setValue(float value) {
            this.value = value;
            invalidate(); // Redraw the view to update the value circle

    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }

    public void setValueText(String valueText) {
        this.valueText = String.valueOf(value);
        invalidate(); // Request a redraw to update the displayed value
    }
}