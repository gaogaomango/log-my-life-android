package jp.co.mo.logmylife.presentation.view.todo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.mo.logmylife.AbstractBaseActivity;
import jp.co.mo.logmylife.R;
import jp.co.mo.logmylife.common.util.Logger;
import jp.co.mo.logmylife.domain.repository.TaskDataRepository;
import jp.co.mo.logmylife.domain.usecase.TaskUsecaseImpl;

public class TaskHomeActivity extends AbstractBaseActivity {

    private static final String TAG = TaskHomeActivity.class.getSimpleName();

    @BindView(R.id.scrollView) NestedScrollView mScrollView;
    @BindView(R.id.loader) ProgressBar mLoader;
    @BindView(R.id.taskListToday) NoScrollListView mTaskListToday;
    @BindView(R.id.taskListTomorrow) NoScrollListView mTaskListTomorow;
    @BindView(R.id.taskListUpcoming) NoScrollListView mTaskListUpcoming;
    @BindView(R.id.todayText) TextView mTodayText;
    @BindView(R.id.tomorrowText) TextView mTomorrowText;
    @BindView(R.id.upcomingText) TextView mUpcomingText;

    private TaskUsecaseImpl mTaskUsecase;

    private ArrayList<HashMap<String, String>> mTodayList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> mTomorrowList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> mUpcomingList = new ArrayList<>();

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        Logger.debug(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.task_home);
        ButterKnife.bind(this);
        mTaskUsecase = new TaskUsecaseImpl(this);
    }

    public void openAddTask(View v) {
        Logger.debug(TAG, "openAddTask");
        startIntentWithSlideAnimation(this, AddTaskActivity.class);
    }

    public void populateDate() {
        Logger.debug(TAG, "populateDate");
        mScrollView.setVisibility(View.GONE);
        mLoader.setVisibility(View.VISIBLE);

        LoadTask loadTask = new LoadTask();
        loadTask.execute();
    }

    public void finishActivity(View v) {
        Logger.debug(TAG, "finishActivity");
        onBackPressed();
    }

    @Override
    public void onResume() {
        Logger.debug(TAG, "onResume");
        super.onResume();

        populateDate();
    }

    class LoadTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mTodayList.clear();
            mTomorrowList.clear();
            mUpcomingList.clear();
        }

        @Override
        protected String doInBackground(String... strings) {
            mTaskUsecase.loadTodayDataList(mTodayList);
            mTaskUsecase.loadTomorrowDataList(mTomorrowList);
            mTaskUsecase.loadUpcomingDataList(mUpcomingList);

            return null;
        }

        @Override
        protected void onPostExecute(String xml) {
            loadListView(mTaskListToday, mTodayList);
            loadListView(mTaskListTomorow, mTomorrowList);
            loadListView(mTaskListUpcoming, mUpcomingList);

            if(mTodayList.size() > 0) {
                mTodayText.setVisibility(View.VISIBLE);
            } else {
                mTodayText.setVisibility(View.GONE);
            }

            if(mTomorrowList.size() > 0) {
                mTomorrowText.setVisibility(View.VISIBLE);
            } else {
                mTomorrowText.setVisibility(View.GONE);
            }

            if(mUpcomingList.size() > 0) {
                mUpcomingText.setVisibility(View.VISIBLE);
            } else {
                mUpcomingText.setVisibility(View.GONE);
            }

            mLoader.setVisibility(View.GONE);
            mScrollView.setVisibility(View.VISIBLE);

        }

        private void loadListView(ListView listView, final ArrayList<HashMap<String, String>> dataList) {
            ListTaskAdapter adapter = new ListTaskAdapter(getApplicationContext(), dataList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(getApplicationContext(), AddTaskActivity.class);
                    i.putExtra(TaskDataRepository.KEY_IS_UPDATE, true);
                    i.putExtra(TaskDataRepository.KEY_ID, dataList.get(+position).get(TaskDataRepository.KEY_ID));
                    startIntentWithSlideAnimation(TaskHomeActivity.this, i);
                }
            });
        }

    }
}
