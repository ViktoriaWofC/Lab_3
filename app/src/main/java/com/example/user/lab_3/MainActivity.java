package com.example.user.lab_3;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tv;

    private Button buttonStatistic;
    private DatePicker datePickerStart;
    private DatePicker datePickerEnd;
    private RecyclerView recyclerCategory;
    private RecyclerView recyclerRecord;
    private EditText editText;
    private RadioButton radioMonth;
    private RadioButton radioVar;
    private TextView topCount;

    private ContentValues cv;
    List<Category> category = new ArrayList<>();
    List<TimeRecord> records = new ArrayList<>();
    Context context;
    int position;

    AlertDialog al;
    AlertDialog.Builder ad;
    View layoutView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = MainActivity.this;

        //dbHelper = new DbHelper(MainActivity.this);
        cv = new ContentValues();

        updateCategory();
        updateRecords();



        tv = (TextView)findViewById(R.id.text_test);



        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");

        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator(getString(R.string.records));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator(getString(R.string.category));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setContent(R.id.tab3);
        tabSpec.setIndicator(getString(R.string.statistics));
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);

        ///////////////////////////////////////////////

        recyclerCategory = (RecyclerView)findViewById(R.id.recycler_category);
        recyclerCategory.setLayoutManager(new LinearLayoutManager(this));
        recyclerCategory.setAdapter(new CategoryAdaper());

        recyclerRecord = (RecyclerView)findViewById(R.id.recycler_record);
        recyclerRecord.setLayoutManager(new LinearLayoutManager(this));
        recyclerRecord.setAdapter(new RecordAdaper());

        ////////////////////////////////////////////////////
        radioMonth = (RadioButton)findViewById(R.id.radio_month);
        radioMonth.setChecked(true);
        radioMonth.setOnClickListener(radioButtonClickListener);
        radioVar = (RadioButton)findViewById(R.id.radio_var);
        radioVar.setChecked(false);
        radioVar.setOnClickListener(radioButtonClickListener);
        datePickerStart = (DatePicker)findViewById(R.id.date_start);
        datePickerStart.setEnabled(false);
        datePickerEnd = (DatePicker)findViewById(R.id.date_end);
        datePickerEnd.setEnabled(false);
        buttonStatistic = (Button)findViewById(R.id.button_statistic);
        buttonStatistic.setEnabled(false);

        topCount = (TextView)findViewById(R.id.text_top_count);
        updateTopCount();
    }

    public void updateCategory(){
        category.clear();
        SQLiteDatabase db = DbHelper.getInstance().getReadableDatabase();
        String query = "SELECT * FROM Category";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            category.add(new Category(Integer.valueOf(cursor.getString(0)),cursor.getString(1)));
        }
        cursor.close();
    }

    public void updateRecords(){
        records.clear();
        SQLiteDatabase db = DbHelper.getInstance().getReadableDatabase();
        String query = "SELECT * FROM Record";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            records.add(new TimeRecord(
                    Integer.valueOf(cursor.getString(0)),
                    Integer.valueOf(cursor.getString(1)),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7)));
        }
        cursor.close();

        //tv.setText(records.size());
    }

    /////////////////////////////////////////////////
    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textNameCategory;
        private CardView cv;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            textNameCategory = (TextView) itemView.findViewById(R.id.text_name_category);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            LayoutInflater li = LayoutInflater.from(context);
            layoutView = li.inflate(R.layout.edit_category, null);
            TextView tv;
            position = this.getLayoutPosition();

            ad = new AlertDialog.Builder(context);
            ad.setView(layoutView);
            ad.setTitle("Edit category");
            ad.setCancelable(false);

            editText = (EditText)layoutView.findViewById(R.id.edit_name_category);
            editText.setText(category.get(position).getName());

            al = ad.create();
            al.show();
        }

        /*public MeetingViewHolder(ViewGroup parent) {
            super(getLayoutInflater().inflate(R.layout.meeting_item, parent, false));
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            userTextView = (TextView) itemView.findViewById(R.id.userTextView);
            stringTextView = (TextView) itemView.findViewById(R.id.stringTextView);
        }*/
    }

    class CategoryAdaper extends RecyclerView.Adapter<CategoryViewHolder> {

        @Override
        public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.category_item, parent, false);
            CategoryViewHolder viewHolder = new CategoryViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(CategoryViewHolder holder, int position) {
            holder.textNameCategory.setText(category.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return category.size();
        }
    }

    class RecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textRefCategory;
        public TextView textTimeStart;
        public TextView textTimeEnd;
        public TextView textTime;
        public TextView textDate;
        private CardView cv;

        public RecordViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            textRefCategory = (TextView) itemView.findViewById(R.id.text_ref_category);
            textTimeStart = (TextView) itemView.findViewById(R.id.text_time_start);
            textTimeEnd = (TextView) itemView.findViewById(R.id.text_time_end);
            textTime = (TextView) itemView.findViewById(R.id.text_time);
            textDate = (TextView) itemView.findViewById(R.id.text_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            position = this.getLayoutPosition();
            Intent intent = new Intent(MainActivity.this,TimeRecordActivity.class);
            intent.putExtra(TimeRecordActivity.STATE,"open");
            intent.putExtra(TimeRecordActivity.RECORD,records.get(position));
            startActivity(intent);
        }

        /*public MeetingViewHolder(ViewGroup parent) {
            super(getLayoutInflater().inflate(R.layout.meeting_item, parent, false));
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            userTextView = (TextView) itemView.findViewById(R.id.userTextView);
            stringTextView = (TextView) itemView.findViewById(R.id.stringTextView);
        }*/
    }

    class RecordAdaper extends RecyclerView.Adapter<RecordViewHolder> {

        @Override
        public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.time_item, parent, false);
            RecordViewHolder viewHolder = new RecordViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecordViewHolder holder, int position) {
            SQLiteDatabase db = DbHelper.getInstance().getReadableDatabase();
            String query = "SELECT name FROM Category where _id='"+records.get(position).getCategoryId()+"'";
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                holder.textRefCategory.setText(cursor.getString(0));
            }
            cursor.close();
            holder.textTimeStart.setText(records.get(position).getTimeStart());
            holder.textTimeEnd.setText(records.get(position).getTimeEnd());
            holder.textTime.setText(records.get(position).getTime());
            holder.textDate.setText(records.get(position).getDate());
        }

        @Override
        public int getItemCount() {
            return records.size();
        }
    }


    ////////////////////////////////////////////////////////
    public void onClickCancel(View view){
        al.cancel();
    }

    ///////////////////////////////////////////////////////////////
    public void onClickAddCategory(View view){
        LayoutInflater li = LayoutInflater.from(context);
        layoutView = li.inflate(R.layout.create_category, null);
        TextView tv;

        ad = new AlertDialog.Builder(context);
        ad.setView(layoutView);
        ad.setTitle("Create category");
        ad.setCancelable(false);
        editText = (EditText)layoutView.findViewById(R.id.edit_name_category);
        al = ad.create();
        al.show();
    }

    public void onClickCreateCategory(View view){
        //добавление значения
        String query = "INSERT INTO Category (name) VALUES ('"+editText.getText().toString()+"')";
        DbHelper.getInstance().getWritableDatabase().execSQL(query);
        al.cancel();
        updateCategory();
        recyclerCategory.getAdapter().notifyDataSetChanged();
    }

    public void onClickSaveCategory(View view){

        editText = (EditText)layoutView.findViewById(R.id.edit_name_category);
        tv.setText(editText.getText().toString());
        //изменение значения
        cv.clear();
        cv.put("name", editText.getText().toString());
        DbHelper.getInstance().getWritableDatabase()
                .update("Category", cv, "_id = ?", new String[]{String.valueOf(category.get(position).getId())});
        al.cancel();
        updateCategory();
        recyclerCategory.getAdapter().notifyDataSetChanged();
    }

    public void onClickDeleteCategory(View view){
        //удаление значения
        DbHelper.getInstance().getWritableDatabase()
                .delete("Category","_id = ?", new String[]{String.valueOf(category.get(position).getId())});
        updateCategory();
        recyclerCategory.getAdapter().notifyDataSetChanged();
        al.cancel();
    }

    public void onClickAddRecord(View view){
        Intent intent = new Intent(MainActivity.this,TimeRecordActivity.class);
        intent.putExtra(TimeRecordActivity.STATE,"new");
        startActivity(intent);
    }
    ///////////////////////////////////////////////////////////////

    View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton)v;
            switch (rb.getId()) {
                case R.id.radio_month:
                    onClickRadioMonth();
                    break;
                case R.id.radio_var:
                    onClickRadioVar();
                    break;
                default:
                    break;
            }
        }
    };

    public void updateTopCount(){
        List<String> list = Statistic.getTopCount();
        String s = "";
        for(String ss:list)
            s +=ss+"\n";
        topCount.setText(s);
        //tv.setText(String.valueOf(list.size()));
    }



    public void onClickRadioMonth(){
        datePickerStart.setEnabled(false);
        datePickerEnd.setEnabled(false);
        buttonStatistic.setEnabled(false);

            //обновляем инфу

    }

    public void onClickRadioVar(){
        datePickerStart.setEnabled(true);
        datePickerEnd.setEnabled(true);
        buttonStatistic.setEnabled(true);

            //обновляем инфу

    }

    public void onClickStatistic(View view){


    }

    public int compareDate(String d1, String d2){
        //d1>d2 - 1
        //d1=d2 - 0
        //d1<d2 - -1
        int month1 = Integer.valueOf(d1.substring(3, 5));
        int day1 = Integer.valueOf(d1.substring(0, 2));
        int year1 = Integer.valueOf(d1.substring(6));
        int month2 = Integer.valueOf(d2.substring(3, 5));
        int day2 = Integer.valueOf(d2.substring(0, 2));
        int year2 = Integer.valueOf(d2.substring(6));

        if(year1>year2) return 1;
        else if(year1<year2) return -1;
        else if(month1>month2) return 1;
        else if(month1<month2) return -1;
        else if(day1>day2) return 1;
        else if(day1<day2) return -1;
        else return 0;
    }


    ////////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
