package com.example.dnfapi.board;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.dnfapi.R;
import com.example.dnfapi.function.FirebaseFunction;

public class BoardChangeActivity extends AppCompatActivity {

    FirebaseFunction firebaseFunction = new FirebaseFunction();

    TextView boardTitle;
    EditText boardContent;
    String writerId;
    String writeDate;
    public String userName;

    public String boardTitleSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_change);

        boardTitle = findViewById(R.id.boardTitle);
        boardContent = findViewById(R.id.boardContent);

        firebaseFunction.getMemberInfo((result)->{
            userName = result.get(0).getMemberName();


            return null;
        });

        Intent intent = getIntent();
        writerId = intent.getStringExtra("boardWriterId");
        writeDate = intent.getStringExtra("boardWriteDate");

        getBoardContent(writerId,writeDate);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.member_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.submitButton).setOnClickListener(onClickListener);
        findViewById(R.id.cancelButton).setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.submitButton:
                    firebaseFunction.updateBoardInfo(boardTitle.getText().toString(),boardContent.getText().toString());
                    finish();
                    break;
                case R.id.cancelButton:

                    finish();

            }
        }


    };

    public void getBoardContent(String writerId, String writeDate){

        firebaseFunction.getBoardInfo(writerId,writeDate,(result)->{
            boardTitleSave = result.get(0).getTitle();
            boardTitle.setText(boardTitleSave);
            boardContent.setText(result.get(0).getBoardContent());


            return null;
        });

    }


}