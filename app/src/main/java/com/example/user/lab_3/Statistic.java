package com.example.user.lab_3;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 24.11.2016.
 */

public class Statistic {

    public static List<String> getTopCount(){
        List<String> list = new ArrayList<>();

        List<Integer> category = new ArrayList<>();
        List<Integer> number = new ArrayList<>();

        SQLiteDatabase db = DbHelper.getInstance().getReadableDatabase();
        String query = "SELECT _id FROM Category";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            category.add(cursor.getInt(0));
            number.add(0);
        }
        cursor.close();

        int k = 0;
        query = "SELECT category_id FROM Record";
        cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            k = category.indexOf(cursor.getInt(0));
            number.set(k,number.get(k)+1);
        }
        cursor.close();

        int min = 1000;
        for(int i = 0; i<number.size();i++){
            if(number.get(i)<=min){
                min = category.get(i);
            }
        }

        int id1=min,id2=min,id3=min;
        int h1=0,h2=0,h3=0;
        for(int i = 0; i<number.size();i++){
            if(number.get(i)>h1){
                h3 = h2;
                h2 = h1;
                h1 = number.get(i);
                id3 = id2;
                id2 = id1;
                id1 = category.get(i);
            }
            else if(number.get(i)>h2){
                h3 = h2;
                h2 = number.get(i);
                id3 = id2;
                id2 = category.get(i);
            }
            else if(number.get(i)>h1){
                h3 = number.get(i);
                id3 = category.get(i);
            }
        }

        query = "SELECT name FROM Category where _id='"+id1+"'";
        cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(0)+" - "+h1);
        }
        cursor.close();
        query = "SELECT name FROM Category where _id='"+id2+"'";
        cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(0)+" - "+h2);
        }
        cursor.close();
        query = "SELECT name FROM Category where _id='"+id3+"'";
        cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(0)+" - "+h3);
        }
        cursor.close();
        return list;
    }
}
