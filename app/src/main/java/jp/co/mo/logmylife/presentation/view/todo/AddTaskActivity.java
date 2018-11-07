package jp.co.mo.logmylife.presentation.view.todo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.mo.logmylife.AbstractBaseActivity;
import jp.co.mo.logmylife.R;
import jp.co.mo.logmylife.common.util.DateUtil;
import jp.co.mo.logmylife.common.util.Logger;
import jp.co.mo.logmylife.domain.repository.TaskDataRepository;
import jp.co.mo.logmylife.domain.usecase.TaskUsecaseImpl;

public class AddTaskActivity extends AbstractBaseActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = AddTaskActivity.class.getSimpleName();
    private static final String FRAGMENT_START_DATE_PICKER_DIALOG_TAG = "startDatepickerdialog";

    private TaskUsecaseImpl mTaskUsecase;
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
        Logger.debug(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_add_new);
        ButterKnife.bind(this);

        mTaskUsecase = new TaskUsecaseImpl(this);
        mIntent = getIntent();
        mIsUpdate = mIntent.getBooleanExtra(TaskDataRepository.KEY_IS_UPDATE, false);

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
        Logger.debug(TAG, "initUpdate");
        mId = mIntent.getStringExtra(TaskDataRepository.KEY_ID);
        mToolbarTaskAddTitle.setText(this.getResources().getText(R.string.todo_update));

        Map<String, Object> data = mTaskUsecase.getDataSpecific(mId);
        if(data != null) {
            mTaskName.setText((String) data.get(TaskDataRepository.KEY_TASK_NAME));
            String calStr = (String) data.get(TaskDataRepository.KEY_CALENDAR);
            if(!TextUtils.isEmpty(calStr)) {
                Calendar cal = DateUtil.Epoch2Calender(calStr);
                mStartYear = cal.get(Calendar.YEAR);
                mStartMonth = cal.get(Calendar.MONTH);
                mStartDay = cal.get(Calendar.DAY_OF_MONTH);
                mTaskDate.setText(DateUtil.Epoch2DateString(calStr, DateUtil.FORMAT_DIVIDE_SLUSH_DDMMYYY));
            }
        }
    }

    public void closeAddTask(View v) {
        Logger.debug(TAG, "closeAddTask");
        finish();
    }

    public void doneAddTask(View v) {
        Logger.debug(TAG, "doneAddTask");
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
                mTaskUsecase.updateContact(mId, mNameFinal, mDateFinal);
                Toast.makeText(getApplicationContext(), "Task Updated.", Toast.LENGTH_SHORT).show();
            } else {
                mTaskUsecase.insertContact(mNameFinal, mDateFinal);
                Toast.makeText(getApplicationContext(), "Task Added.", Toast.LENGTH_SHORT).show();
            }
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Try Again.", Toast.LENGTH_SHORT).show();
        }
    }

    public void showStartDatePicker(View v) {
        Logger.debug(TAG, "showStartDatePicker");
        mDPD = DatePickerDialog.newInstance(AddTaskActivity.this, mStartYear, mStartMonth, mStartDay);
        mDPD.setOnDateSetListener(this);
        mDPD.show(getFragmentManager(), FRAGMENT_START_DATE_PICKER_DIALOG_TAG);
    }

    @Override
    public void onResume() {
        Logger.debug(TAG, "onResume");
        super.onResume();
        mDPD = (DatePickerDialog) getFragmentManager().findFragmentByTag(FRAGMENT_START_DATE_PICKER_DIALOG_TAG);
        if(mDPD != null) {
            mDPD.setOnDateSetListener(this);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Logger.debug(TAG, "onDateSet");
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
