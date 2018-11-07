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
    TaskTableHelper mydb;
    DatePickerDialog dpd;
    int startYear = 0, startMonth = 0, startDay = 0;
    String dateFinal;
    String nameFinal;

    Intent intent;
    Boolean isUpdate;
    String id;

    @BindView(R.id.toolbar_task_add_title) TextView toolbarTaskAddTitle;
    @BindView(R.id.task_name) EditText taskName;
    @BindView(R.id.task_date) EditText taskDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_add_new);

        ButterKnife.bind(this);

        // TODO: refuctaring

        mydb = new TaskTableHelper(getApplicationContext(), null, null, 0);
        intent = getIntent();
        isUpdate = intent.getBooleanExtra(TaskHomeActivity.KEY_IS_UPDATE, false);

        dateFinal = DateUtil.todayDateString();
        Date yourDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(yourDate);
        startYear = cal.get(Calendar.YEAR);
        startMonth = cal.get(Calendar.MONTH);
        startDay = cal.get(Calendar.DAY_OF_MONTH);

        if (isUpdate) {
            initUpdate();
        }
    }

    private void initUpdate() {
        id = intent.getStringExtra(TaskHomeActivity.KEY_ID);
        // TODO: change to constant
        toolbarTaskAddTitle.setText("Update");

        // TODO: move to usecase and repository
        Cursor task = mydb.getDataSpecific(id);
        if(task != null) {
            task.moveToFirst();

            taskName .setText(task.getString(1).toString());
            Calendar cal = DateUtil.Epoch2Calender(task.getString(2).toString());
            startYear = cal.get(Calendar.YEAR);
            startMonth = cal.get(Calendar.MONTH);
            startDay = cal.get(Calendar.DAY_OF_MONTH);
            taskDate.setText(DateUtil.Epoch2DateString(task.getString(2).toString(), DateUtil.FORMAT_DIVIDE_SLUSH_DDMMYYY));
        }
    }

    public void closeAddTask(View v) {
        finish();
    }

    public void doneAddTask(View v) {
        int errorStep = 0;
        nameFinal = taskName.getText().toString();
        dateFinal = taskDate.getText().toString();

        if (nameFinal.trim().length() < 1) {
            errorStep++;
            taskName.setError("Provide a task name.");
        }

        if(dateFinal.trim().length() < 4) {
            errorStep++;
            taskDate.setError("Provide a specific date");
        }

        if(errorStep == 0) {
            if (isUpdate) {
                mydb.updateContact(id, nameFinal, dateFinal);
                Toast.makeText(getApplicationContext(), "Task Updated.", Toast.LENGTH_SHORT).show();
            } else {
                mydb.insertContact(nameFinal, dateFinal);
                Toast.makeText(getApplicationContext(), "Task Added.", Toast.LENGTH_SHORT).show();
            }
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Try Again.", Toast.LENGTH_SHORT).show();
        }
    }

    public void showStartDatePicker(View v) {
        dpd = DatePickerDialog.newInstance(AddTaskActivity.this, startYear, startMonth, startDay);
        dpd.setOnDateSetListener(this);
        dpd.show(getFragmentManager(), FRAGMENT_START_DATE_PICKER_DIALOG_TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag(FRAGMENT_START_DATE_PICKER_DIALOG_TAG);
        if(dpd != null) {
            dpd.setOnDateSetListener(this);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        startYear = year;
        startMonth = monthOfYear;
        startDay = dayOfMonth;
        int monthAddOne = startMonth + 1;
        String date = (startDay < 10 ? "0" + startDay : "" + startDay) +
                "/" + (monthAddOne < 10 ? "0" + monthAddOne : "" + monthAddOne) +
                "/" + startYear;
        taskDate.setText(date);
    }

}
