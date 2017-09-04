package com.example.android.bakingapp.entities;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.net.URLConnection;
import java.util.Locale;

public class RecipeStep implements Parcelable, Comparable<RecipeStep> {
    public static final Parcelable.Creator<RecipeStep> CREATOR = new Parcelable.Creator<RecipeStep>() {
        public RecipeStep createFromParcel(Parcel in) {
            return new RecipeStep(in);
        }

        public RecipeStep[] newArray(int size) {
            return new RecipeStep[size];
        }
    };
    private static final String LOG_TAG = RecipeStep.class.getSimpleName();
    private Integer id;
    private String shortDescription;
    private String description;
    @Nullable
    private String videoURL;
    @Nullable
    private String thumbnailURL;
    private boolean isSelected;

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

    public RecipeStep(Integer id, String shortDescription, String description, @Nullable String videoURL, @Nullable String thumbnailURL) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
        this.isSelected = false;
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

    public void selected() {
        isSelected = true;
    }

    public void unSelected() {
        isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getShortDescriptionWithOrderNumber() {
        if (getId() > 0) {
            return String.format(Locale.US, "%d. %s", getId(), getShortDescription());
        } else {
            return String.format(Locale.US, "%s", getShortDescription());
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Nullable
    public String getVideoURL() {
        if (hasVideo()) {
            return videoURL;
        }
        return "";
    }

    public void setVideoURL(@Nullable String videoURL) {
        this.videoURL = videoURL;
    }

    public Uri getVideoUri() {
        if (hasVideo()) {
            return Uri.parse(getVideoURL());
        }
        return null;
    }

    public boolean hasVideo() {
        String mimeType = URLConnection.guessContentTypeFromName(videoURL);
        return (videoURL != null && !videoURL.isEmpty() && mimeType != null && mimeType.startsWith("video"));
    }

    public boolean hasThumbVideo() {
        String mimeType = URLConnection.guessContentTypeFromName(thumbnailURL);
        return (thumbnailURL != null && !thumbnailURL.isEmpty() && mimeType != null && mimeType.startsWith("video"));
    }
    @Nullable
    public String getThumbnailURL() {
        String mimeType = URLConnection.guessContentTypeFromName(thumbnailURL);
        if (mimeType != null && mimeType.startsWith("image")) {
            return thumbnailURL;
        }
        return "";
    }

    public void setThumbnailURL(@Nullable String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getThumbURLVideo() {
        if (hasThumbVideo()) {
            return thumbnailURL;
        }
        return "";
    }

    public Uri getThumbVideoUri() {
        if (hasThumbVideo()) {
            return Uri.parse(thumbnailURL);
        }
        return null;
    }

    public boolean hasThumbnailURL() {
        return (getThumbnailURL() != null);
    }
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

    @Override
    public int compareTo(@NonNull RecipeStep recipeStep) {
        return getId() - recipeStep.getId();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RecipeStep && getId().intValue() == ((RecipeStep) obj).getId().intValue();
    }
}
