package com.example.dnfapi.board;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.dnfapi.R;
import com.example.dnfapi.function.FirebaseFunction;

public class BoardInputActivity extends AppCompatActivity {

    FirebaseFunction firebaseFunction = new FirebaseFunction();

    EditText boardTitle;
    EditText boardContent;

    public String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_input);

        boardTitle = findViewById(R.id.boardTitle);
        boardContent = findViewById(R.id.boardContent);

        firebaseFunction.getMemberInfo((result)->{
            userName = result.get(0).getMemberName();


            return null;
        });

        findViewById(R.id.submitButton).setOnClickListener(onClickListener);
        findViewById(R.id.cancelButton).setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.submitButton:
                    firebaseFunction.insertBoardInfo(boardTitle.getText().toString(),boardContent.getText().toString(),userName);
                    finish();
                    break;
                case R.id.cancelButton:
                    finish();

            }
        }


    };


}