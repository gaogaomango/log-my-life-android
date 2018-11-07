package jp.co.mo.logmylife.domain.usecase;

import android.content.Context;

import java.util.ArrayList;
import java.util.Map;

import jp.co.mo.logmylife.domain.repository.TaskDataRepository;

public class TaskUsecaseImpl extends AbstractUseCase implements TaskUsecase {

    private TaskDataRepository mTaskDataRepository;

    public TaskUsecaseImpl(Context context) {
        mTaskDataRepository = new TaskDataRepository(context);
    }

    public void loadTodayDataList(ArrayList<Map<String, String>> dataList) {
        mTaskDataRepository.loadTodayDataList(dataList);
    }

    public void loadTomorrowDataList(ArrayList<Map<String, String>> dataList) {
        mTaskDataRepository.loadTomorrowDataList(dataList);
    }

    public void loadUpcomingDataList(ArrayList<Map<String, String>> dataList) {
        mTaskDataRepository.loadUpcomingDataList(dataList);
    }

    public Map<String, Object> getDataSpecific(String keyId) {
        return mTaskDataRepository.getDataSpecific(keyId);
    }

    public void updateContact(String mId, String mNameFinal, String mDateFinal) {
        mTaskDataRepository.updateContact(mId, mNameFinal, mDateFinal);
    }

    public void insertContact(String mNameFinal, String mDateFinal) {
        mTaskDataRepository.insertContact(mNameFinal, mDateFinal);
    }

}
