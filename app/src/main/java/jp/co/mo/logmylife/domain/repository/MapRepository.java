package jp.co.mo.logmylife.domain.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import jp.co.mo.logmylife.domain.entity.map.MapPlaceData;

public class MapRepository {

    private MapDataTableHelper mapDataTableHelper = null;

    public List<MapPlaceData> getInfoWindowDatas(Context context) {
        if(mapDataTableHelper == null) {
            mapDataTableHelper = new MapDataTableHelper(context);
        }
        List<MapPlaceData> list = readMapData();

        // TODO: picの情報も取り入れる事。

        return list;
    }

    private List<MapPlaceData> readMapData() {
        List<MapPlaceData> list = new ArrayList<>();
        SQLiteDatabase db = mapDataTableHelper.getReadableDatabase();
        Cursor cursor = db.query(
                MapDataTableHelper.TABLE_NAME,
        new String[]{MapDataTableHelper.COLUMN_NAME_USER_ID,
                MapDataTableHelper.COLUMN_NAME_TITLE,
                MapDataTableHelper.COLUMN_NAME_LAT,
                MapDataTableHelper.COLUMN_NAME_LNG,
                MapDataTableHelper.COLUMN_NAME_TYPE_ID,
                MapDataTableHelper.COLUMN_NAME_TYPE_DETAIL_ID,
                MapDataTableHelper.COLUMN_NAME_URL,
                MapDataTableHelper.COLUMN_NAME_DETAIL,
                MapDataTableHelper.COLUMN_NAME_CREATE_DATE,
                MapDataTableHelper.COLUMN_NAME_UPDATE_DATE},
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount(); i++) {
            MapPlaceData data = new MapPlaceData();
            data.setUserId(cursor.getString(MapDataTableHelper.COLUMN_NAME_USER_ID_NUM));
            data.setTitle(cursor.getString(MapDataTableHelper.COLUMN_NAME_TITLE_NUM));
            data.setLat(cursor.getDouble(MapDataTableHelper.COLUMN_NAME_LAT_NUM));
            data.setLng(cursor.getDouble(MapDataTableHelper.COLUMN_NAME_LNG_NUM));
            data.setTypeId(cursor.getInt(MapDataTableHelper.COLUMN_NAME_TYPE_ID_NUM));
            data.setTypeDetailId(cursor.getInt(MapDataTableHelper.COLUMN_NAME_TYPE_DETAIL_ID_NUM));
            // TODO: get pic info
            data.setUrl(cursor.getString(MapDataTableHelper.COLUMN_NAME_URL_NUM));
            data.setDetail(cursor.getString(MapDataTableHelper.COLUMN_NAME_DETAIL_NUM));
            data.setCreateDate(cursor.getString(MapDataTableHelper.COLUMN_NAME_CREATE_DATE_NUM));
            data.setUpdateDate(cursor.getString(MapDataTableHelper.COLUMN_NAME_UPDATE_DATE_NUM));
            cursor.moveToNext();
            list.add(data);
        }

        cursor.close();

        return list;
    }

    public void saveInfoWindowData(Context context, MapPlaceData placeData) {
        if(mapDataTableHelper == null) {
            mapDataTableHelper = new MapDataTableHelper(context);
        }

        saveMapData(placeData);
        // TODO: picの情報もsaveする事。
    }

    private void saveMapData(MapPlaceData placeData) {
        SQLiteDatabase db = mapDataTableHelper.getWritableDatabase();
        mapDataTableHelper.saveData(db, null, placeData);
    }


}