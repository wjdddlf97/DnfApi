package com.example.dnfapi.board;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dnfapi.MainActivity;
import com.example.dnfapi.MainBoardActivity;
import com.example.dnfapi.R;
import com.example.dnfapi.board.BoardAdpater;
import com.example.dnfapi.board.BoardInfoActivity;
import com.example.dnfapi.board.BoardInputActivity;
import com.example.dnfapi.function.VOS.BoardListView;
import com.example.dnfapi.function.FirebaseFunction;

import java.util.ArrayList;

public class BoardListActivity extends AppCompatActivity {

    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseFunction firebaseFunction = new FirebaseFunction();
    ArrayList<BoardListView> boardList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.member_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        swipeRefreshLayout = findViewById(R.id.swipeRefresh);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });


        findViewById(R.id.boardPlusBtn).setOnClickListener(onClickListener);
        findViewById(R.id.test).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.boardPlusBtn:
                    startBoardInputActivity();
                    break;
                case R.id.test:
                    startTestActivity();
                    break;

            }
        }


    };
    protected void onResume() {
        super.onResume();
        InitializeBoardData();
    }

    public void refresh() { // 새로고침 작동
        InitializeBoardData();
        swipeRefreshLayout.setRefreshing(false);
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

    public void InitializeBoardData()
    {
        boardList = new ArrayList<BoardListView>();
        firebaseFunction.getBoardList("",(result)->{
            ListView listView = (ListView)findViewById(R.id.board_item_view);
            final BoardAdpater boardAdpater = new BoardAdpater(this,boardList);
            listView.setAdapter(boardAdpater);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), BoardInfoActivity.class);
                    /* putExtra의 첫 값은 식별 태그, 뒤에는 다음 화면에 넘길 값 */
                    intent.putExtra("boardWriteDate",boardList.get(position).getWriteDate());
                    intent.putExtra("boardWriterId", boardList.get(position).getBoardWriterId());
                    startActivity(intent);
                }
            });

            if(result.size() > 0) {
                for (int i = 0; i < result.size(); i++) {
                    boardAdpater.addItem(result.get(i).getTitle(), result.get(i).getWriter(),result.get(i).getWriteDate(), result.get(i).getWriterId());
                }
            }else{
                boardAdpater.addItem("현재 게시글이 없습니다", " ", " ", " ");
            }
            boardAdpater.notifyDataSetChanged();
            return null;
        });

    }

    private void startBoardInputActivity() {
        Intent intent = new Intent(this, BoardInputActivity.class);
        startActivity(intent);
    }

    private void startTestActivity() {
        Intent intent = new Intent(this, MainBoardActivity.class);
        startActivity(intent);
    }
}