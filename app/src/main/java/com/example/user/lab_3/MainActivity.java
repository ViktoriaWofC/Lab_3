package com.example.user.lab_3;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText ed;
    private TextView tv;
    private Button bt;
    private Button btTest;
    private RecyclerView recyclerCategory;
    private RecyclerView recyclerRecord;
    private EditText editText;

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

        ed = (EditText)findViewById(R.id.edit_test);
        tv = (TextView)findViewById(R.id.text_test);
        bt = (Button)findViewById(R.id.button_print_all);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*SQLiteDatabase db = DbHelper.getInstance().getWritableDatabase();
                String insertQuery = "INSERT INTO Record "
                        +"(category_id,description,time_start,time_end,time,photo)"
                        +" VALUES ('3','lalala','12:50','13:10','00:20',' ')";
                db.execSQL(insertQuery);*/

                SQLiteDatabase db = DbHelper.getInstance().getReadableDatabase();
                String query = "SELECT * FROM Record";
                Cursor cursor = db.rawQuery(query, null);
                String s = "";
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    s +="\n"+id+" "+name;
                }
                cursor.close();
                tv.setText(s);

                /*SQLiteDatabase db = DbHelper.getInstance().getReadableDatabase();
                String query = "SELECT * FROM Category";
                Cursor cursor = db.rawQuery(query, null);
                String s = "";
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    s +="\n"+id+" "+name;
                }
                cursor.close();
                tv.setText(s);*/
            }
        });

        btTest = (Button)findViewById(R.id.button_test);
        btTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query;
                tv.setText("");

                //получение конкретного значения
                /*query = "SELECT * FROM Category where _id='"+ed.getText().toString()+"'";
                Cursor cursor = DbHelper.getInstance().getReadableDatabase().rawQuery(query, null);
                String s = "";
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    s +="\n"+id+" "+name;
                }
                cursor.close();
                tv.setText(s);*/

                //изменение значения
                //cv.clear();
                //cv.put("name", ed.getText().toString());
                //DbHelper.getInstance().getWritableDatabase().update("Category", cv, "_id = ?", new String[]{"6"});

                //удаление значения
                //DbHelper.getInstance().getWritableDatabase().delete("Category","_id = ?", new String[]{"6"});

                //добавление значения
                //query = "INSERT INTO Category (name) VALUES ('"+ed.getText().toString()+"')";
                //DbHelper.getInstance().getWritableDatabase().execSQL(query);
            }
        });


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
                    cursor.getString(6)));
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
        private CardView cv;

        public RecordViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            textRefCategory = (TextView) itemView.findViewById(R.id.text_ref_category);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            /*
            LayoutInflater li = LayoutInflater.from(context);
            layoutView = li.inflate(R.layout.edit_category, null);
            TextView tv;
            position = this.getLayoutPosition();

            ad = new AlertDialog.Builder(context);
            ad.setView(layoutView);
            ad.setTitle("Edit category");
            ad.setCancelable(false);

            editText = (EditText)layoutView.findViewById(R.id.edit_name_category);
            editText.setText(category.get(position));

            al = ad.create();
            al.show();*/
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
            holder.textRefCategory.setText(records.get(position).getDescription());
            //holder.textNameCategory.setText(category.get(position));
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
    ///////////////////////////////////////////////////////////////


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
