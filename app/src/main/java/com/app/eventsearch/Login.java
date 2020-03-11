package com.app.eventsearch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.eventsearch.session.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {

    EditText Email, Password;
    String email, password;
    Button SignIn, register;

    SessionManager sessionManager;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();
        RootRef = FirebaseDatabase.getInstance().getReference();

        InitializeFields();

        if (sessionManager.isLogged()) {
            Intent mainIntent = new Intent(getApplication(), MainActivity.class);
            preferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
            String emailku = preferences.getString("email", "");
            Log.d("EmailLoginsession", emailku);
            startActivity(mainIntent);
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), Register.class);
                preferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
                startActivity(intent);
            }
        });

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = Email.getText().toString().trim();
                password = Password.getText().toString().trim();
                SignIn.setVisibility(View.GONE);
                loadingBar = new ProgressDialog(Login.this);
                loadingBar.setTitle("Sign In");
                loadingBar.setMessage("Please Wait....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Please write a Email...", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Please write a Password...", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
                else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                    }
                                }
                            });
                }
            }
        });
    }

    private void InitializeFields() {
        Email = findViewById(R.id.email_login);
        Password = findViewById(R.id.password_login);
        SignIn = findViewById(R.id.Sign_In);
        register = findViewById(R.id.register);
        sessionManager = new SessionManager(this);
    }
}
