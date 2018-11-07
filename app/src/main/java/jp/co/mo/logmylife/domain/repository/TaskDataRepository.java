package jp.co.mo.logmylife.domain.repository;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.co.mo.logmylife.common.util.DateUtil;

public class TaskDataRepository {

    private TaskTableHelper mTaskTableHelper;

    public static String KEY_ID = "mId";
    public static String KEY_TASK = "task";
    public static String KEY_DATE = "dateStr";
    public static String KEY_IS_UPDATE = "mIsUpdate";

    private static final int TASK_NAME_INDEX = 1;
    private static final int CALENDAR_INDEX = TASK_NAME_INDEX + 1;

    public static String KEY_TASK_NAME = "taskName";
    public static String KEY_CALENDAR = "calendar";

    public TaskDataRepository(Context context) {
        mTaskTableHelper = new TaskTableHelper(context);
    }

    public void loadTodayDataList(ArrayList<Map<String, String>> dataList) {
        loadDataList(mTaskTableHelper.getDataToday(), dataList);
    }

    public void loadTomorrowDataList(ArrayList<Map<String, String>> dataList) {
        loadDataList(mTaskTableHelper.getDataTomorrow(), dataList);
    }

    public void loadUpcomingDataList(ArrayList<Map<String, String>> dataList) {
        loadDataList(mTaskTableHelper.getDataUpcoming(), dataList);
    }

    private void loadDataList(Cursor cursor, ArrayList<Map<String, String>> dataList) {
        if(cursor == null) {
            return;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Map<String, String> mapToday = new HashMap<>();
            mapToday.put(KEY_ID, cursor.getString(0).toString());
            mapToday.put(KEY_TASK, cursor.getString(1).toString());
            mapToday.put(KEY_DATE, DateUtil.Epoch2DateString(cursor.getString(2).toString(), DateUtil.FORMAT_DIVIDE_HYPEHEN_DDMMYYY));
            dataList.add(mapToday);
            cursor.moveToNext();
        }
    }

    public Map<String, Object> getDataSpecific(String keyId) {
        Map<String, Object> map = new HashMap<>();
        Cursor task = mTaskTableHelper.getDataSpecific(keyId);
        if(task != null) {
            task.moveToFirst();
            map.put(KEY_TASK_NAME, task.getString(TASK_NAME_INDEX).toString());
            map.put(KEY_CALENDAR, task.getString(CALENDAR_INDEX).toString());
        }

        return map;
    }

    public void updateContact(String mId, String mNameFinal, String mDateFinal) {
        mTaskTableHelper.updateContact(mId, mNameFinal, mDateFinal);
    }

    public void insertContact(String mNameFinal, String mDateFinal) {
        mTaskTableHelper.insertContact(mNameFinal, mDateFinal);
    }

}
