package jp.co.mo.logmylife.domain.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.mo.logmylife.common.util.DateUtil;
import jp.co.mo.logmylife.domain.entity.map.MapPlaceData;
import jp.co.mo.logmylife.domain.entity.map.MapPlacePicData;

public class MapDataTableHelper extends AbstractDataTableHelper {

    protected static final String TABLE_NAME = "mapInfo";
    protected static final String COLUMN_NAME_ID = "_id";
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

    private static final String SQL_SANITIZE_LETTER = " = ?";

    protected static final int COLUMN_NAME_ID_NUM = 0;
    protected static final int COLUMN_NAME_USER_ID_NUM = COLUMN_NAME_ID_NUM + 1;
    protected static final int COLUMN_NAME_TITLE_NUM = COLUMN_NAME_USER_ID_NUM + 1;
    protected static final int COLUMN_NAME_LAT_NUM = COLUMN_NAME_TITLE_NUM + 1;
    protected static final int COLUMN_NAME_LNG_NUM = COLUMN_NAME_LAT_NUM + 1;
    protected static final int COLUMN_NAME_TYPE_ID_NUM = COLUMN_NAME_LNG_NUM + 1;
    protected static final int COLUMN_NAME_TYPE_DETAIL_ID_NUM = COLUMN_NAME_TYPE_ID_NUM + 1;
    protected static final int COLUMN_NAME_URL_NUM = COLUMN_NAME_TYPE_DETAIL_ID_NUM + 1;
    protected static final int COLUMN_NAME_DETAIL_NUM = COLUMN_NAME_URL_NUM + 1;
    protected static final int COLUMN_NAME_CREATE_DATE_NUM = COLUMN_NAME_DETAIL_NUM + 1;
    protected static final int COLUMN_NAME_UPDATE_DATE_NUM = COLUMN_NAME_CREATE_DATE_NUM + 1;

    protected static final String PIC_TABLE_NAME = "mapPicInfo";
    protected static final String COLUMN_NAME_PIC_ID = "_id";
    protected static final String COLUMN_NAME_PIC_MAP_PLACE_ID = "mapPlaceId";
    protected static final String COLUMN_NAME_PIC_TITLE = "title";
    protected static final String COLUMN_NAME_PIC_FILE_PATH = "filePath";
    protected static final String COLUMN_NAME_PIC_CREATE_DATE = "createDate";
    protected static final String COLUMN_NAME_PIC_UPDATE_DATE = "updateDate";

    protected static final int COLUMN_NAME_PIC_ID_NUM = 0;
    protected static final int COLUMN_NAME_PIC_MAP_PLACE_ID_NUM = COLUMN_NAME_PIC_ID_NUM + 1;
    protected static final int COLUMN_NAME_PIC_TITLE_NUM = COLUMN_NAME_PIC_MAP_PLACE_ID_NUM + 1;
    protected static final int COLUMN_NAME_PIC_FILE_PATH_NUM = COLUMN_NAME_PIC_TITLE_NUM + 1;
    protected static final int COLUMN_NAME_PIC_CREATE_DATE_NUM = COLUMN_NAME_PIC_FILE_PATH_NUM + 1;
    protected static final int COLUMN_NAME_PIC_UPDATE_DATE_NUM = COLUMN_NAME_PIC_CREATE_DATE_NUM + 1;

    public static final String CREATE_MAP_INFO_TABLE_SQL =
            "CREATE TABLE  IF NOT EXISTS " + TABLE_NAME + " (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
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

    public static final String DROP_MAP_INFO_TABLE_SQL =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String CREATE_MAP_PIC_INFO_TABLE_SQL =
            "CREATE TABLE  IF NOT EXISTS " + PIC_TABLE_NAME + " (" +
                    COLUMN_NAME_PIC_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_PIC_MAP_PLACE_ID + " INTEGER NOT NULL," +
                    COLUMN_NAME_PIC_TITLE + " TEXT," +
                    COLUMN_NAME_PIC_FILE_PATH + " TEXT NOT NULL," +
                    COLUMN_NAME_PIC_CREATE_DATE + " DATETIME default current_timestamp," +
                    COLUMN_NAME_PIC_UPDATE_DATE + "  DATETIME default current_timestamp)";

    public static final String DROP_MAP_PIC_INFO_TABLE_SQL =
            "DROP TABLE IF EXISTS " + PIC_TABLE_NAME;


    public static final String SQL_LAST_INSERTED_RECORD_ENTRIES =
            "SELECT " + COLUMN_NAME_ID + ", " +
                    COLUMN_NAME_USER_ID + ", " +
                    COLUMN_NAME_TITLE + ", " +
                    COLUMN_NAME_LAT + ", " +
                    COLUMN_NAME_LNG + ", " +
                    COLUMN_NAME_TYPE_ID + ", " +
                    COLUMN_NAME_TYPE_DETAIL_ID + ", " +
                    COLUMN_NAME_URL + ", " +
                    COLUMN_NAME_DETAIL + ", " +
                    COLUMN_NAME_CREATE_DATE + ", " +
                    COLUMN_NAME_UPDATE_DATE +
                    " FROM " + TABLE_NAME +
                    " ORDER BY " + COLUMN_NAME_ID + " DESC LIMIT 1";

    public MapDataTableHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public MapPlaceData getLastInsertedRecord(SQLiteDatabase db) {
        Cursor c = db.rawQuery(SQL_LAST_INSERTED_RECORD_ENTRIES, null);
        if (c != null && c.moveToFirst()) {
            MapPlaceData data = new MapPlaceData();
            data.setId(c.getInt(MapDataTableHelper.COLUMN_NAME_ID_NUM));
            data.setUserId(c.getString(MapDataTableHelper.COLUMN_NAME_USER_ID_NUM));
            data.setTitle(c.getString(MapDataTableHelper.COLUMN_NAME_TITLE_NUM));
            data.setLat(c.getDouble(MapDataTableHelper.COLUMN_NAME_LAT_NUM));
            data.setLng(c.getDouble(MapDataTableHelper.COLUMN_NAME_LNG_NUM));
            data.setTypeId(c.getInt(MapDataTableHelper.COLUMN_NAME_TYPE_ID_NUM));
            data.setTypeDetailId(c.getInt(MapDataTableHelper.COLUMN_NAME_TYPE_DETAIL_ID_NUM));
            data.setPicList(readMapPlacePicDatas(db, c.getInt(MapDataTableHelper.COLUMN_NAME_ID_NUM)));
            data.setUrl(c.getString(MapDataTableHelper.COLUMN_NAME_URL_NUM));
            data.setDetail(c.getString(MapDataTableHelper.COLUMN_NAME_DETAIL_NUM));
            data.setCreateDate(c.getString(MapDataTableHelper.COLUMN_NAME_CREATE_DATE_NUM));
            data.setUpdateDate(c.getString(MapDataTableHelper.COLUMN_NAME_UPDATE_DATE_NUM));

            return data;
        }
        return null;
    }

    public List<MapPlacePicData> readMapPlacePicDatas(SQLiteDatabase db, Integer placeId) {
        List<MapPlacePicData> list = new ArrayList<>();
        Cursor cursor = db.query(
                MapDataTableHelper.PIC_TABLE_NAME,
                new String[]{MapDataTableHelper.COLUMN_NAME_PIC_ID,
                        MapDataTableHelper.COLUMN_NAME_PIC_MAP_PLACE_ID,
                        MapDataTableHelper.COLUMN_NAME_PIC_TITLE,
                        MapDataTableHelper.COLUMN_NAME_PIC_FILE_PATH,
                        MapDataTableHelper.COLUMN_NAME_PIC_CREATE_DATE,
                        MapDataTableHelper.COLUMN_NAME_PIC_UPDATE_DATE},
                MapDataTableHelper.COLUMN_NAME_PIC_MAP_PLACE_ID + "= ?",
                new String[]{placeId.toString()},
                null,
                null,
                null
        );

        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount(); i++) {
            MapPlacePicData data = new MapPlacePicData();
            data.setId(cursor.getInt(MapDataTableHelper.COLUMN_NAME_PIC_ID_NUM));
            data.setMapPlaceId(cursor.getInt(MapDataTableHelper.COLUMN_NAME_PIC_MAP_PLACE_ID_NUM));
            data.setTitle(cursor.getString(MapDataTableHelper.COLUMN_NAME_PIC_TITLE_NUM));
            data.setFilePath(cursor.getString(MapDataTableHelper.COLUMN_NAME_PIC_FILE_PATH_NUM));
            data.setCreateDate(cursor.getString(MapDataTableHelper.COLUMN_NAME_PIC_CREATE_DATE_NUM));
            data.setUpdateDate(cursor.getString(MapDataTableHelper.COLUMN_NAME_PIC_UPDATE_DATE_NUM));
            cursor.moveToNext();
            list.add(data);
        }

        cursor.close();

        return list;
    }

    public void saveData(SQLiteDatabase db, String userId, MapPlaceData data) {
        ContentValues values = new ContentValues();
        if(data.getId() != null) {
            values.put(COLUMN_NAME_ID, data.getId());
        }
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

        // TODO: recognize and check user id.

        db.insert(TABLE_NAME, null, values);
    }

    public void removeData(SQLiteDatabase db, String userId, Integer placeId) {
        // TODO: recognize and check user id.
        if(placeId == null) {
            return;
        }
        db.delete(TABLE_NAME, COLUMN_NAME_ID + SQL_SANITIZE_LETTER, new String[]{placeId.toString()});
    }


    public void savePicData(SQLiteDatabase db, String userId, MapPlaceData data) {
        // TODO: recognize and check user id.
        if(data.getPicList() == null || data.getPicList().isEmpty()) {
            return;
        }

        if(data.getId() == null) {
            return;
        }

        for(MapPlacePicData pic : data.getPicList()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_PIC_MAP_PLACE_ID, data.getId());
            if(!TextUtils.isEmpty(pic.getTitle())) {
                values.put(COLUMN_NAME_PIC_TITLE, pic.getTitle());
            }
            values.put(COLUMN_NAME_PIC_FILE_PATH, pic.getFilePath());
            setDate(values, COLUMN_NAME_CREATE_DATE, pic.getCreateDate());
            setDate(values, COLUMN_NAME_UPDATE_DATE, pic.getUpdateDate());
            db.insert(PIC_TABLE_NAME, null, values);
        }

    }

    public void removePicData(SQLiteDatabase db, String userId, Integer placeId) {
        if(placeId == null) {
            return;
        }
        db.delete(PIC_TABLE_NAME, COLUMN_NAME_PIC_MAP_PLACE_ID + SQL_SANITIZE_LETTER, new String[]{placeId.toString()});
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
