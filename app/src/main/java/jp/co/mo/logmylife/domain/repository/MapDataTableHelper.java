package jp.co.mo.logmylife.domain.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.mo.logmylife.common.util.DateUtil;
import jp.co.mo.logmylife.domain.entity.map.MapPlaceData;

public class MapDataTableHelper extends AbstractDataTableHelper {

    protected static final String TABLE_NAME = "mapInfo";
    protected static final String _ID = "_id";
    protected static final String COLUMN_NAME_USER_ID = "userId";
    protected static final String COLUMN_NAME_TITLE = "title";
    protected static final String COLUMN_NAME_LAT = "lat";
    protected static final String COLUMN_NAME_LNG = "lng";
    protected static final String COLUMN_NAME_TYPE_ID = "typeId";
    protected static final String COLUMN_NAME_TYPE_DETAIL_ID = "typeDetailId";
    protected static final String COLUMN_NAME_URL = "url";
    protected static final String COLUMN_NAME_DETAIL = "detail";
    protected static final String COLUMN_NAME_CREATE_DATE = "createDate";
    protected static final String COLUMN_NAME_UPDATE_DATE = "updateDate";

    protected static final int COLUMN_NAME_USER_ID_NUM = 0;
    protected static final int COLUMN_NAME_TITLE_NUM = COLUMN_NAME_USER_ID_NUM + 1;
    protected static final int COLUMN_NAME_TYPE_ID_NUM = COLUMN_NAME_TITLE_NUM + 1;
    protected static final int COLUMN_NAME_LAT_NUM = COLUMN_NAME_TITLE_NUM + 1;
    protected static final int COLUMN_NAME_LNG_NUM = COLUMN_NAME_LAT_NUM + 1;
    protected static final int COLUMN_NAME_TYPE_DETAIL_ID_NUM = COLUMN_NAME_LNG_NUM + 1;
    protected static final int COLUMN_NAME_URL_NUM = COLUMN_NAME_TYPE_DETAIL_ID_NUM + 1;
    protected static final int COLUMN_NAME_DETAIL_NUM = COLUMN_NAME_URL_NUM + 1;
    protected static final int COLUMN_NAME_CREATE_DATE_NUM = COLUMN_NAME_DETAIL_NUM + 1;
    protected static final int COLUMN_NAME_UPDATE_DATE_NUM = COLUMN_NAME_CREATE_DATE_NUM + 1;

    // TODO: image fileは別から取得する。

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE  IF NOT EXISTS " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_USER_ID + " TEXT," +
                    COLUMN_NAME_TITLE + " TEXT," +
                    COLUMN_NAME_LAT + " REAL," +
                    COLUMN_NAME_LNG + " REAL," +
                    COLUMN_NAME_TYPE_ID + " INTEGER," +
                    COLUMN_NAME_TYPE_DETAIL_ID + " INTEGER," +
                    COLUMN_NAME_URL + " TEXT," +
                    COLUMN_NAME_DETAIL + " TEXT," +
                    COLUMN_NAME_CREATE_DATE + " DATETIME default current_timestamp," +
                    COLUMN_NAME_UPDATE_DATE + "  DATETIME default current_timestamp)";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public MapDataTableHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                SQL_CREATE_ENTRIES
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(
                SQL_DELETE_ENTRIES
        );
        onCreate(db);
    }

    public void saveData(SQLiteDatabase db, String userId, MapPlaceData data) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_USER_ID, userId);
        values.put(COLUMN_NAME_TITLE, data.getTitle());
        values.put(COLUMN_NAME_LAT, data.getLat());
        values.put(COLUMN_NAME_LNG, data.getLng());
        values.put(COLUMN_NAME_TYPE_ID, data.getTypeId());
        values.put(COLUMN_NAME_TYPE_DETAIL_ID, data.getTypeDetailId());
        values.put(COLUMN_NAME_URL, data.getUrl());
        values.put(COLUMN_NAME_DETAIL, data.getDetail());
        setDate(values, COLUMN_NAME_CREATE_DATE, data.getCreateDate());
        setDate(values, COLUMN_NAME_UPDATE_DATE, data.getUpdateDate());

        db.insert(TABLE_NAME, null, values);
    }

    private void setDate(ContentValues values, String key, String dateText) {
        if(TextUtils.isEmpty(dateText)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FORMAT_YYYYMMDDHHMMSS);
                String date = sdf.format(new Date());
                values.put(key, date);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } else {
            values.put(key, dateText);
        }
    }

}
