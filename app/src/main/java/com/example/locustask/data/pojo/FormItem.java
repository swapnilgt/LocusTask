package com.example.locustask.data.pojo;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FormItem {

    @SerializedName("type")
    @Expose
    private String type;
    private int typeInt;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("dataMap")
    @Expose
    private JsonObject dataMap;

    // The other set of data that has to be input by the user ...

    // This is the file path for th pic stored for the type 'PHOTO'
    private String picFilePath = null;
    private boolean imageExists = false;

    // This below two are for storing the details for the 'COMMENT' type
    private String comment;
    private boolean isCommentActive = false;

    // The current choice selected for the type 'SINGLE_CHOICE'
    private Integer choiceIndex = null;


    private static final String TYPE_PHOTO = "PHOTO";
    private static final String TYPE_COMMENT = "COMMENT";
    private static final String TYPE_SINGLE_CHOICE = "SINGLE_CHOICE";

    public static final int TYPE_PHOTO_INT = 0;
    public static final int TYPE_COMMENT_INT = 1;
    public static final int TYPE_SINGLE_CHOICE_INT = 2;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public JsonObject getDataMap() {
        return dataMap;
    }

    public void setDataMap(JsonObject dataMap) {
        this.dataMap = dataMap;
    }

    public int getTypeInt() {
        return typeInt;
    }

    public void setTypeInt() {
        switch (type) {
            case TYPE_COMMENT:
                this.typeInt = TYPE_COMMENT_INT;
                break;
            case TYPE_PHOTO:
                this.typeInt = TYPE_PHOTO_INT;
                break;
            case TYPE_SINGLE_CHOICE:
                this.typeInt = TYPE_SINGLE_CHOICE_INT;
                break;
        }
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isCommentActive() {
        return isCommentActive;
    }

    public void setCommentActive(boolean commentActive) {
        isCommentActive = commentActive;
    }

    public Integer getChoiceIndex() {
        return choiceIndex;
    }

    public void setChoiceIndex(Integer choiceIndex) {
        this.choiceIndex = choiceIndex;
    }

    public String getPicFilePath() {
        return picFilePath;
    }

    public void setPicFilePath(String picFilePath) {
        this.picFilePath = picFilePath;
    }

    public boolean isImageExists() {
        return imageExists;
    }

    public void setImageExists(boolean imageExists) {
        Log.d("FormPresenter", "Setting image exists for object as: " + imageExists);
        this.imageExists = imageExists;
    }

    @Override
    public String toString() {
        return "FormItem{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", dataMap=" + dataMap +
                ", imageExists='" + imageExists + '\'' +
                ", picFilePath='" + picFilePath + '\'' +
                ", comment='" + comment + '\'' +
                ", isCommentActive='" + isCommentActive + '\'' +
                ", choiceIndex=" + choiceIndex +
                '}';
    }
}
