package com.example.user.lab_3;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 23.11.2016.
 */

public class TimeRecordActivity extends AppCompatActivity {

    public static final String STATE = "STATE";
    public static final String RECORD = "RECORD";
    public static final String PHOTO = "PHOTO";

    TimeRecord record;
    EditText editDescription;
    Spinner spinner;
    Context context;
    Intent intent;
    DatePicker datePicker;
    NumberPicker startHour;
    NumberPicker startMinute;
    NumberPicker endHour;
    NumberPicker endMinute;
    List<Photo> photo = new ArrayList<>();

    AlertDialog al;
    AlertDialog.Builder ad;
    View layoutView;

    private LinearLayout linear;
    private List<TextView> textViews = new ArrayList<>();
    private List<CheckBox> checkBox = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_layout);
        context = TimeRecordActivity.this;

        intent = getIntent();
        photo.clear();

        ///////////////////////////////////////////////////////////////////////////////////
        editDescription = (EditText)findViewById(R.id.edit_description);

        List<String> category = new ArrayList<>();
        SQLiteDatabase db = DbHelper.getInstance().getReadableDatabase();
        String query = "SELECT * FROM Category";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            category.add(cursor.getString(1));
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (Spinner)findViewById(R.id.spinner_category);
        spinner.setAdapter(adapter);

        startHour = (NumberPicker)findViewById(R.id.start_hour);
        startHour.setMinValue(0);
        startHour.setMaxValue(23);
        startMinute = (NumberPicker)findViewById(R.id.start_minute);
        startMinute.setMinValue(0);
        startMinute.setMaxValue(59);
        endHour = (NumberPicker)findViewById(R.id.end_hour);
        endHour.setMinValue(0);
        endHour.setMaxValue(23);
        endMinute = (NumberPicker)findViewById(R.id.end_minute);
        endMinute.setMinValue(0);
        endMinute.setMaxValue(59);

        datePicker = (DatePicker)findViewById(R.id.date_picker);

        //////////////////////////////////////////////////////////////////////////////
        if(intent.getSerializableExtra(STATE).equals("open")){
            record = (TimeRecord)intent.getSerializableExtra(RECORD);

            String name="";
            query = "SELECT name FROM Category where _id='"+record.getCategoryId()+"'";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                name = cursor.getString(0);
            }
            cursor.close();

            editDescription.setText(record.getDescription());
            String time;
            String[] t;
            time = record.getTimeStart();
            t = time.split(":");
            startHour.setValue(Integer.valueOf(t[0]));
            startMinute.setValue(Integer.valueOf(t[1]));
            time = record.getTimeEnd();
            t = time.split(":");
            endHour.setValue(Integer.valueOf(t[0]));
            endMinute.setValue(Integer.valueOf(t[1]));

            String date = record.getDate();
            String month = date.substring(3, 5);
            String day = date.substring(0, 2);
            String year = date.substring(6);
            datePicker.init(Integer.parseInt(year), Integer.parseInt(month)-1,Integer.parseInt(day), null);

            spinner.setSelection(category.indexOf(name));



            if(record.getPhotoIdList().length()>0) {
                if(record.getPhotoIdList().contains(",")) {
                    String ph[] = record.getPhotoIdList().split(",");

                    query = "SELECT * FROM Photo";
                    cursor = db.rawQuery(query, null);
                    while (cursor.moveToNext()) {
                        for (int i = 0; i < ph.length; i++)
                            if(cursor.getInt(0)==Integer.valueOf(ph[i]))
                                photo.add(new Photo(cursor.getInt(0),cursor.getString(1)));
                    }
                    cursor.close();
                }
                else {
                    query = "SELECT * FROM Photo WHERE _id='"+record.getPhotoIdList()+"'";
                    cursor = db.rawQuery(query, null);
                    while (cursor.moveToNext()) {
                        photo.add(new Photo(cursor.getInt(0),cursor.getString(1)));
                    }
                    cursor.close();
                }
            }
        }
        else{
            startHour.setValue(0);
            startMinute.setValue(0);
            endHour.setValue(0);
            endMinute.setValue(0);
            spinner.setSelection(0);
        }

        String f = intent.getStringExtra(PHOTO);
        if(f.length()>1){
            query = "SELECT * FROM Photo WHERE name='"+f+"'";
            cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                photo.add(new Photo(cursor.getInt(0),cursor.getString(1)));
            }
            cursor.close();
        }
        linear = (LinearLayout)findViewById(R.id.linear_photo);
        addTextView();
    }

    public void addTextView(){
        textViews.clear();
        linear.removeAllViews();

        checkBox.clear();
        linear.removeAllViews();
        CheckBox cb;
        for(int i = 0; i<photo.size();i++){
            cb = new CheckBox(context);
            cb.setId(i);
            cb.setOnClickListener(textClickListener);
            cb.setText(photo.get(i).getTitle());
            checkBox.add(cb);
        }

        for(CheckBox c:checkBox)
            linear.addView(c);
    }

    int id;

    View.OnClickListener textClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LayoutInflater li = LayoutInflater.from(context);
            layoutView = li.inflate(R.layout.photo_layout, null);
            CheckBox cb = (CheckBox)v;
            cb.setChecked(false);

            ad = new AlertDialog.Builder(context);
            ad.setView(layoutView);
            ad.setTitle("Edit category");
            ad.setCancelable(false);

            id = v.getId();
            ImageView im = (ImageView)layoutView.findViewById(R.id.image_view);
            Uri uri = Uri.fromFile(new File(photo.get(v.getId()).getName()));
            im.setImageURI(uri);

            al = ad.create();
            al.show();
        }
    };

    public void onClickAddPhoto(View view){
        String category,time,start,end,descr,catId = "",photoList = " ";

        category = spinner.getSelectedItem().toString();
        SQLiteDatabase db = DbHelper.getInstance().getReadableDatabase();
        String query = "SELECT _id FROM Category where name='"+category+"'";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            catId = cursor.getString(0);
        }
        cursor.close();
        //////////////////////////////////
        start = startHour.getValue()+":"+startMinute.getValue();
        end = endHour.getValue()+":"+endMinute.getValue();
        time = getTime(startHour.getValue(),startMinute.getValue(),endHour.getValue(),endMinute.getValue());
        //////////////////////////////////////
        descr = editDescription.getText().toString();

        String month,day,year,date;

        if (String.valueOf(datePicker.getMonth() + 1).length() == 1)
            month = "0" + String.valueOf(datePicker.getMonth() + 1);
        else month = String.valueOf(datePicker.getMonth() + 1);
        if (String.valueOf(datePicker.getDayOfMonth()).length() == 1)
            day = "0" + String.valueOf(datePicker.getDayOfMonth());
        else day = String.valueOf(datePicker.getDayOfMonth());
        year = String.valueOf(datePicker.getYear());
        date = String.valueOf(day + "." + month + "." + year);

        if(photo.size()>0){
            photoList = String.valueOf(photo.get(0).getId());
            for(int i = 1; i<photo.size();i++)
                photoList +=","+photo.get(i).getId();
        }

        Intent in = new Intent(TimeRecordActivity.this,CameraActivity.class);

        if(intent.getStringExtra(STATE).equals("open")){
            TimeRecord rec = new TimeRecord(record.getId(),Integer.valueOf(catId),date,descr,start,end,time,photoList);
            in.putExtra(TimeRecordActivity.STATE,"open");
            in.putExtra(RECORD,rec);
        }
        else{
            in.putExtra(TimeRecordActivity.STATE,"new");
        }

        startActivity(in);
    }

    public void onClickSave(View view){
        String category,time,start,end,descr,catId = "",photoList = " ";

        category = spinner.getSelectedItem().toString();
        SQLiteDatabase db = DbHelper.getInstance().getReadableDatabase();
        String query = "SELECT _id FROM Category where name='"+category+"'";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            catId = cursor.getString(0);
        }
        cursor.close();
        //////////////////////////////////
        start = startHour.getValue()+":"+startMinute.getValue();
        end = endHour.getValue()+":"+endMinute.getValue();
        time = getTime(startHour.getValue(),startMinute.getValue(),endHour.getValue(),endMinute.getValue());
        //////////////////////////////////////
        descr = editDescription.getText().toString();

        String month,day,year,date;

        if (String.valueOf(datePicker.getMonth() + 1).length() == 1)
            month = "0" + String.valueOf(datePicker.getMonth() + 1);
        else month = String.valueOf(datePicker.getMonth() + 1);
        if (String.valueOf(datePicker.getDayOfMonth()).length() == 1)
            day = "0" + String.valueOf(datePicker.getDayOfMonth());
        else day = String.valueOf(datePicker.getDayOfMonth());
        year = String.valueOf(datePicker.getYear());
        date = String.valueOf(day + "." + month + "." + year);

        if(photo.size()>0){
            photoList = String.valueOf(photo.get(0).getId());
            for(int i = 1; i<photo.size();i++)
                photoList +=","+photo.get(i).getId();
        }

        if(intent.getStringExtra(STATE).equals("open")){
            ContentValues cv = new ContentValues();
            cv.put("category_id", catId);
            cv.put("date", date);
            cv.put("description", descr);
            cv.put("time_start", start);
            cv.put("time_end", end);
            cv.put("time", time);
            cv.put("photo", photoList);
            DbHelper.getInstance().getWritableDatabase()
                    .update("Record", cv, "_id = ?", new String[]{String.valueOf(record.getId())});
        }
        else{
            query = "INSERT INTO Record "
                    +"(category_id,date,description,time_start,time_end,time,photo)"
                    +" VALUES ('"+catId+"','"+date+"','"+descr+"','"+start+"','"+end+"','"+time+"','"+photoList+"')";
            DbHelper.getInstance().getWritableDatabase().execSQL(query);
        }

        Intent intent = new Intent(TimeRecordActivity.this,MainActivity.class);
        startActivity(intent);
    }

    public String getTime(int h1, int m1, int h2, int m2){
        int h,m;

        if(h2>h1)
            h = h2-h1;
        else if(h2<h1){
            h = 24-h1+h2;
        }
        else h = h1;

        if(m2>m1)
            m = m2-m1;
        else if(m2<m1){
            m = 60-m1+m2;
            h--;
        }
        else m = m1;
        return String.valueOf(h+":"+m);
    }

    public void onClickCancel(View view){
        Intent intent = new Intent(TimeRecordActivity.this,MainActivity.class);
        startActivity(intent);
    }

    public void onClickDelete(View view){
        if(intent.getStringExtra(STATE).equals("open")) {
            DbHelper.getInstance().getWritableDatabase()
                    .delete("Record", "_id = ?", new String[]{String.valueOf(record.getId())});
        }
        Intent intent = new Intent(TimeRecordActivity.this,MainActivity.class);
        startActivity(intent);
    }

    public void onClickDeleteImage(View view){
        DbHelper.getInstance().getWritableDatabase()
                .delete("Photo", "_id = ?", new String[]{String.valueOf(id)});
        photo.remove(id);
        al.cancel();
        addTextView();
    }

    public void onClickCancelImage(View view){
        al.cancel();
    }
}
