package com.example.android.bakingapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Recipe implements Parcelable {
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
    private Integer id;
    private String name;
    private List<Ingredient> ingredients;
    private List<RecipeStep> steps;
    private Integer servings;
    private String image;
    public Recipe (Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.servings = in.readInt();
        this.image = in.readString();
        this.ingredients = new ArrayList<>();
        this.steps = new ArrayList<>();

        in.readTypedList(this.ingredients, Ingredient.CREATOR);
        in.readTypedList(this.steps, RecipeStep.CREATOR);
    }

    public Recipe(Integer id, String name, List<Ingredient> ingredients, List<RecipeStep> steps, Integer servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<RecipeStep> getSteps() {
        return steps;
    }

    public void setSteps(List<RecipeStep> steps) {
        Collections.sort(steps);
        this.steps = steps;
    }

    public int countSteps() {
        return steps.size();
    }

    public RecipeStep getStep(int stepNumber) {
        int length = countSteps();
        for (int i = 0; i < length; i++) {
            if (steps.get(i).getId() == stepNumber) {
                return steps.get(i);
            }
        }
        return null;
    }

    public RecipeStep getNextStepAfter(int stepNumber) {
        if (stepNumber < 0) {
            throw new IllegalArgumentException("Step number cannot be negative number");
        }
        if (stepNumber > countSteps() - 1) {
            throw new IllegalArgumentException("Step number cannot larger than last number step");
        }
        int nextNumber = stepNumber + 1;
        return getStep(nextNumber);
    }

    public RecipeStep getPreviousStepBefore(int stepNumber) {
        if (stepNumber < 0) {
            throw new IllegalArgumentException("Step number cannot smaller than the first number step");
        }
        int nextNumber = stepNumber - 1;
        return getStep(nextNumber);
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isImageExists() {
        return !(getImage().equals(""));
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(servings);
        parcel.writeString(image);
        parcel.writeTypedList(ingredients);
        parcel.writeTypedList(steps);
    }
}
