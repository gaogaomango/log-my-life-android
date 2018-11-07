package jp.co.mo.logmylife.presentation.view.todo;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.mo.logmylife.R;
import jp.co.mo.logmylife.common.util.DateUtil;
import jp.co.mo.logmylife.domain.repository.TaskTableHelper;

public class TaskHomeActivity extends AppCompatActivity {

    @BindView(R.id.scrollView) NestedScrollView scrollView;
    @BindView(R.id.loader) ProgressBar loader;
    @BindView(R.id.taskListToday) NoScrollListView taskListToday;
    @BindView(R.id.taskListTomorrow) NoScrollListView taskListTomorow;
    @BindView(R.id.taskListUpcoming) NoScrollListView taskListUpcoming;
    @BindView(R.id.todayText) TextView todayText;
    @BindView(R.id.tomorrowText) TextView tomorrowText;
    @BindView(R.id.upcomingText) TextView upcomingText;

    TaskTableHelper mydb;

    ArrayList<HashMap<String, String>> todayList = new ArrayList<>();
    ArrayList<HashMap<String, String>> tomorrowList = new ArrayList<>();
    ArrayList<HashMap<String, String>> upcomingList = new ArrayList<>();

    public static String KEY_ID = "id";
    public static String KEY_TASK = "task";
    public static String KEY_DATE = "dateStr";
    public static String KEY_IS_UPDATE = "isUpdate";

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_home);
        ButterKnife.bind(this);

        mydb = new TaskTableHelper(this);
    }

    public void openAddTask(View v) {
        Intent i = new Intent(this, AddTaskActivity.class);
        startActivity(i);
    }

    public void populateDate() {
        mydb = new TaskTableHelper(this);
        scrollView.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);

        LoadTask loadTask = new LoadTask();
        loadTask.execute();
    }

    @Override
    public void onResume() {
        super.onResume();

        populateDate();
    }

    class LoadTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            todayList.clear();
            tomorrowList.clear();
            upcomingList.clear();
        }

        @Override
        protected String doInBackground(String... strings) {
            String xml = "";

            // TODO: move to usecase and repository;
            Cursor today = mydb.getDataToday();
            loadDataList(today, todayList);

            // TODO: move to usecase and repository;
            Cursor tmr = mydb.getDataTomorrow();
            loadDataList(tmr, tomorrowList);

            // TODO: move to usecase and repository;
            Cursor upcoming = mydb.getDataUpcoming();
            loadDataList(upcoming, upcomingList);

            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {
            loadListView(taskListToday, todayList);
            loadListView(taskListTomorow, tomorrowList);
            loadListView(taskListUpcoming, upcomingList);

            if(todayList.size() > 0) {
                todayText.setVisibility(View.VISIBLE);
            } else {
                todayText.setVisibility(View.GONE);
            }

            if(tomorrowList.size() > 0) {
                tomorrowText.setVisibility(View.VISIBLE);
            } else {
                tomorrowText.setVisibility(View.GONE);
            }

            if(upcomingList.size() > 0) {
                upcomingText.setVisibility(View.VISIBLE);
            } else {
                upcomingText.setVisibility(View.GONE);
            }

            loader.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);

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
                    startActivity(i);
                }
            });
        }

    }
}
