package com.example.seryun.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    // Database 관련 객체들
    SQLiteDatabase db;
    String dbName = "idList.db";
    String tableName = "idListTable";
    int dbMode = Context.MODE_PRIVATE;

    // layout object
    EditText mEtName;
    EditText mEtId;
    Button mBtInsert;
    Button mBtRead;
    Button mBtDelete;
    Button mBtUpdate;
    Button mBtSort;

    ListView mList;
    ArrayAdapter<String> baseAdapter;
    ArrayList<String> nameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // // Database 생성 및 열기
        db = openOrCreateDatabase(dbName, dbMode, null);
        // 테이블 생성
        createTable();

        mEtName = (EditText) findViewById(R.id.et_text);
        mEtId = (EditText) findViewById(R.id.et_id);
        mBtInsert = (Button) findViewById(R.id.bt_insert);
        mBtRead = (Button) findViewById(R.id.bt_read);
        mBtDelete = (Button) findViewById(R.id.bt_delete);
        mBtUpdate = (Button) findViewById(R.id.bt_update);
        mBtSort = (Button) findViewById(R.id.bt_sort);
        ListView mList = (ListView) findViewById(R.id.list_view);

        mBtInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEtName.getText().toString();
                inserData(name);
            }
        });

        mBtRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameList.clear();
                selectAll();
                baseAdapter.notifyDataSetChanged();
            }
        });

        mBtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int id = Integer.parseInt(mEtId.getText().toString());
                    removeData(id);
                } catch (Exception e) {
                }
            }
        });

        mBtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEtName.getText().toString();
                int id = Integer.parseInt(mEtId.getText().toString());

                updateData(id, name);
            }
        });

        mBtSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameList.clear();
                selectAll_desc();
                baseAdapter.notifyDataSetChanged();
            }
        });

        // Create listview
        nameList = new ArrayList<String>();
        baseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nameList);
        mList.setAdapter(baseAdapter);
    }


    // Table 생성
    public void createTable() {
        try {
            String sql = "create table " + tableName + " (id integer primary key autoincrement, " + "name text not null)";
            db.execSQL(sql);
        } catch (android.database.sqlite.SQLiteException e) {
            Log.d("Lab sqlite", "error: " + e);
        }
    }

    // Table 삭제
    public void removeTable() {
        String sql = "drop table " + tableName;
        db.execSQL(sql);
    }

    // Data 추가
    public void inserData(String name) {
        String sql = "insert into " + tableName + " values(NULL, '" + name + "');";
        db.execSQL(sql);
    }

    // Data 업데이트
    public void updateData(int index, String name) {
        String sql = "update " + tableName + " set name = '" + name + "' where id = " + index + ";";
        db.execSQL(sql);
    }

    // Data 삭제
    public void removeData(int index) {
        try {
            String sql = "delete from " + tableName + " where id = " + index + ";";
            db.execSQL(sql);
        } catch (Exception e) {
        }
    }

    // Data 읽기 (꺼내오기)
    public void selectData(int index) {
        String sql = "select * from " + tableName + " where id = " + index + ";";
        Cursor result = db.rawQuery(sql, null);


        if(result.moveToFirst()) {
            int id = result.getInt(0);
            String name = result.getString(1);

            Log.d("lab_sqlite", "\"index= \" + id \" name=\" + name ");
        }
        result.close();
    }

    // 모든 Data 읽기
    public void selectAll() {
        String sql = "select * from " + tableName + ";";
        Cursor results = db.rawQuery(sql, null);
        results.moveToFirst();

        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String name = results.getString(1);

            Log.d("lab_sqlite", "index= " + id + " name=" + name);

            nameList.add(name);
            results.moveToNext();
        }
        results.moveToNext();
    }

    // 모든 Data 내림차순으로 읽기
    public void selectAll_desc() {
        try {
            String sql = "select * from " + tableName + " order by id desc;";
            Cursor results = db.rawQuery(sql, null);
            results.moveToFirst();

            while (!results.isAfterLast()) {
                int id = results.getInt(0);
                String name = results.getString(1);

                Log.d("lab_sqlite", "index= " + id + " name=" + name);

                nameList.add(name);
                results.moveToNext();
            }
            results.moveToNext();
        } catch (Exception e) {

        }
    }
}


