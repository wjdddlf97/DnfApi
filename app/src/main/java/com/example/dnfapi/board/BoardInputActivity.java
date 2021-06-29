package com.example.dnfapi.board;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.dnfapi.MainActivity;
import com.example.dnfapi.R;
import com.example.dnfapi.function.FirebaseFunction;

public class BoardInputActivity extends AppCompatActivity {

    FirebaseFunction firebaseFunction = new FirebaseFunction();

    EditText boardTitle;
    EditText boardContent;
    String type = "";
    String classType = "a";
    public String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_input);

        boardTitle = findViewById(R.id.boardTitle);
        boardContent = findViewById(R.id.boardContent);


        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        if(type.equals("class")) createDialog();

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
                    if(classType.equals("a")) {
                        firebaseFunction.insertBoardInfo(boardTitle.getText().toString(), boardContent.getText().toString(), userName, type);
                    }else{
                        firebaseFunction.insertClassBoardInfo(boardTitle.getText().toString(), boardContent.getText().toString(), userName, type,classType);
                    }
                    finish();
                    break;
                case R.id.cancelButton:
                    finish();

            }
        }


    };

    public void createDialog(){
        AlertDialog.Builder adServer = new AlertDialog.Builder(BoardInputActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.classtype_dialog,null);
        adServer.setTitle("직업 선택");       // 제목 설정
        adServer.setMessage("직업을 선택해주세요");   // 내용 설정

        Spinner sp = (Spinner)mView.findViewById(R.id.classSpinner);
        ArrayAdapter yearAdapter = ArrayAdapter.createFromResource(this, R.array.classType, android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(yearAdapter);
        serverPickSpinner(sp);
        adServer.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();     //닫기
                // Event
            }
        });
        adServer.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v("","No Btn Click");
                dialog.dismiss();     //닫기
                // Event
            }
        });

        adServer.setView(mView);
        adServer.show();

    }

    public void serverPickSpinner(Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch(position){
                    case 0:
                        classType="warrior(m)";
                        break;
                    case 1:
                        classType="warrior(w)";
                        break;
                    case 2:
                        classType="fighter(m)";
                        break;
                    case 3:
                        classType="fighter(w)";
                        break;
                    case 4:
                        classType="gunner(m)";
                        break;
                    case 5:
                        classType="gunner(w)";
                        break;
                    case 6:
                        classType="magician(m)";
                        break;
                    case 7:
                        classType="magician(w)";
                        break;
                    case 8:
                        classType="thief";
                        break;
                    case 9:
                        classType="gunKnife";
                        break;
                    case 10:
                        classType="magicLance";
                        break;
                    case 11:
                        classType="knight";
                        break;
                    case 12:
                        classType="create";
                        break;
                    case 13:
                        classType="darknight";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                classType="warrior(m)";
            }
        });
    }


}