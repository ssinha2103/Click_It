package com.sudarshan.clickit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private EditText emailInput;
    private EditText passwordInput;
    private Button loginBtn;
    private Button loginRegBtn;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.log_email);
        passwordInput = findViewById(R.id.log_password);
        loginBtn = findViewById(R.id.login_btn);
        loginRegBtn = findViewById(R.id.login_reg_btnn);

        mAuth = FirebaseAuth.getInstance();


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lemail = emailInput.getText().toString().trim();
                String lpass = passwordInput.getText().toString().trim();

                if (!TextUtils.isEmpty(lemail) && (!TextUtils.isEmpty(lpass)))
                {
                    logIn(lemail, lpass);
                }else{
                    Toast.makeText(LoginActivity.this, "Please Fill up the Required Fields", Toast.LENGTH_LONG).show();
                }

            }
        });

        loginRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToRegister();

            }
        });

    }

    private void sendToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void logIn(String email, String password){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in, please wait...");
        progressDialog.show();


//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Logging in, please wait...");
//        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this, "Sign in successful.", Toast.LENGTH_LONG).show();
                    sendToMain();
                }else
                {
                    Toast.makeText(LoginActivity.this, "Error :" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser!=null)
        {
            sendToMain();
        }
    }

    private void sendToMain()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
