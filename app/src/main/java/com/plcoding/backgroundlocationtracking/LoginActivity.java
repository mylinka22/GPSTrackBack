package com.plcoding.backgroundlocationtracking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        sessionManager = new SessionManager(this);
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Проверяем, был ли пользователь ранее авторизован
        if (sessionManager.isLoggedIn() && currentUser != null) {
            navigateToMapActivity();
        }



        Button button_login = findViewById(R.id.button_login);
        Button button_reg = findViewById(R.id.button_reg);
        TextView textView_noacc = findViewById(R.id.textView_noacc);
        EditText edit_mail = findViewById(R.id.edit_mail);
        EditText edit_pass = findViewById(R.id.edit_pass);


        textView_noacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_login.setVisibility(View.INVISIBLE);
                button_reg.setVisibility(View.VISIBLE);
            }
        });

        button_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edit_mail.getText().toString();
                String password = edit_pass.getText().toString();
                if (!email.isEmpty() && !password.isEmpty())
                    RegUser(email, password);

            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edit_mail.getText().toString();
                String password = edit_pass.getText().toString();
                if (!email.isEmpty() && !password.isEmpty())
                    SingUser(email, password);

            }
        });

    }


    private void navigateToMapActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Закрываем LoginActivity, чтобы пользователь не мог вернуться назад
    }




    private void SingUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Авторизация успешна", Toast.LENGTH_LONG).show();

                            // Сохраняем состояние авторизации и адрес электронной почты
                            sessionManager.setLoggedIn(true);
                            sessionManager.setEmail(email);
                            navigateToMapActivity();
                        } else {
                            Log.e("FirebaseAuth", "Registration failed: " + task.getException());
                            Toast.makeText(LoginActivity.this, "Ошибка", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void RegUser(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Регистрация успешна", Toast.LENGTH_LONG).show();
                            Button button_login = findViewById(R.id.button_login);
                            Button button_reg = findViewById(R.id.button_reg);
                            button_login.setVisibility(View.VISIBLE);
                            button_reg.setVisibility(View.INVISIBLE);

                        } else {
                            Log.e("FirebaseAuth", "Registration failed: " + task.getException());
                            Toast.makeText(LoginActivity.this, "Ошибка", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}