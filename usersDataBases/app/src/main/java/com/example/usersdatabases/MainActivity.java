package com.example.usersdatabases;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "logins";
    private static final String COLUMN_LOGIN = "login";
    private static final String COLUMN_PASSWORD = "password";
    private static final int NUM_COLUMN_LOGIN = 0;
    private static final int NUM_COLUMN_PASSWORD = 1;

    private EditText editTextLogin;
    private EditText editTextPassword;
    private Button buttonLogIn;

    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OpenHelper openHelper = new OpenHelper(MainActivity.this);
        sqLiteDatabase = openHelper.getWritableDatabase();

        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogIn = findViewById(R.id.buttonLogIn);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.buttonLogIn:
                        String introducedLogin = editTextLogin.getText().toString();
                        String introducedPassword = editTextPassword.getText().toString();
                        String baseLogin = "";
                        String basePassword = "";
                        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, null, COLUMN_LOGIN + " = '" + introducedLogin + "'", null, null, null, null);
                        cursor.moveToFirst();
                        if( cursor != null && cursor.moveToFirst() ){
                            baseLogin = cursor.getString(NUM_COLUMN_LOGIN);
                            basePassword = cursor.getString(NUM_COLUMN_PASSWORD);
                            cursor.close();
                        }
                        if(baseLogin.equals(introducedLogin) && basePassword.equals(introducedPassword)) {
                            Toast.makeText(MainActivity.this, "DONE", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_LOGIN, "admin");
                contentValues.put(COLUMN_PASSWORD, "admin");
                sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
            }
        };
        buttonLogIn.setOnClickListener(listener);
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String query = "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_LOGIN + " VARCHAR(50) PRIMARY KEY, " +
                    COLUMN_PASSWORD + " VARCHAR(50));";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}