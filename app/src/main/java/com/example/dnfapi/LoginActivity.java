package com.example.dnfapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        Toolbar mToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);


        getSupportActionBar().setDisplayShowTitleEnabled(false);
        findViewById(R.id.loginButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoSigninButton).setOnClickListener(onClickListener);

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }


    //각 버튼 온클릭 이벤트
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.loginButton:
                    login();
                    break;
                case R.id.gotoSigninButton:
                    startSignUpActivity();
                    finish();
                    break;

            }
        }
    };

    //일반 로그인 기능 함수
    private void login() {
        String email = ((EditText)findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordEditText)).getText().toString();

        if(email.length() > 0 && password.length() > 0) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                startToast("로그인에 성공했습니다.");
                                startMainActivity();
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                if(task.getException() != null) {
                                    startToast("로그인에 실패했습니다. 계정을 확인해주세요");
                                }
                            }
                        }
                    });
        }
        else {
            startToast("이메일 또는 비밀번호를 입력해 주세요.");
        }
    }


    //토스트 메세지 출력
    private void startToast(String msg) {
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }

    //회원가입 액티비티 이동 함수
    private void startSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    //메인 엑티비티로 이동 함수
    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}