package com.example.user.lab_3;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 20.11.2016.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static DbHelper instance = null;

    public static void init(Context context){
        instance = new DbHelper(context);
    }

    public static DbHelper getInstance(){
        return instance;
    }

    private DbHelper(Context context) {
        super(context, "lab3.sqlite", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Category (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE Record (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL);");
        sqLiteDatabase.execSQL("CREATE TABLE Photo (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL);");
        setTableCategory(sqLiteDatabase);
    }

    public void setTableCategory(SQLiteDatabase sqLiteDatabase){
        String insertQuery = "INSERT INTO Category (name) VALUES ('Работа')";
        sqLiteDatabase.execSQL(insertQuery);
        insertQuery = "INSERT INTO Category (name) VALUES ('Обед')";
        sqLiteDatabase.execSQL(insertQuery);
        insertQuery = "INSERT INTO Category (name) VALUES ('Отдых')";
        sqLiteDatabase.execSQL(insertQuery);
        insertQuery = "INSERT INTO Category (name) VALUES ('Уборка')";
        sqLiteDatabase.execSQL(insertQuery);
        insertQuery = "INSERT INTO Category (name) VALUES ('Сон')";
        sqLiteDatabase.execSQL(insertQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
