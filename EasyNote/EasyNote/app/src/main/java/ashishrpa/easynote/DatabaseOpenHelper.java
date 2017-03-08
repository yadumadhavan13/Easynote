package ashishrpa.easynote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by WINDOWS 8.1 on 12/4/2016.
 */

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE = "easynotes.db";
    public static final String TABLE_NOTES = "notes";
    public static final String TABLE_MEMO = "memo";
    public static final String TABLE_TODO = "todo";

    public static final int VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE memo(date INTEGER PRIMARY KEY, memo TEXT);");
        db.execSQL("CREATE TABLE notes(date INTEGER PRIMARY KEY, note TEXT);");
        db.execSQL("CREATE TABLE todo(date INTEGER PRIMARY KEY, title TEXT, description TEXT, toDoDate INTEGER, status INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}