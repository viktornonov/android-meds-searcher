package com.example.myapp;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.*;

public class MySqlLiteHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "meds";//the extension may be .sqlite or .db
    private static final int DB_VERSION = 1;
    private String DB_PATH ;

    // Meds table name
    private static final String TABLE_MEDS = "meds";

    // Meds Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String[] COLUMNS = {KEY_ID,KEY_TITLE};

    private Context myContext;
    public SQLiteDatabase db;

    public MySqlLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
        DB_PATH = "/data/data/" + myContext.getApplicationContext().getPackageName() + "/databases/";

        prepareDatabase();
        openDatabase();
    }

    public Med getMed(int id){
        Cursor cursor =
                db.query(TABLE_MEDS, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        Med med = new Med();
        med.setId(Integer.parseInt(cursor.getString(0)));
        med.setTitle(cursor.getString(1));

        Log.d("getMed(" + id + ")", med.toString());

        return med;
    }

    public Med getMedByName(String searchTerm) {
        Cursor cursor =
                db.query(TABLE_MEDS, // a. table
                        COLUMNS, // b. column names
                        "title LIKE '%" + searchTerm + "%'", // c. selections
                        null, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit
        if (cursor.getCount() <= 0) {
            return null;
        }
        else {
            cursor.moveToFirst();
        }

        Med med = new Med();
        med.setId(Integer.parseInt(cursor.getString(0)));
        med.setTitle(cursor.getString(1));

        //log
        Log.d("getMed(" + searchTerm + ")", med.toString());

        // 5. return book
        return med;
    }
    public void addMed(Med med){
        Log.d("addMed", med.toString());

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, med.getTitle()); // get title

        db.insert(TABLE_MEDS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
    }

    private void prepareDatabase() {
        boolean dbExist = checkDatabase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Database doesn't exist");
        }
    }

    private void openDatabase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    private boolean checkDatabase() {
        boolean dbExists = false;
        try {
            String myPath = DB_PATH + DB_NAME;
            File dbFile = new File(myPath);
            dbExists = dbFile.exists();
        } catch(SQLiteException e) {
            System.out.println("Database doesn't exist");
        }
        return dbExists;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public synchronized void close() {
        if(db != null) {
            db.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
