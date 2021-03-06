package com.example.dnfapi.board;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dnfapi.R;
import com.example.dnfapi.function.FirebaseFunction;
import com.example.dnfapi.function.VOS.ReplyFormVO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class BoardInfoActivity extends AppCompatActivity {

    FirebaseFunction firebaseFunction = new FirebaseFunction();
    SwipeRefreshLayout swipeRefreshLayout;



    TextView boardTitle;
    TextView boardContent;
    TextView boardWriter;
    EditText replyContent;
    String writerId;
    String writeDate;
    public String boardTitleSave;
    public String reportType;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<ReplyFormVO> replyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_info);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.member_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        boardTitle = findViewById(R.id.boardTitle);
        boardContent = findViewById(R.id.boardContent);
        boardWriter = findViewById(R.id.boardWriter);

        replyContent = findViewById(R.id.replyContent);

        Intent intent = getIntent();
        writerId = intent.getStringExtra("boardWriterId");
        writeDate = intent.getStringExtra("boardWriteDate");

        replyContent.bringToFront();

        getBoardContent(writerId,writeDate);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        getReplyContent(writerId, writeDate);




        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        findViewById(R.id.replyInputButton).setOnClickListener(onClickListener);
        findViewById(R.id.boardReport).setOnClickListener(onClickListener);



    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.replyInputButton:
                    inputReply();
                    break;
                case R.id.boardReport:
                    reportSpinnerShow();
                    break;

            }
        }


    };
    public void reportSpinnerShow(){
        AlertDialog.Builder adServer = new AlertDialog.Builder(BoardInfoActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.report_type_spinner,null);

        Spinner sp = (Spinner)mView.findViewById(R.id.reportSpinner);
        ArrayAdapter reportAdapter = ArrayAdapter.createFromResource(this, R.array.reportType, android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(reportAdapter);
        reportTypePickSpinner(sp);

        adServer.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //??????
                boardReportSend();
                // Event
            }
        });
        adServer.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //??????
                // Event
            }
        });

        adServer.setView(mView);
        adServer.show();
    }


    public void reportReplySpinnerShow(String replyContent, String replyWriterId){
        AlertDialog.Builder adServer = new AlertDialog.Builder(BoardInfoActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.report_type_spinner,null);

        Spinner sp = (Spinner)mView.findViewById(R.id.reportSpinner);
        ArrayAdapter reportAdapter = ArrayAdapter.createFromResource(this, R.array.reportType, android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(reportAdapter);
        reportTypePickSpinner(sp);

        adServer.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //??????
                replyReportSend(replyContent,replyWriterId);
                // Event
            }
        });
        adServer.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //??????
                // Event
            }
        });

        adServer.setView(mView);
        adServer.show();
    }

    public void reportTypePickSpinner(Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                switch(position){
                    case 0:
                        reportType="???????????? ??????";
                        break;
                    case 1:
                        reportType="???????????? ??????";
                        break;
                    case 2:
                        reportType="????????? ?????????";
                        break;
                    case 3:
                        reportType="?????? ????????? ??????";
                        break;
                    case 4:
                        reportType="????????? ??????";
                        break;
                    case 5:
                        reportType="??????";
                        break;
                    case 6:
                        reportType="??????";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                reportType="???????????? ??????";
            }
        });
    }


    public void boardReportSend() {
        firebaseFunction.insertReportInfo("?????????",boardTitle.getText().toString(),boardContent.getText().toString(),reportType,writerId);
    }
    public void replyReportSend(String replyContent, String replyWriterId) {
        firebaseFunction.insertReportInfo("??????",boardTitle.getText().toString(),replyContent,reportType, replyWriterId);
    }


    protected void onResume() {
        super.onResume();
        refreshBoard();
    }

    public void refresh() { // ???????????? ??????
        InitializeReplyData(boardTitleSave);
        swipeRefreshLayout.setRefreshing(false);
    }

    public void refreshBoard() { // ???????????? ??????
        getBoardContent(writerId,writeDate);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //toolbar??? back??? ????????? ??? ??????
                finish();
                return true;
            case R.id.goChangeBoard:
                if(writerId.equals(user.getUid()) || user.getUid().equals(firebaseFunction.getAdministerId())) {
                    startBoardChangeActivity();
                }else{

                }
                break;
            case R.id.goDeleteBoard:
                if(writerId.equals(user.getUid()) || user.getUid().equals(firebaseFunction.getAdministerId())) {
                    firebaseFunction.delBoard(boardTitle.getText().toString(), boardContent.getText().toString(),writerId);
                    finish();
                }else{

                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.board_menu, menu);
        return true;

    }

    public void getBoardContent(String writerId, String writeDate){

        firebaseFunction.getBoardInfo(writerId,writeDate,(result)->{
            boardTitleSave = result.get(0).getTitle();
            boardTitle.setText(boardTitleSave);
            boardContent.setText(result.get(0).getBoardContent());
            boardWriter.setText(result.get(0).getWriter());

            return null;
        });

    }


    public void getReplyContent(String writerId, String writeDate){
        firebaseFunction.getBoardInfo(writerId,writeDate,(result)->{
            InitializeReplyData(boardTitleSave);

            return null;
        });

    }
    public void inputReply(){
        firebaseFunction.getMemberInfo((result)->{
            firebaseFunction.insertReplyInfo(boardTitleSave, result.get(0).getMemberName(),replyContent.getText().toString());
            refresh();
            replyContent.setText("");
            return null;
        });

    }

    public void InitializeReplyData(String title)
    {
        replyList = new ArrayList<ReplyFormVO>();
        firebaseFunction.getReplyList(title,(result)->{
            ListView listView = (ListView)findViewById(R.id.board_reply_view);
            final ReplyAdpater replyAdpater = new ReplyAdpater(this,replyList, title);
            listView.setAdapter(replyAdpater);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    if(replyList.get(position).getWriteDate().equals(" ")){

                    }else {
                        showReportConfirm(replyList.get(position).getReplyContent(), replyList.get(position).getWriterId());
                    }
                }
            });

            if(result.size() > 0) {
                for (int i = 0; i < result.size(); i++) {
                    replyAdpater.addItem(result.get(i).getReplyContent(), result.get(i).getWriter(),result.get(i).getWriteDate(), result.get(i).getWriterId());
                    listView.bringChildToFront(replyContent);
                }
            }else{
                replyAdpater.addItem("?????? ????????? ????????????", " ", " ", " ");

            }
            replyAdpater.notifyDataSetChanged();
            return null;
        });

    }

    public void showReportConfirm(String replyContent, String replyWriterId){
        AlertDialog.Builder dialog = new AlertDialog.Builder(BoardInfoActivity.this);
        dialog.setMessage("?????? ????????? ?????????????????????????");

        dialog.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                reportReplySpinnerShow(replyContent, replyWriterId);
                dialogInterface.dismiss();
            }
        });
        dialog.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }


    private void startBoardChangeActivity() {
        Intent intent = new Intent(this, BoardChangeActivity.class);

        /* putExtra??? ??? ?????? ?????? ??????, ????????? ?????? ????????? ?????? ??? */
        intent.putExtra("boardWriteDate",writeDate);
        intent.putExtra("boardWriterId", writerId);

        startActivity(intent);
    }


}