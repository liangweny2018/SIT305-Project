package com.project.sit305;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * DESC:
 * Date: 2019/4/2  8:56 PM
 * Time: 下午4:29
 * author: liang
 */
public class LoginActivity extends AppCompatActivity {


    private EditText username;
    private EditText password;
    private Button   login;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        this.login = (Button) findViewById(R.id.login_btn);
        this.password = (EditText) findViewById(R.id.myEdit_pswd);
        this.username = (EditText) findViewById(R.id.myEdit_uname);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("admin".equals(username.getText().toString().trim()) && "123456".equals(password.getText().toString().trim())) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "username or password not correct!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
