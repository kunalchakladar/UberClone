package com.example.kunal.uberclone;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DriverSignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "DriverSignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(new Intent(DriverSignUpActivity.this, DriverActivity.class));
                    finish();

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        Window window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorDriver));

        final EditText email = (EditText) findViewById(R.id.driverEmail);
        final EditText password = (EditText) findViewById(R.id.driverPassword);

        email.setHintTextColor(getResources().getColor(R.color.colorEditTextHint));
        password.setHintTextColor(getResources().getColor(R.color.colorEditTextHint));

        TextView textView = (TextView) findViewById(R.id.textView3);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DriverSignUpActivity.this, DriverLogInActivity.class));

            }
        });

        ImageButton signUpBtn = (ImageButton) findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String driverEmail, driverPassword;
                long driverPhn;
                driverEmail = email.getText().toString();
                driverPassword = password.getText().toString();

                signUpDriver(driverEmail, driverPassword);

            }
        });

    }

    private void signUpDriver(final String driverEmail, String driverPassword) {

        mAuth.createUserWithEmailAndPassword(driverEmail, driverPassword)
                .addOnCompleteListener(DriverSignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Auth failed" + task.getException());
                        }
                        else{

                            Intent intent = new Intent(DriverSignUpActivity.this, DriverActivity.class);
                            startActivity(intent);
                            finish();

                        }

                    }
                });

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
