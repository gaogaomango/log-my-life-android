package jp.co.mo.logmylife.domain.repository;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.mo.logmylife.common.util.DateUtil;

public class TaskDataRepository {

    private TaskTableHelper mTaskTableHelper;

    public static String KEY_ID = "mId";
    public static String KEY_TASK = "task";
    public static String KEY_DATE = "dateStr";
    public static String KEY_IS_UPDATE = "mIsUpdate";

    public TaskDataRepository(Context context) {
        mTaskTableHelper = new TaskTableHelper(context);
    }

    public void loadTodayDataList(ArrayList<HashMap<String, String>> dataList) {
        loadDataList(mTaskTableHelper.getDataToday(), dataList);
    }

    public void loadTomorrowDataList(ArrayList<HashMap<String, String>> dataList) {
        loadDataList(mTaskTableHelper.getDataTomorrow(), dataList);
    }

    public void loadUpcomingDataList(ArrayList<HashMap<String, String>> dataList) {
        loadDataList(mTaskTableHelper.getDataUpcoming(), dataList);
    }

    private void loadDataList(Cursor cursor, ArrayList<HashMap<String, String>> dataList) {
        if(cursor == null) {
            return;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            HashMap<String, String> mapToday = new HashMap<>();
            mapToday.put(KEY_ID, cursor.getString(0).toString());
            mapToday.put(KEY_TASK, cursor.getString(1).toString());
            mapToday.put(KEY_DATE, DateUtil.Epoch2DateString(cursor.getString(2).toString(), DateUtil.FORMAT_DIVIDE_HYPEHEN_DDMMYYY));
            dataList.add(mapToday);
            cursor.moveToNext();
        }
    }

}
