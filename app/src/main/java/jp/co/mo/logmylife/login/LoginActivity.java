package jp.co.mo.logmylife.login;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import jp.co.mo.logmylife.R;
import jp.co.mo.logmylife.main.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private CallbackManager callbackManager;

    private Button createUserButton;
    private Button loginUserButton;

    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                    startActivity(intent);
                    return;
                } else {
                    // TODO sign out
                }
            }
        };

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);

        createUserButton = findViewById(R.id.create_user_button);
        createUserButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO: validation
                if (!TextUtils.isEmpty(emailEditText.getText()) && !TextUtils.isEmpty(passwordEditText.getText())) {
                    mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        // complete to create user
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Sorry, but failed to register. Please try again.", Toast.LENGTH_LONG);
                                    }
                                }
                            });
                }
            }
        });
        loginUserButton = findViewById(R.id.login_user_button);
        loginUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: validation
                if (!TextUtils.isEmpty(emailEditText.getText()) && !TextUtils.isEmpty(passwordEditText.getText())) {
                    mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // login completed
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Sorry, but failed to login. Please check your id and password, and try again later.", Toast.LENGTH_LONG);
                                    }
                                }
                            });
                }
            }
        });

        FingerprintManager fm = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        try {
            if (fm.isHardwareDetected() && fm.hasEnrolledFingerprints()) {
                fm.authenticate(null,
                        null,
                        0,
                        new FingerprintManager.AuthenticationCallback() {
                            @Override
                            public void onAuthenticationError(int errorCode, CharSequence errString) {
                                Log.e(TAG, "ERROR: " + errorCode + ":" + errString);
                                Toast.makeText(LoginActivity.this, "Sorry, could not recgnize your finger print. " + errString, Toast.LENGTH_LONG);
                            }

                            @Override
                            public void onAuthenticationFailed() {
                                Log.i(TAG, "Failed");
                                Toast.makeText(LoginActivity.this, "Sorry, could not recgnize your finger print. Please try again.", Toast.LENGTH_LONG);
                            }

                            @Override
                            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                                Toast.makeText(LoginActivity.this, "Login succeeded!", Toast.LENGTH_LONG);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }, null);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAuth == null) {
            mAuth = FirebaseAuth.getInstance();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
