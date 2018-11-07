package jp.co.mo.logmylife.presentation.view.main;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.mo.logmylife.AbstractBaseActivity;
import jp.co.mo.logmylife.R;
import jp.co.mo.logmylife.common.util.Logger;
import jp.co.mo.logmylife.presentation.view.map.MapActivity;
import jp.co.mo.logmylife.presentation.view.todo.TaskHomeActivity;

public class MainActivity extends AbstractBaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

//    private FirebaseAuth mAuth;

    private Context mContext;
    private Activity mActivity;
    @BindView(R.id.logout_button) Button mLogoutButton;
    @BindView(R.id.menu_map) Button mMenuMap;
    @BindView(R.id.menu_todo) Button mMenuTodo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        mActivity = this;

//        mAuth = FirebaseAuth.getInstance();

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mAuth.signOut();
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
                Logger.error(TAG, "It's logout menu!");
                Toast.makeText(mContext, "It's stopped for now", Toast.LENGTH_LONG).show();
            }
        });
        mMenuMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntentWithSlideAnimation(MainActivity.this, MapActivity.class);
            }
        });
        mMenuTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntentWithSlideAnimation(MainActivity.this, TaskHomeActivity.class);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(mAuth == null) {
//            mAuth = FirebaseAuth.getInstance();
//        }
    }
}
