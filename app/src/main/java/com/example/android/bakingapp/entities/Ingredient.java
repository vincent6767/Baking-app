package com.example.android.bakingapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public class Ingredient implements Parcelable {
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
    private Float quantity;
    private String measure;
    @SerializedName("ingredient")
    private String name;
    // Required by Parcelable
    private Ingredient(Parcel in) {
        this.quantity = in.readFloat();
        this.measure = in.readString();
        this.name = in.readString();
    }

    public Ingredient(float quantity, String measure, String name) {
        this.quantity = quantity;
        this.measure = measure;
        this.name = name;
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(quantity);
        parcel.writeString(measure);
        parcel.writeString(name);
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getFullDescription() {
        return String.format("%s: %s %s", getName(), getQuantity(), getMeasure());
    }

    public String getQtyString() {
        return String.format(Locale.US, "%.1f %s", getQuantity(), getMeasure());
    }
}
