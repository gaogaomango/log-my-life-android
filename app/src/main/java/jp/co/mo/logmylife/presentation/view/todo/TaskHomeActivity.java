package jp.co.mo.logmylife.presentation.view.todo;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
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
import jp.co.mo.logmylife.common.util.DateUtil;
import jp.co.mo.logmylife.common.util.Logger;
import jp.co.mo.logmylife.domain.repository.TaskTableHelper;
import jp.co.mo.logmylife.presentation.view.main.MainActivity;
import jp.co.mo.logmylife.presentation.view.map.MapActivity;

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

    private TaskTableHelper mTaskTableHelper;

    private ArrayList<HashMap<String, String>> mTodayList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> mTomorrowList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> mUpcomingList = new ArrayList<>();

    public static String KEY_ID = "mId";
    public static String KEY_TASK = "task";
    public static String KEY_DATE = "dateStr";
    public static String KEY_IS_UPDATE = "mIsUpdate";

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        Logger.debug(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.task_home);
        ButterKnife.bind(this);

        mTaskTableHelper = new TaskTableHelper(this);
    }

    public void openAddTask(View v) {
        Logger.debug(TAG, "openAddTask");
        startIntentWithSlideAnimation(this, AddTaskActivity.class);
    }

    public void populateDate() {
        Logger.debug(TAG, "populateDate");
        mTaskTableHelper = new TaskTableHelper(this);
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
            String xml = "";

            // TODO: move to usecase and repository;
            Cursor today = mTaskTableHelper.getDataToday();
            loadDataList(today, mTodayList);

            // TODO: move to usecase and repository;
            Cursor tmr = mTaskTableHelper.getDataTomorrow();
            loadDataList(tmr, mTomorrowList);

            // TODO: move to usecase and repository;
            Cursor upcoming = mTaskTableHelper.getDataUpcoming();
            loadDataList(upcoming, mUpcomingList);

            return xml;
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

        private void loadListView(ListView listView, final ArrayList<HashMap<String, String>> dataList) {
            ListTaskAdapter adapter = new ListTaskAdapter(getApplicationContext(), dataList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(getApplicationContext(), AddTaskActivity.class);
                    i.putExtra(KEY_IS_UPDATE, true);
                    i.putExtra(KEY_ID, dataList.get(+position).get(KEY_ID));
                    startIntentWithSlideAnimation(TaskHomeActivity.this, i);
                }
            });
        }

    }
}
