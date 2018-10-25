package jp.co.mo.logmylife.domain.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AbstractDataTableHelper extends SQLiteOpenHelper {

    protected static final int DATABASE_VERSION = 2;

    protected static final String DATABASE_NAME = "LogMyLife.db";

    public AbstractDataTableHelper(Context context, String dataBaseName, SQLiteDatabase.CursorFactory factory, int dataBaseVersion){
        super(context, dataBaseName, null, dataBaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
