package jp.co.mo.logmylife.domain.usecase;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskUsecase {

    public void loadTodayDataList(ArrayList<HashMap<String, String>> dataList);

    public void loadTomorrowDataList(ArrayList<HashMap<String, String>> dataList);

    public void loadUpcomingDataList(ArrayList<HashMap<String, String>> dataList);

}
