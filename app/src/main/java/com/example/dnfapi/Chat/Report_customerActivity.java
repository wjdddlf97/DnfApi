package com.example.dnfapi.Chat;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dnfapi.R;
import com.example.dnfapi.function.FirebaseFunction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Report_customerActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    public  RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<ChatData> chatList;
    private String nick = "";

    private EditText EditText_chat;
    private Button Button_send;
    private Button endReportButton;
    private DatabaseReference myRef;
    FirebaseFunction firebaseFunction = new FirebaseFunction();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_customer);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.report_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button_send = findViewById(R.id.Button_send);
        EditText_chat = findViewById(R.id.EditText_chat);
        endReportButton = findViewById(R.id.endReportButton);


        firebaseFunction.getMemberInfo((result)->{
            if(result != null) {
                nick = result.get(0).getMemberName();
                mAdapter = new ChatAdapter(chatList, com.example.dnfapi.Chat.Report_customerActivity.this, nick);

                mRecyclerView.setAdapter(mAdapter);
            }
            return null;
        });


        findViewById(R.id.Button_send).setOnClickListener(onClickListener);
        findViewById(R.id.endReportButton).setOnClickListener(onClickListener);

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        chatList = new ArrayList<>();
        mAdapter = new ChatAdapter(chatList, com.example.dnfapi.Chat.Report_customerActivity.this, nick);

        mRecyclerView.setAdapter(mAdapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message/" +user.getUid());



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
                    break;
                case R.id.endReportButton:
                    ChatData chat = new ChatData();
                    chat.setNickName("endMessage");
                    chat.setMsg("상담이 종료되었습니다.");
                    myRef.push().setValue(chat);

                    firebaseFunction.updateIsEnd(user.getUid());
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