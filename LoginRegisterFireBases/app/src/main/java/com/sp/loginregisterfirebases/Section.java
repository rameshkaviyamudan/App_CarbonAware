package com.sp.loginregisterfirebases;

public class Section {
    private float value; // The value for this section
    private int color; // The color of this section
    private int borderWidth; // The border thickness for this section

    public Section(float value, int color, int borderWidth) {
        this.value = value;
        this.color = color;
        this.borderWidth = borderWidth;
    }

    // Add getters and setters as needed\

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }
    // ...
}
