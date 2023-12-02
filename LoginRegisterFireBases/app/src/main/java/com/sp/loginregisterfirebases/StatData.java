package com.sp.loginregisterfirebases;

import android.os.Parcel;
import android.os.Parcelable;

public class StatData implements Parcelable {
    private float homeCarbonFootprint;
    private float foodCarbonFootprint;
    private float travelCarbonFootprint;
    private float totalCarbonFootprint;

    protected StatData(Parcel in) {
        homeCarbonFootprint = in.readFloat();
        foodCarbonFootprint = in.readFloat();
        travelCarbonFootprint = in.readFloat();
        totalCarbonFootprint = in.readFloat();
    }

    public static final Creator<StatData> CREATOR = new Creator<StatData>() {
        @Override
        public StatData createFromParcel(Parcel in) {
            return new StatData(in);
        }

        @Override
        public StatData[] newArray(int size) {
            return new StatData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(homeCarbonFootprint);
        dest.writeFloat(foodCarbonFootprint);
        dest.writeFloat(travelCarbonFootprint);
        dest.writeFloat(totalCarbonFootprint);
    }

    public StatData(float homeCarbonFootprint, float foodCarbonFootprint, float travelCarbonFootprint, float totalCarbonFootprint) {
        this.homeCarbonFootprint = homeCarbonFootprint;
        this.foodCarbonFootprint = foodCarbonFootprint;
        this.travelCarbonFootprint = travelCarbonFootprint;
        this.totalCarbonFootprint = totalCarbonFootprint;
    }

    public float getHomeCarbonFootprint() {
        return homeCarbonFootprint;
    }

    public void setHomeCarbonFootprint(float homeCarbonFootprint) {
        this.homeCarbonFootprint = homeCarbonFootprint;
    }

    public float getFoodCarbonFootprint() {
        return foodCarbonFootprint;
    }

    public void setFoodCarbonFootprint(float foodCarbonFootprint) {
        this.foodCarbonFootprint = foodCarbonFootprint;
    }

    public float getTravelCarbonFootprint() {
        return travelCarbonFootprint;
    }

    public void setTravelCarbonFootprint(float travelCarbonFootprint) {
        this.travelCarbonFootprint = travelCarbonFootprint;
    }

    public float getTotalCarbonFootprint() {
        return totalCarbonFootprint;
    }

    public void setTotalCarbonFootprint(float totalCarbonFootprint) {
        this.totalCarbonFootprint = totalCarbonFootprint;
    }
    // This is just an example of a method that provides sample StatData for testing
    // This is just an example of a method that provides sample StatData for testing
    private StatData getStatData() {
        float homeCarbonFootprint = this.homeCarbonFootprint;
        float foodCarbonFootprint =  this.foodCarbonFootprint;
        float travelCarbonFootprint =  this.travelCarbonFootprint;
        float totalCarbonFootprint = homeCarbonFootprint + foodCarbonFootprint + travelCarbonFootprint;

        return new StatData(homeCarbonFootprint, foodCarbonFootprint, travelCarbonFootprint, totalCarbonFootprint);
    }


}
