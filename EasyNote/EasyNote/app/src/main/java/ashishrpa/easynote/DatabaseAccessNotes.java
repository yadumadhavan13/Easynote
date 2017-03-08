package ashishrpa.easynote;

/**
 * Created by WINDOWS 8.1 on 12/4/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class DatabaseAccessNotes {
    private SQLiteDatabase database;
    private DatabaseOpenHelper openHelper;
    private static volatile DatabaseAccessNotes instance;

    private DatabaseAccessNotes(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static synchronized DatabaseAccessNotes getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccessNotes(context);
        }
        return instance;
    }

    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public void saveNotes(Memo note) {
        ContentValues values = new ContentValues();
        values.put("date", note.getTime());
        values.put("note", note.getText());
        database.insert(DatabaseOpenHelper.TABLE_NOTES, null, values);
        Log.e("DBA : ", String.valueOf(note.getTime()));
        Log.e("DBA : ",note.getText());
    }

    public void updateNotes(Memo note) {
        ContentValues values = new ContentValues();
        values.put("date", new Date().getTime());
        values.put("note", note.getText());
        String date = Long.toString(note.getTime());
        database.update(DatabaseOpenHelper.TABLE_NOTES, values, "date = ?", new String[]{date});
    }

    public void deleteNotes(Memo note) {
        String date = Long.toString(note.getTime());
        database.delete(DatabaseOpenHelper.TABLE_NOTES, "date = ?", new String[]{date});
    }

    public List getAllNotes() {
        List notesList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * From notes ORDER BY date ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            long time = cursor.getLong(0);
            String text = cursor.getString(1);
            notesList.add(new Memo(time, text));
            cursor.moveToNext();
        }
        cursor.close();
        return notesList;
    }
}
