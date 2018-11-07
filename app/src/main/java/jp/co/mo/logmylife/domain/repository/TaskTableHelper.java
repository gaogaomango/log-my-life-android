package jp.co.mo.logmylife.domain.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import jp.co.mo.logmylife.common.util.DateUtil;

public class TaskTableHelper extends AbstractDataTableHelper {

    private static final String CONTACTS_TABLE_NAME = "todo";
    private static final String CONTACTS_COLUMN_ID = "id";
    private static final String CONTACTS_COLUMN_TASK = "task";
    private static final String CONTACTS_COLUMN_DATE = "dateStr";

    public static final String CREATE_TASK_TABLE_SQL = "CREATE TABLE " + CONTACTS_TABLE_NAME +
            "(" + CONTACTS_COLUMN_ID + " INTEGER PRIMARY KEY, " + CONTACTS_COLUMN_TASK + " TEXT, " + CONTACTS_COLUMN_DATE + " INTEGER)";

    public static final String DROP_TASK_TABLE_SQL = "DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME;

    public TaskTableHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // TODO
    public TaskTableHelper(Context context, String dataBaseName, SQLiteDatabase.CursorFactory factory, int dataBaseVersion) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor getDataSpecific(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " WHERE " + CONTACTS_COLUMN_ID + " = '" + id + "' order by " + CONTACTS_COLUMN_ID + " desc", null);
        return res;
    }

    public boolean updateContact(String id, String task, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_TASK, task);
        contentValues.put(CONTACTS_COLUMN_DATE, DateUtil.getDate(date));

        db.update(CONTACTS_TABLE_NAME, contentValues, CONTACTS_COLUMN_ID + " = ? ", new String[] {id});

        return true;
    }

    public boolean insertContact(String task, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_TASK, task);
        contentValues.put(CONTACTS_COLUMN_DATE, DateUtil.getDate(date));

        db.insert(CONTACTS_TABLE_NAME, null, contentValues);

        return true;
    }

    public Cursor getDataToday() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " WHERE date(datetime(" + CONTACTS_COLUMN_DATE + " / 1000, 'unixepoch', 'localtime')) = date('now', 'localtime') order by " + CONTACTS_COLUMN_ID + " desc", null);
        return res;
    }

    public Cursor getDataTomorrow() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " WHERE date(datetime(" + CONTACTS_COLUMN_DATE + " / 1000, 'unixepoch', 'localtime')) = date('now', '+1 day', 'localtime') order by " + CONTACTS_COLUMN_ID + " desc", null);
        return res;
    }

    public Cursor getDataUpcoming() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME + " WHERE date(datetime(" + CONTACTS_COLUMN_DATE + " / 1000, 'unixepoch', 'localtime')) > date('now', '+1 day', 'localtime') order by " + CONTACTS_COLUMN_ID + " desc", null);
        return res;
    }

}
