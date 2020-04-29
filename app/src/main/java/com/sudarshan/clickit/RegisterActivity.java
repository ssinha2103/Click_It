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

public class RegisterActivity extends AppCompatActivity {

    private EditText reg_email_feild;
    private EditText reg_pass;
    private EditText reg_confirm_pass;
    private Button reg_btn;
    private Button reg_login_btn;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        reg_email_feild = findViewById(R.id.reg_email);
        reg_pass = findViewById(R.id.password);
        reg_confirm_pass = findViewById(R.id.confirm_password);
        reg_btn = findViewById(R.id.register);
        reg_login_btn = findViewById(R.id.reg_login_btn);


        reg_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToLogin();
            }
        });


        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = reg_email_feild.getText().toString();
                String pass = reg_pass.getText().toString();
                String confirm_pass = reg_confirm_pass.getText().toString();

                if((!TextUtils.isEmpty(email)) && (!TextUtils.isEmpty(pass)) && (!TextUtils.isEmpty(confirm_pass)))
                {
                    if(pass.equals(confirm_pass))
                    {
                        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                        progressDialog.setMessage("Signing up, please wait...");
                        progressDialog.show();

                        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    progressDialog.dismiss();
                                    Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
                                    startActivity(setupIntent);
                                    finish();
                                }else {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Error :" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }else {
                        Toast.makeText(RegisterActivity.this, "Both the passwords do not match", Toast.LENGTH_LONG).show();
                    }


                }else {
                    Toast.makeText(RegisterActivity.this, "Please Fill up the Required Fields", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void sendToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
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

    private void sendToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}
