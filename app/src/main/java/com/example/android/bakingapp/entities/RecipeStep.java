package com.example.android.bakingapp.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

/**
 * Created by vincent on 8/21/17.
 */

public class RecipeStep implements Parcelable {
    private Integer id;
    private String shortDescription;
    private String description;
    @Nullable
    private String videoURL;
    @Nullable
    private String thumbnailURL;

    public RecipeStep(Parcel in) {
        this.id = in.readInt();
        this.shortDescription = in.readString();
        this.description = in.readString();
        this.videoURL = in.readString();
        this.thumbnailURL = in.readString();
    }
    public RecipeStep() {
        super();
    }
    public Integer getId() {
        return id;
    }
    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Nullable
    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(@Nullable String videoURL) {
        this.videoURL = videoURL;
    }

    @Nullable
    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(@Nullable String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }
    public static final Parcelable.Creator<RecipeStep> CREATOR = new Parcelable.Creator<RecipeStep>() {
        public RecipeStep createFromParcel(Parcel in) {
            return new RecipeStep(in);
        }
        public RecipeStep[] newArray(int size) {
            return new RecipeStep[size];
        }
    };
    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(shortDescription);
        parcel.writeString(description);
        parcel.writeString(videoURL);
        parcel.writeString(thumbnailURL);
    }
}
