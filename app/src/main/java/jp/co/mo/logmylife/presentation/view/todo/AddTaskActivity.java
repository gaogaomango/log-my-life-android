package jp.co.mo.logmylife.presentation.view.todo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.mo.logmylife.R;
import jp.co.mo.logmylife.common.util.DateUtil;
import jp.co.mo.logmylife.domain.repository.TaskTableHelper;

public class AddTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = AddTaskActivity.class.getSimpleName();
    private static final String FRAGMENT_START_DATE_PICKER_DIALOG_TAG = "startDatepickerdialog";

    // TODO: move to usecase
    private TaskTableHelper mTaskTableHelper;
    private DatePickerDialog mDPD;
    private int mStartYear = 0, mStartMonth = 0, mStartDay = 0;
    private String mDateFinal;
    private String mNameFinal;

    private Intent mIntent;
    private Boolean mIsUpdate;
    private String mId;

    @BindView(R.id.toolbar_task_add_title) TextView mToolbarTaskAddTitle;
    @BindView(R.id.task_name) EditText mTaskName;
    @BindView(R.id.task_date) EditText mTaskDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_add_new);

        ButterKnife.bind(this);

        // TODO: refuctaring

        mTaskTableHelper = new TaskTableHelper(getApplicationContext(), null, null, 0);
        mIntent = getIntent();
        mIsUpdate = mIntent.getBooleanExtra(TaskHomeActivity.KEY_IS_UPDATE, false);

        mDateFinal = DateUtil.todayDateString();
        Date yourDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(yourDate);
        mStartYear = cal.get(Calendar.YEAR);
        mStartMonth = cal.get(Calendar.MONTH);
        mStartDay = cal.get(Calendar.DAY_OF_MONTH);

        if (mIsUpdate) {
            initUpdate();
        }
    }

    private void initUpdate() {
        mId = mIntent.getStringExtra(TaskHomeActivity.KEY_ID);
        // TODO: change to constant
        mToolbarTaskAddTitle.setText("Update");

        // TODO: move to usecase and repository
        Cursor task = mTaskTableHelper.getDataSpecific(mId);
        if(task != null) {
            task.moveToFirst();

            mTaskName.setText(task.getString(1).toString());
            Calendar cal = DateUtil.Epoch2Calender(task.getString(2).toString());
            mStartYear = cal.get(Calendar.YEAR);
            mStartMonth = cal.get(Calendar.MONTH);
            mStartDay = cal.get(Calendar.DAY_OF_MONTH);
            mTaskDate.setText(DateUtil.Epoch2DateString(task.getString(2).toString(), DateUtil.FORMAT_DIVIDE_SLUSH_DDMMYYY));
        }
    }

    public void closeAddTask(View v) {
        finish();
    }

    public void doneAddTask(View v) {
        int errorStep = 0;
        mNameFinal = mTaskName.getText().toString();
        mDateFinal = mTaskDate.getText().toString();

        if (mNameFinal.trim().length() < 1) {
            errorStep++;
            mTaskName.setError("Provide a task name.");
        }

        if(mDateFinal.trim().length() < 4) {
            errorStep++;
            mTaskDate.setError("Provide a specific date");
        }

        if(errorStep == 0) {
            if (mIsUpdate) {
                mTaskTableHelper.updateContact(mId, mNameFinal, mDateFinal);
                Toast.makeText(getApplicationContext(), "Task Updated.", Toast.LENGTH_SHORT).show();
            } else {
                mTaskTableHelper.insertContact(mNameFinal, mDateFinal);
                Toast.makeText(getApplicationContext(), "Task Added.", Toast.LENGTH_SHORT).show();
            }
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Try Again.", Toast.LENGTH_SHORT).show();
        }
    }

    public void showStartDatePicker(View v) {
        mDPD = DatePickerDialog.newInstance(AddTaskActivity.this, mStartYear, mStartMonth, mStartDay);
        mDPD.setOnDateSetListener(this);
        mDPD.show(getFragmentManager(), FRAGMENT_START_DATE_PICKER_DIALOG_TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        mDPD = (DatePickerDialog) getFragmentManager().findFragmentByTag(FRAGMENT_START_DATE_PICKER_DIALOG_TAG);
        if(mDPD != null) {
            mDPD.setOnDateSetListener(this);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mStartYear = year;
        mStartMonth = monthOfYear;
        mStartDay = dayOfMonth;
        int monthAddOne = mStartMonth + 1;
        String date = (mStartDay < 10 ? "0" + mStartDay : "" + mStartDay) +
                "/" + (monthAddOne < 10 ? "0" + monthAddOne : "" + monthAddOne) +
                "/" + mStartYear;
        mTaskDate.setText(date);
    }

}
