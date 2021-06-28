package com.example.dnfapi.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.dnfapi.R;
import com.example.dnfapi.function.FirebaseFunction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Report_adminActivity extends AppCompatActivity {
    RecyclerView directReport_recycler_view;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DirectReportListAdpater directReportListAdpater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_admin);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.directReport_admin_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        directReport_recycler_view = findViewById(R.id.directReport_recycler_view);

        directReport_recycler_view.setNestedScrollingEnabled(false);

        showDirectReportListFunc();


    }


    public void showDirectReportListFunc(){

        FirebaseFunction firebaseTest = new FirebaseFunction();
        if (user.getUid() != null) {
            firebaseTest.getDirectReportInfo(((result) -> {
                directReportListAdpater = new DirectReportListAdpater();
                directReport_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                //adapter 달기
                directReport_recycler_view.setAdapter(directReportListAdpater);

                directReport_recycler_view.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                    @Override
                    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                        if (e.getAction() == MotionEvent.ACTION_UP) {
                            View child = directReport_recycler_view.findChildViewUnder(e.getX(), e.getY());
                            int position = directReport_recycler_view.getChildAdapterPosition(child);

                            Intent intent = new Intent(getApplicationContext(), Report_adminDetailActivity.class);
                            /* putExtra의 첫 값은 식별 태그, 뒤에는 다음 화면에 넘길 값 */
                            intent.putExtra("name", result.get(position).getName());
                            intent.putExtra("userId", result.get(position).getUserId());
                            intent.putExtra("date", result.get(position).getDate());
                            startActivity(intent);
                        }
                        return false;
                    }

                    @Override
                    public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

                    }

                    @Override
                    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                    }
                });

                // null check
                if (result.size() > 0) {

                    int num = result.size();

                    for (int i = 0; i < num; i++) {
                        directReportListAdpater.addItem(result.get(i).getName(), result.get(i).getUserId(),result.get(i).getDate());
                    }
                    directReportListAdpater.notifyDataSetChanged();//adapter의 변경을 알림
                } else {
                    directReportListAdpater.addItem(
                            "보고받은 문제가 존재하지 않습니다",
                            " "," ");
                    directReportListAdpater.notifyDataSetChanged();//adapter의 변경을 알림
                }

                return null;

            }));

        }
    }

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