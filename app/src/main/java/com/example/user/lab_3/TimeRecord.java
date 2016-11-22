package com.example.user.lab_3;

/**
 * Created by User on 22.11.2016.
 */

public class TimeRecord {
    private int id;
    private int categoryId;
    private String description;
    private String timeStart;
    private String timeEnd;
    private String time;
    private String photoIdList;

    public TimeRecord(int id, int categoryId, String description, String timeStart, String timeEnd,String time,String photoIdList){
        this.id = id;
        this.categoryId = categoryId;
        this.description = description;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.photoIdList = photoIdList;
        this.time = time;
    }

    public String getTime(String timeStart, String timeEnd){

        return "1";
    }

    public int getId(){
        return id;
    }

    public int getCategoryId(){
        return categoryId;
    }

    public String getDescription(){
        return description;
    }

    public String getTimeStart(){
        return timeStart;
    }

    public String getTimeEnd(){
        return timeEnd;
    }

    public String getTime(){
        return time;
    }

    public String getPhotoIdList(){
        return photoIdList;
    }

}
