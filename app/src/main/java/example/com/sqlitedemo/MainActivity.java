package example.com.sqlitedemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText myName, myAge, myAddress;
    private DbHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        helper = new DbHelper(this, "mydb", null, 2);


    }

    private void init() {
        myName = (EditText) findViewById(R.id.myName);
        myAge = (EditText) findViewById(R.id.myAge);
        myAddress = (EditText) findViewById(R.id.myAddress);
        findViewById(R.id.txt_info).setOnClickListener(this::Click);
        findViewById(R.id.btn_insert).setOnClickListener(this::Click);
        findViewById(R.id.btn_update).setOnClickListener(this::Click);
        findViewById(R.id.btn_display).setOnClickListener(this::Click);
        findViewById(R.id.btn_delete).setOnClickListener(this::Click);
        findViewById(R.id.btn_join).setOnClickListener(this::Click);

    }

    private void insert() {
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put("myname",getMyName());
        values.put("myage",getMyage());
        db.insertWithOnConflict("myinfo", null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
        db.insert("myinfo",null,values);


        ContentValues addressValues= new ContentValues();
        addressValues.put("myname_address",getMyName());
        addressValues.put("myaddress",getMyaddress());
        db.insertWithOnConflict("myAddress", null, addressValues,
                SQLiteDatabase.CONFLICT_REPLACE);
        db.insert("myAddress",null,addressValues);


        db.close();
    }

    private String getMyaddress() {
        return myAddress.getText().toString().trim();
    }

    private void view() {
        SQLiteDatabase db=helper.getReadableDatabase();
        String table="myinfo";
        String [] column={"myname"};
        //where clause
        String selection="myage = ? ";
        String [] selectionArgs={""+getMyage()};
        String groupby=null;
        String having=null;
        String orderby=null;

        Cursor cursor=db.query(table,column,selection,selectionArgs,groupby,having,orderby);

        while (cursor.moveToNext()){
         String myName=cursor.getString(cursor.getColumnIndex("myname"));
//            Integer myAge=cursor.getInt(cursor.getColumnIndex("myage"));
//            Log.i("Data", "My Information"+"\n"+myName+"\n"+myAge);
            Log.i("Data", "My Information"+"\n"+myName);

            ((EditText)findViewById(R.id.myName)).setText(myName);

        }
        db.close();
    }

    private void delete() {
    }

    private void update() {
    }

    private String getMyName() {

        return myName.getText().toString().trim();

    }
    private Integer getMyage(){
        return Integer.valueOf(myAge.getText().toString().trim());
    }

    public void Click(View view) {
        if (view.getId() == R.id.btn_insert) {
            insert();
        }
        if (view.getId() == R.id.btn_update) {
            update();
        }
        if (view.getId() == R.id.btn_display) {
            view();
        }
        if (view.getId() == R.id.btn_delete) {
            delete();
        }
        if (view.getId() == R.id.btn_join) {
             join();
        }

    }

    private void join() {
        SQLiteDatabase db=helper.getWritableDatabase();

        String query="SELECT * FROM myinfo info INNER JOIN myAddress address ON info.myname = address.myname_address";
        Cursor cursor=db.rawQuery(query,null);

        while (cursor.moveToNext()){
            String myName=cursor.getString(cursor.getColumnIndex("myname"));
            Integer myAge=cursor.getInt(cursor.getColumnIndex("myage"));
            String myAddress=cursor.getString(cursor.getColumnIndex("myname_address"));

            Log.i("Data", "My Information"+"\n"+myName+"\n"+myAge+"\n"+myAddress);



        }

        db.close();

    }
}
