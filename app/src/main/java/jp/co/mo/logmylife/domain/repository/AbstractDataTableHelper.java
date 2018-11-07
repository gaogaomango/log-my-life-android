package jp.co.mo.logmylife.domain.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AbstractDataTableHelper extends SQLiteOpenHelper {

    protected static final int DATABASE_VERSION = 1;

    protected static final String DATABASE_NAME = "LogMyLife.db";

    public AbstractDataTableHelper(Context context, String dataBaseName, SQLiteDatabase.CursorFactory factory, int dataBaseVersion){
        super(context, dataBaseName, null, dataBaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String[] queries = {
                MapDataTableHelper.CREATE_MAP_INFO_TABLE_SQL,
                MapDataTableHelper.CREATE_MAP_PIC_INFO_TABLE_SQL,
                TaskTableHelper.CREATE_TASK_TABLE_SQL
        };
        for(String query : queries) {
            db.execSQL(
                    query
            );
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String[] queries = {
                MapDataTableHelper.DROP_MAP_INFO_TABLE_SQL,
                MapDataTableHelper.DROP_MAP_PIC_INFO_TABLE_SQL,
                TaskTableHelper.DROP_TASK_TABLE_SQL
        };
        for(String query : queries) {
            db.execSQL(
                    query
            );
        }
        onCreate(db);
    }
}
