package com.example.user.lab_3;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText ed;
    private TextView tv;
    private Button bt;
    private Button btTest;

    private ContentValues cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //dbHelper = new DbHelper(MainActivity.this);
        cv = new ContentValues();

        ed = (EditText)findViewById(R.id.edit_test);
        tv = (TextView)findViewById(R.id.text_test);
        bt = (Button)findViewById(R.id.button_print_all);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = DbHelper.getInstance().getReadableDatabase();
                String query = "SELECT * FROM Category";
                Cursor cursor = db.rawQuery(query, null);
                String s = "";
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    s +="\n"+id+" "+name;
                }
                cursor.close();
                tv.setText(s);
            }
        });

        btTest = (Button)findViewById(R.id.button_test);
        btTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query;
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



        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

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
