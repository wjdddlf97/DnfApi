package com.example.dnfapi.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dnfapi.R;
import com.example.dnfapi.function.FirebaseFunction;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Report_adminDetailActivity extends AppCompatActivity {
    private EditText EditText_chat;
    private Button Button_send;
    private Button endReportButton;
    private DatabaseReference myRef;
    private RecyclerView reportDetailRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatData> chatList;
    private String nick = "관리자";
    public  RecyclerView.Adapter mAdapter;
    public String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_admin_detail);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.directReport_admin_detail_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Button_send = findViewById(R.id.Button_send);
        EditText_chat = findViewById(R.id.EditText_chat);
        endReportButton = findViewById(R.id.endReportButton);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        userId = intent.getStringExtra("userId");
        String date = intent.getStringExtra("date");


        findViewById(R.id.Button_send).setOnClickListener(onClickListener);
        findViewById(R.id.endReportButton).setOnClickListener(onClickListener);

        reportDetailRecyclerView = findViewById(R.id.directReport_recycler_view);
        reportDetailRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        reportDetailRecyclerView.setLayoutManager(mLayoutManager);

        chatList = new ArrayList<>();
        mAdapter = new ChatAdapter(chatList, com.example.dnfapi.Chat.Report_adminDetailActivity.this, nick);

        reportDetailRecyclerView.setAdapter(mAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message/" + userId);



        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("CHATCHAT", snapshot.getValue().toString());
                ChatData chat = snapshot.getValue(ChatData.class);
                ((ChatAdapter) mAdapter).addChat(chat);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }




    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.Button_send: // + 버튼 눌렀을 시 캐릭터 추가 dialog출력
                    String msg = EditText_chat.getText().toString(); //msg

                    if(msg != null) {
                        ChatData chat = new ChatData();
                        chat.setNickName(nick);
                        chat.setMsg(msg);
                        myRef.push().setValue(chat);
                    }
                    EditText_chat.setText("");
                    break;
                case R.id.endReportButton:
                    FirebaseFunction.updateIsEnd(userId);
                    break;

            }
        }


    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}