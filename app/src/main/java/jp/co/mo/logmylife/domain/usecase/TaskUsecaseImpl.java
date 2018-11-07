package jp.co.mo.logmylife.domain.usecase;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.mo.logmylife.domain.repository.TaskDataRepository;

public class TaskUsecaseImpl extends AbstractUseCase implements TaskUsecase {

    private TaskDataRepository mTaskDataRepository;

    public TaskUsecaseImpl(Context context) {
        mTaskDataRepository = new TaskDataRepository(context);
    }

    public void loadTodayDataList(ArrayList<HashMap<String, String>> dataList) {
        mTaskDataRepository.loadTodayDataList(dataList);
    }

    public void loadTomorrowDataList(ArrayList<HashMap<String, String>> dataList) {
        mTaskDataRepository.loadTomorrowDataList(dataList);
    }

    public void loadUpcomingDataList(ArrayList<HashMap<String, String>> dataList) {
        mTaskDataRepository.loadUpcomingDataList(dataList);
    }

}
