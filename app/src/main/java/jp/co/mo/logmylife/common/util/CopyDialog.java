package jp.co.mo.logmylife.common.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import jp.co.mo.logmylife.R;

public class CopyDialog extends Dialog implements View.OnClickListener {


    private Activity mActivity;
    private Dialog mDialog;
    private String mMessage;
    private TextView dialogText;
    private Button copy, exit;

    public CopyDialog(Activity activity) {
        super(activity);
        this.mActivity = activity;
    }

    public CopyDialog(Activity activity, String message) {
        super(activity);
        this.mActivity = activity;
        this.mMessage = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        dialogText = findViewById(R.id.txt_dialog);
        if(!TextUtils.isEmpty(mMessage)) {
          dialogText.setText(mMessage);
        }
        copy = findViewById(R.id.btn_copy);
        exit = findViewById(R.id.btn_exit);
        copy.setOnClickListener(this);
        exit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_copy:
                if(!TextUtils.isEmpty(mMessage)) {
                    ClipboardManager clipboard = (ClipboardManager)
                            mActivity.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("copy_message", mMessage);
                    clipboard.setPrimaryClip(clip);
                }
                dismiss();
                break;
            case R.id.btn_exit:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }}
