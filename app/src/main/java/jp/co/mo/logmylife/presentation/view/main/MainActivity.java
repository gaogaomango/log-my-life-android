package jp.co.mo.logmylife.presentation.view.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import jp.co.mo.logmylife.AbstractBaseActivity;
import jp.co.mo.logmylife.R;
import jp.co.mo.logmylife.presentation.view.login.LoginActivity;
import jp.co.mo.logmylife.presentation.view.map.MapActivity;

public class MainActivity extends AbstractBaseActivity {

    private FirebaseAuth mAuth;

    private Button logoutButton;

    private Button menuMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        menuMap = findViewById(R.id.menu_map);
        menuMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }
    }
}
