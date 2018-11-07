package jp.co.mo.logmylife.domain.usecase;

import java.util.ArrayList;
import java.util.Map;

public interface TaskUsecase {

    public void loadTodayDataList(ArrayList<Map<String, String>> dataList);

    public void loadTomorrowDataList(ArrayList<Map<String, String>> dataList);

    public void loadUpcomingDataList(ArrayList<Map<String, String>> dataList);

    public Map<String, Object> getDataSpecific(String keyId);

    public void updateContact(String mId, String mNameFinal, String mDateFinal);

    public void insertContact(String mNameFinal, String mDateFinal);

}
